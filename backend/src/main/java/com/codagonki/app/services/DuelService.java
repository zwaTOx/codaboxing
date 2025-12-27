package com.codagonki.app.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.codagonki.app.DTO.Duel.DuelInfoResponse;
import com.codagonki.app.DTO.Duel.DuelResponse;
import com.codagonki.app.DTO.Duel.DuelsPaginationResponse;
import com.codagonki.app.DTO.User.UserResponse;
import com.codagonki.app.models.Duel;
import com.codagonki.app.models.Problem;
import com.codagonki.app.models.User;
import com.codagonki.app.repositories.DuelRepository;
import com.codagonki.app.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class DuelService{
    private final DuelRepository duelRepository;
    private final ProblemGenerateService problemGenerateService;
    private final DuelActionService duelActionService;
    private final UserRepository userRepository;

    public boolean isUserParticipatingInDuel(Long userId, Long duelId) {
        return duelRepository.findById(duelId)
            .map(duel -> userId.equals(duel.getHostUserId()) || 
                userId.equals(duel.getConnectingUserId()))
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,  
                "Дуэль не найдена"
            ));
    }

    public User getOpponent(Duel duel, User currentUser) {
        if (duel.getConnectingUserId().equals(currentUser.getId())) {
            return userRepository.findById(duel.getHostUserId()).get();
        } else {
            return userRepository.findById(duel.getConnectingUserId()).get();
        }
    }

    public DuelsPaginationResponse getUserDuelsPage(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Duel> duelsPage = duelRepository.findByHostUserIdOrConnectingUserId(userId, userId, pageable);
        List<DuelResponse> duelResponses = duelsPage.getContent()
            .stream()
            .map(DuelResponse::fromEntity)
            .collect(Collectors.toList());
        return DuelsPaginationResponse.of(
            duelResponses,
            page,
            size,
            duelsPage.getTotalPages()
        );
    }

    public DuelResponse createDuel(User user, Integer problemCount) {
        if (duelRepository.hasActiveDuel(user.getId())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,  
                "Пользователь уже находится в дуэли"
            );
        }

        if (duelRepository.hasWaitingDuel(user.getId())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,  
                "Пользователь уже ждёт начала дуэли"
            );
        }
        Duel newDuel = Duel.builder()
                .hostUserId(user.getId())
                .status(Duel.DuelStatus.WAITING)
                .problemCount(problemCount)
                .createdAt(LocalDateTime.now())
                .build();
        Duel savedDuel = duelRepository.save(newDuel);
        return DuelResponse.fromEntity(savedDuel);
    }
    
    public DuelInfoResponse getDuelInfo(User user, Long duelId){
        Duel duel = duelRepository.findById(duelId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,  
                "Дуэль не найдена"
            ));
        if (!duel.getHostUserId().equals(user.getId()) && 
            !user.getId().equals(duel.getConnectingUserId())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,  
                "Пользователь не участвует в этой дуэли"
            );
        }

        List<Long> participantIds = new ArrayList<>();
        participantIds.add(duel.getHostUserId());
        if (duel.getConnectingUserId() != null) {
            participantIds.add(duel.getConnectingUserId());
        }
        
        List<User> participants = userRepository.findAllById(participantIds);
        
        List<UserResponse> userResponses = participants.stream()
            .map(UserResponse::new)
            .collect(Collectors.toList());
        
        return new DuelInfoResponse(
            duel.getId(),
            userResponses,
            duel.getStatus().name(),
            duel.getCreatedAt(),
            duel.getStartTime(),
            duel.getProblemCount(),
            user.getId()
        );
    }

    public DuelResponse connectToDuel(User user) {
        List<Duel> availableDuels = duelRepository.findWaitingDuels();
        if (availableDuels.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Нет доступных для подключения дуэлей"
            );
        }
        Duel duelToConnect = availableDuels.get(0);
        if (duelToConnect.getHostUserId().equals(user.getId())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Нельзя подключиться к своей же дуэли"
            );
        } 
        duelToConnect.setConnectingUserId(user.getId());
        duelToConnect.setStatus(Duel.DuelStatus.ACTIVE);
        duelToConnect.setStartTime(LocalDateTime.now());
        Duel updatedDuel = duelRepository.save(duelToConnect);
        List<Problem> problems = problemGenerateService.generateRandomProblems(updatedDuel);
        duelActionService.setStartStatus(updatedDuel, problems);
        return DuelResponse.fromEntity(updatedDuel);
    }

    public void disconnectFromDuel(Long duelId, User user) {
        Duel duel = duelRepository.findById(duelId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,  
                "Дуэль не найдена"
            ));
        
        if (!duel.getHostUserId().equals(user.getId()) && 
            !user.getId().equals(duel.getConnectingUserId())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,  
                "Пользователь не участвует в этой дуэли"
            );
        }

        if (duel.getStatus() == Duel.DuelStatus.WAITING){
            duelRepository.delete(duel);
            return;
        }
        
        if (Arrays.asList(Duel.DuelStatus.CANCELLED, Duel.DuelStatus.COMPLETED).contains(duel.getStatus())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,  
                "Дуэль уже завершена или отменена"
            );
        }
        
        if (duel.getStatus() != Duel.DuelStatus.ACTIVE) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,  
                "Нельзя отключиться из неактивной дуэли"
            );
            
        }
        
        duel.setStatus(Duel.DuelStatus.CANCELLED);
        duelRepository.save(duel);
    } 
}
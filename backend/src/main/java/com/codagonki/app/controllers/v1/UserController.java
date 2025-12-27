package com.codagonki.app.controllers.v1;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codagonki.app.DTO.User.UpdatePasswordRequest;
import com.codagonki.app.DTO.User.UpdateProfileRequest;
import com.codagonki.app.DTO.User.UserProfileResponse;
import com.codagonki.app.DTO.Duel.DuelResultResponse;
import com.codagonki.app.dependencies.CurrentUser;
import com.codagonki.app.models.User;
import com.codagonki.app.services.DuelResultService;
import com.codagonki.app.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/users/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final DuelResultService duelResultService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(@CurrentUser User user) {
        UserProfileResponse userInfo = userService.getUserInfo(user);
        return ResponseEntity.ok(userInfo);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserProfileResponse> updateUserProfile(
            @CurrentUser User user,
            @Valid @RequestBody UpdateProfileRequest updateRequest) {
        UserProfileResponse updatedUser = userService.updateUserProfile(user, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/me/password")
    public ResponseEntity<UserProfileResponse> updateUserProfile(
            @CurrentUser User user,
            @Valid @RequestBody UpdatePasswordRequest updateRequest) {
        UserProfileResponse updatedUser = userService.updateUserPassword(user, updateRequest);
        return ResponseEntity.ok(updatedUser);  
    }

    @GetMapping("/me/duels")
    public ResponseEntity<List<DuelResultResponse>> getDuelResults(
            @CurrentUser User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<DuelResultResponse> duelResults = duelResultService.getUserDuelResults(user);
        return ResponseEntity.ok(duelResults);
    }
}

package com.codagonki.app.DTO.Duel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddProblemsToDuelRequest {
    private Long duelId;
    private List<Long> problemIds;
}
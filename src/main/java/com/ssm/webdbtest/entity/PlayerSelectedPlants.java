package com.ssm.webdbtest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerSelectedPlants {
    private Long id;
    private Long playerId;
    private Long levelId;
    private Long plantId;
}

package com.ssm.webdbtest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerData {
    private Long id;

    private String playerName;
    private String playId;
    private Integer pointId;
    // 用户选择的关卡列表
    private Set<Level> levels;

    public void addLevel(Level level) {
        levels.add(level);
    }

    public void removeLevel(Level level) {
        levels.remove(level);
    }
}

package com.ssm.webdbtest.service;
import com.ssm.webdbtest.entity.Level;
import com.ssm.webdbtest.entity.Plant;
import com.ssm.webdbtest.entity.Zombie;

import java.util.List;
import java.util.Set;

public interface LevelService {
    Level getLevelById(Long id);
    List<Level> getAllLevels();
    Level saveLevel(Level level);
    Level updateLevel(Level level);
    void deleteLevelById(Long id);
    Set<Plant> getPlantsByLevelId(Long levelId);
    Set<Zombie> getZombiesByLevelId(Long levelId);
    Set<Plant> getPlantsSelectedByPlayer(Long playerId, Long levelId); // 获取玩家选择的植物
}

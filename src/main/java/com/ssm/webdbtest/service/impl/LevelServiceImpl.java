package com.ssm.webdbtest.service.impl;
import com.ssm.webdbtest.entity.Level;
import com.ssm.webdbtest.entity.Plant;
import com.ssm.webdbtest.entity.Zombie;
import com.ssm.webdbtest.mapper.LevelMapper;
import com.ssm.webdbtest.mapper.ZombieMapper;
import com.ssm.webdbtest.service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class LevelServiceImpl implements LevelService {

    @Autowired
    private LevelMapper levelMapper;

    @Autowired
    private ZombieMapper zombieMapper;

    @Override
    public Level getLevelById(Long id) {
        return levelMapper.selectById(id);
    }

    @Override
    public List<Level> getAllLevels() {
        return levelMapper.selectList(null);
    }

    @Override
    public Level saveLevel(Level level) {
        levelMapper.insert(level);
        return level;
    }

    @Override
    public Level updateLevel(Level level) {
        levelMapper.updateById(level);
        return level;
    }

    @Override
    public void deleteLevelById(Long id) {
        levelMapper.deleteById(id);
    }

    @Override
    public Set<Zombie> getZombiesByLevelId(Long levelId) {
        // 同上，根据实际情况调整
        return new HashSet<>(zombieMapper.selectByLevelId(levelId));
    }
    //获取植物（还没做）
    @Override
    public Set<Plant> getPlantsByLevelId(Long levelId) {
        return Set.of();
    }
    @Override
    public Set<Plant> getPlantsSelectedByPlayer(Long playerId, Long levelId) {
        return Set.of();
    }
}

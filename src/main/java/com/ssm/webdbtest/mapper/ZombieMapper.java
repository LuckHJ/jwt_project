package com.ssm.webdbtest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ssm.webdbtest.entity.Zombie;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

@Mapper
public interface ZombieMapper extends BaseMapper<Zombie> {
    @Select("SELECT z.* FROM zombie z JOIN level_zombie lz ON lz.zombie_id = z.zombie_id WHERE lz.level_id = #{levelId}")
    public Set<Zombie> selectByLevelId(Long levelId);
}

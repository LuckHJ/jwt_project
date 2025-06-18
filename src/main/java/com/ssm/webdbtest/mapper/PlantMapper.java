package com.ssm.webdbtest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ssm.webdbtest.entity.Plant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface PlantMapper extends BaseMapper<Plant> {
    @Select("SELECT p.*\n" +
            "    FROM plant p\n" +
            "    JOIN player_selected_plants ps ON p.id = ps.plant_id\n" +
            "    WHERE ps.player_id = #{playerId} AND ps.level_id = #{levelId}")
    List<Plant> selectByPlayerAndLevel(@Param("playerId") Long playerId, @Param("levelId") Long levelId);
}

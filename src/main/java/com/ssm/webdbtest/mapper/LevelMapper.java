package com.ssm.webdbtest.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ssm.webdbtest.entity.Level;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LevelMapper extends BaseMapper<Level> {
    // 可以在这里添加自定义查询方法
}

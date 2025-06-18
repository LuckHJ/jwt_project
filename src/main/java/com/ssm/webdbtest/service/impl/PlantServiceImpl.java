package com.ssm.webdbtest.service.impl;
import com.ssm.webdbtest.entity.Plant;
import com.ssm.webdbtest.mapper.PlantMapper;
import com.ssm.webdbtest.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlantServiceImpl implements PlantService {

    @Autowired
    private PlantMapper plantMapper;

    @Override
    public Plant getPlantById(Long id) {
        return plantMapper.selectById(id);
    }

    @Override
    public List<Plant> getAllPlants() {
        return plantMapper.selectList(null);
    }

    @Override
    public Plant savePlant(Plant plant) {
        plantMapper.insert(plant);
        return plant;
    }

    @Override
    public Plant updatePlant(Plant plant) {
        plantMapper.updateById(plant);
        return plant;
    }

    @Override
    public void deletePlantById(Long id) {
        plantMapper.deleteById(id);
    }

}

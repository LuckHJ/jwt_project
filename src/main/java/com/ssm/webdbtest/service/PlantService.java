package com.ssm.webdbtest.service;
import com.ssm.webdbtest.entity.Plant;

import java.util.List;

public interface PlantService {
    //CRUD
    Plant getPlantById(Long id);
    List<Plant> getAllPlants();
    Plant savePlant(Plant plant);
    Plant updatePlant(Plant plant);
    void deletePlantById(Long id);
}

package com.ssm.webdbtest.entity;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Level {
    private Long id;
    private String name;
    private Integer roundNum;
    private Set<Zombie> zombies = new HashSet<>();
}
//    private Map<Integer,Zombie> zombiesRound =new HashMap<>();

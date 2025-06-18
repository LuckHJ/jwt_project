package com.ssm.webdbtest.entity;

import lombok.Data;
@Data
public class Zombie {
    private Long id;

    private String name; // 如 "Normal Zombie", "Flag Zombie"
    private Integer zombieId;
    private Integer health;
    private Integer state;
    private Integer damage;
    private Integer level;
}

package com.ssm.webdbtest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plant {
    private Long id;

    private String name; // 如 "Tree", "Flower"
    private String type;
    private Integer plantId;
    private Integer health;
    private Integer state;
    private Integer damage;
    private Integer level;
}

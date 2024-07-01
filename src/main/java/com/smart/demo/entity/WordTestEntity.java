package com.smart.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class WordTestEntity extends BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column(name = "test_t_point")
    private Integer test_point;

    @Column(name = "test_t_incorrect_percent")
    private Double test_incorrect_percent;

}

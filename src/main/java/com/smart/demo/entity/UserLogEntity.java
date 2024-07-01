package com.smart.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class UserLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private String login_ip;

    @Column
    private String login_agent;

//    @Column
//    public Date login_in_time;
//
//    @Column
//    public Date login_out_time;
}

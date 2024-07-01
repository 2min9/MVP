package com.smart.demo.entity;

import com.smart.demo.dto.AdminDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class AdminEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column
    private String admin_name;

    @Column
    private String admin_email;

    @Column
    private String admin_password;

    @Column
    private char rank;


    public static AdminEntity toAdminEntity(AdminDto adminDto) {
        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setAdmin_name(adminDto.getAdmin_name());
        adminEntity.setAdmin_email(adminDto.getAdmin_email());
        adminEntity.setAdmin_password(adminDto.getAdmin_password());
        adminEntity.setRank(adminDto.getRank());

        return adminEntity;
    }
}

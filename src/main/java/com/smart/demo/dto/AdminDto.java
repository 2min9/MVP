package com.smart.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdminDto {
    private Integer idx;
    private String admin_name;
    private String admin_email;
    private String admin_password;
    private char rank;
}

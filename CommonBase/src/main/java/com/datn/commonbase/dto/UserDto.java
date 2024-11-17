package com.datn.commonbase.dto;

import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserDto {
    private Long userId;
    private String name;
    private Date dateOfBirth;
    private int gender;
    private String language;
    private Long cash;
    private Long point;
    private String avatarUrl;
}

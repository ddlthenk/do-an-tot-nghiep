package com.devtam.commonbase.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailModel {
    private String subject;
    private String message;
}

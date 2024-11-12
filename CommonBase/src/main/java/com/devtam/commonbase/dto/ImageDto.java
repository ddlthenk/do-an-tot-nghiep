package com.devtam.commonbase.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {

    private long imageId;

    private int imageType;

    private String url;

    private int status;

    private long referenceId;

}

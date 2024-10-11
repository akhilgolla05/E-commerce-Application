package com.learnboot.dreamshopping.dto;

import lombok.Data;

import java.sql.Blob;

@Data
public class ImageDto {

    private long id;
    private String imageName;
    private String downloadUrl;
}

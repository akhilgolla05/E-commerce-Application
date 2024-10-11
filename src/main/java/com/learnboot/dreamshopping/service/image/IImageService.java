package com.learnboot.dreamshopping.service.image;

import com.learnboot.dreamshopping.dto.ImageDto;
import com.learnboot.dreamshopping.models.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {

    Image getImageById(long id);
    void deleteImageById(long id);
    List<ImageDto> saveImage(List<MultipartFile> images, Long ProductId);
    void updateImage(MultipartFile image, Long imageId);

}

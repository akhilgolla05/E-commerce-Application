package com.learnboot.dreamshopping.service.image;

import com.learnboot.dreamshopping.dto.ImageDto;
import com.learnboot.dreamshopping.exception.ResourceNotFoundException;
import com.learnboot.dreamshopping.models.Image;
import com.learnboot.dreamshopping.models.Product;
import com.learnboot.dreamshopping.repository.ImageRepository;
import com.learnboot.dreamshopping.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {
    private final ImageRepository imageRepository;
    private final IProductService productService;

    @Override
    public Image getImageById(long id) {
        return imageRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Image not found with id " + id));
    }

    @Override
    public void deleteImageById(long id) {
        imageRepository.findById(id)
                .ifPresentOrElse(imageRepository::delete, () -> {
                    throw new ResourceNotFoundException("Image not found with id " + id);
                });
    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> images, Long ProductId) {
        Product product = productService.getProductBYId(ProductId);
        List<ImageDto> savedImageDtos = new ArrayList<>();
        for (MultipartFile file : images) {
            try{
                Image image = new Image();
                image.setProduct(product);
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));

                String buildDownloadURL = "/api/v1/images/image/download/";
                String downloadURL = buildDownloadURL + image.getId(); //just dummy-Id assigned
                image.setDownloadUrl(downloadURL);

                Image savedImage = imageRepository.save(image);

                //now from saved image we can get the actual ID
                image.setDownloadUrl(buildDownloadURL+savedImage.getId());
                imageRepository.save(image);

                //return image to front-end
                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDtos.add(imageDto);
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDtos;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {

        Image dbImage = getImageById(imageId);
        try{
            dbImage.setFileName(file.getOriginalFilename());
            //dbImage.setFileType(file.getContentType());
            dbImage.setDownloadUrl(file.getOriginalFilename());
            dbImage.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(dbImage);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}

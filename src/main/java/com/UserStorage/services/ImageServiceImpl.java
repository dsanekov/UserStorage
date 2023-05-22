package com.UserStorage.services;

import com.UserStorage.dto.ImageDTO;
import com.UserStorage.dto.UserDTO;
import com.UserStorage.models.Image;
import com.UserStorage.models.User;
import com.UserStorage.repositories.ImagesRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
@Service
@Slf4j
public class ImageServiceImpl implements ImageService{
    private final ImagesRepository imagesRepository;
    @Autowired
    public ImageServiceImpl(ImagesRepository imagesRepository) {
        this.imagesRepository = imagesRepository;
    }

    @Override
    public ImageDTO saveImage(MultipartFile file) throws IOException {
        if (file.getSize() != 0) {
            Image image = toImageEntity(file);
            imagesRepository.save(image);
            log.info("Save new image " + file.getOriginalFilename());
            return convertToImageDTO(image);
        }
        return null;
    }

    @Override
    public ResponseEntity<Object> editImage(int id, MultipartFile file) throws IOException {
        Image image = imagesRepository.findById(id).orElse(null);
        if(file.getSize() != 0 && image != null){
            image.setName(file.getName());
            image.setSize(file.getSize());
            image.setContentType(file.getContentType());
            image.setOriginalFileName(file.getOriginalFilename());
            image.setBytes(file.getBytes());
            imagesRepository.save(image);
            log.info("Edit image with id " + id);
        }
        if(image == null){
            log.info("Image with id - " + id + " is not found!");
            return new ResponseEntity<>("Image with id - " + id + " is not found!", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok()
                .header("fileName",image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }

    @Override
    public ResponseEntity<Object> getImageById(int id) {
        Image image = imagesRepository.findById(id).orElse(null);
        if(image == null){
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        }
        ImageDTO imageDTO = convertToImageDTO(image);
        return ResponseEntity.ok()
                .header("fileName",imageDTO.getOriginalFileName())
                .contentType(MediaType.valueOf(imageDTO.getContentType()))
                .contentLength(imageDTO.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(imageDTO.getBytes())));
    }

    @Override
    public ResponseEntity<Object> getImageByUserId(int userId) {
        Image image = imagesRepository.findImageByUserId(userId);
        if(image == null){
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        }
        ImageDTO imageDTO = convertToImageDTO(image);
        return ResponseEntity.ok()
                .header("fileName",imageDTO.getOriginalFileName())
                .contentType(MediaType.valueOf(imageDTO.getContentType()))
                .contentLength(imageDTO.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(imageDTO.getBytes())));
    }

    private Image toImageEntity(MultipartFile file) throws IOException {
        Image newImage = new Image();
        newImage.setName(file.getName());
        newImage.setSize(file.getSize());
        newImage.setContentType(file.getContentType());
        newImage.setOriginalFileName(file.getOriginalFilename());
        newImage.setBytes(file.getBytes());
        return newImage;
    }
    @Override
    public Image getImageEntityById(int id) {
        return imagesRepository.findById(id).orElse(null);
    }
    private ImageDTO convertToImageDTO(Image image){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(image,ImageDTO.class);
    }
}

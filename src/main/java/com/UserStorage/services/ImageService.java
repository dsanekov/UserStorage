package com.UserStorage.services;

import com.UserStorage.dto.ImageDTO;
import com.UserStorage.models.Image;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    ImageDTO saveImage(MultipartFile file) throws IOException;
    ResponseEntity<Object> editImage(int id, MultipartFile file) throws IOException;
    ResponseEntity<Object> getImageById (int id);
    ResponseEntity<Object> getImageByUserId(int userId);
    Image getImageEntityById(int id);
}

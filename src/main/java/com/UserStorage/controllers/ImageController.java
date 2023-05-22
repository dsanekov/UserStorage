package com.UserStorage.controllers;

import com.UserStorage.dto.UserDTO;
import com.UserStorage.models.Image;
import com.UserStorage.models.User;
import com.UserStorage.repositories.ImagesRepository;
import com.UserStorage.services.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api2")
@Tag(name = "Image", description = "Methods for images")
@Slf4j
public class ImageController {
    private final ImageService imageService;
    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get image by id")
    @Transactional(readOnly = true)
    public ResponseEntity<Object> getImageById(@PathVariable("id") int id) {
        return imageService.getImageById(id);
    }

    @PatchMapping("/{id}/edit")
    @Operation(summary = "Replace image by id")
    public ResponseEntity<Object> editImageById(@PathVariable("id") int id,
                                                @RequestParam("file") MultipartFile file) throws IOException {
        return imageService.editImage(id,file);
    }
    @GetMapping("/get-by-user-id/{id}")
    @Operation(summary = "Get image by user id")
    @Transactional(readOnly = true)
    public ResponseEntity<Object> getImageByUserId(@PathVariable("id") int userId){
        return imageService.getImageByUserId(userId);
    }
}

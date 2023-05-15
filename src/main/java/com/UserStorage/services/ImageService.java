package com.UserStorage.services;

import com.UserStorage.models.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    Image saveImage(MultipartFile file) throws IOException;
}

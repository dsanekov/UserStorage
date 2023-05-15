package com.UserStorage.controllers;

import com.UserStorage.models.Image;
import com.UserStorage.models.User;
import com.UserStorage.repositories.UsersRepository;
import com.UserStorage.services.ImageService;
import com.UserStorage.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api1")
@Tag(name = "User", description = "Methods for users")
public class UserController {
    private final UsersRepository usersRepository;
    private final UsersService usersService;
    private final ImageService imageService;
    @Autowired
    public UserController(UsersRepository usersRepository, UsersService usersService, ImageService imageService) {
        this.usersRepository = usersRepository;
        this.usersService = usersService;
        this.imageService = imageService;
    }
    @PostMapping("/create")
    @Operation(summary = "Create new user")
    @Transactional
    public ResponseEntity<Object> createNewUser(@RequestParam(name = "surname") String surname,
                                                      @RequestParam(name = "name") String name,
                                                      @RequestParam(name = "middleName") String middleName,
                                                      @RequestParam(name = "dateOfBirth") LocalDate dateOfBirth,
                                                      @RequestParam(name = "email") String email,
                                                      @RequestParam(name = "phone") String phone,
                                                      @RequestParam("file") MultipartFile file
                                                      ) throws IOException {
        Image newImage = imageService.saveImage(file);
        if(newImage == null){
            return new ResponseEntity<>("Image size = 0",HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setSurname(surname);
        user.setName(name);
        user.setMiddleName(middleName);
        user.setDateOfBirth(dateOfBirth);
        user.setEmail(email);
        user.setPhone(phone);
        user.setImage(newImage);
        usersRepository.save(user);
        return new ResponseEntity<>(user.getId(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    @Transactional(readOnly = true)
    public ResponseEntity<Object> getUserById(@PathVariable("id") int id) {
        User user = usersRepository.findById(id).orElse(null);
        if(user == null){
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        }
        //TODO вернуть фотку нельзя сделай надо ДТО шку
        return new ResponseEntity<>(user,HttpStatus.OK);
    }
}

package com.UserStorage.controllers;

import com.UserStorage.dto.UserDTO;
import com.UserStorage.models.Image;
import com.UserStorage.models.User;
import com.UserStorage.repositories.UsersRepository;
import com.UserStorage.services.ImageService;
import com.UserStorage.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api1")
@Tag(name = "User", description = "Methods for users")
@Slf4j
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
            log.info("Image size = 0");
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
        log.info("User was created with id " + user.getId());
        return new ResponseEntity<>(user.getId(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    @Transactional(readOnly = true)
    public ResponseEntity<Object> getUserById(@PathVariable("id") int id) {
        User user = usersRepository.findById(id).orElse(null);
        if(user == null){
            log.info("User not found");
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        }
        log.info("Getting userDTO");
        UserDTO userDTO = convertToUserDTO(user);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    public ResponseEntity<Object> deleteUserById(@PathVariable("id") int id) {
        User user = usersRepository.findById(id).orElse(null);
        if(user != null){
            usersRepository.delete(user);
            log.info("User with id - " + id + " has been deleted!");
            return new ResponseEntity<>("User with id - " + id + " has been deleted!", HttpStatus.OK);
        }
        log.info("User with id - " + id + " is not found!");
        return new ResponseEntity<>("User with id - " + id + " is not found!", HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/{id}/edit/contactInfo")
    @Operation(summary = "Edit contact information by id")
    public ResponseEntity<Object> editContactInfoById(@PathVariable("id") int id,
                                                      @RequestParam(name = "email") String email,
                                                      @RequestParam(name = "phone") String phone) {

        User user = usersService.edit(id,email,phone);
        if(user == null){
            log.info("User not found");
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        }
        log.info("User's contact info with id - " + id + " has been edited");
        UserDTO userDTO = convertToUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}/edit/detailedInfo")
    @Operation(summary = "Edit detailed information by id")
    public ResponseEntity<Object> editDetailedInfoById(@PathVariable("id") int id,
                                                       @RequestParam(name = "surname") String surname,
                                                       @RequestParam(name = "name") String name,
                                                       @RequestParam(name = "middleName") String middleName,
                                                       @RequestParam(name = "dateOfBirth") LocalDate dateOfBirth) {

        User user = usersService.edit(id,surname,name, middleName, dateOfBirth);
        if(user == null){
            log.info("User not found");
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        }
        log.info("User's detailed info with id - " + id + " has been edited");
        UserDTO userDTO = convertToUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping()
    @Operation(summary = "Get all users")
    @Transactional(readOnly = true)
    public ResponseEntity<Object> getAllUsers(){
        List<User> users = usersRepository.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users){
            userDTOS.add(convertToUserDTO(user));
        }
        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }


    private UserDTO convertToUserDTO(User user){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user,UserDTO.class);
    }
}

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
    private final UsersService usersService;
    @Autowired
    public UserController(UsersService usersService) {
        this.usersService = usersService;
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
        return usersService.createNewUser(surname,name,middleName,dateOfBirth,email,phone,file);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    @Transactional(readOnly = true)
    public ResponseEntity<Object> getUserDTOById(@PathVariable("id") int id) {
        return usersService.getUserDTOById(id);
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    public ResponseEntity<Object> deleteUserById(@PathVariable("id") int id) {
        return usersService.deleteUserById(id);
    }

    @PatchMapping("/{id}/edit/contactInfo")
    @Operation(summary = "Edit contact information by id")
    public ResponseEntity<Object> editContactInfoById(@PathVariable("id") int id,
                                                      @RequestParam(name = "email") String email,
                                                      @RequestParam(name = "phone") String phone) {
        return usersService.editContactInfoById(id,email,phone);
    }

    @PatchMapping("/{id}/edit/detailedInfo")
    @Operation(summary = "Edit detailed information by id")
    public ResponseEntity<Object> editDetailedInfoById(@PathVariable("id") int id,
                                                       @RequestParam(name = "surname") String surname,
                                                       @RequestParam(name = "name") String name,
                                                       @RequestParam(name = "middleName") String middleName,
                                                       @RequestParam(name = "dateOfBirth") LocalDate dateOfBirth) {
        return usersService.editDetailedInfoById(id,surname,name,middleName,dateOfBirth);
    }

    @GetMapping()
    @Operation(summary = "Get all users")
    @Transactional(readOnly = true)
    public ResponseEntity<Object> getAllUsers(){
        return usersService.getAllUsers();
    }
}

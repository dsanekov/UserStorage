package com.UserStorage.services;

import com.UserStorage.dto.UserDTO;
import com.UserStorage.models.Image;
import com.UserStorage.models.User;
import com.UserStorage.repositories.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UsersService{
    private final UsersRepository usersRepository;
    private final ImageService imageService;
    @Autowired
    public UserServiceImpl(UsersRepository usersRepository, ImageService imageService) {
        this.usersRepository = usersRepository;
        this.imageService = imageService;
    }

    public User saveNewUser(String surname, String name, String middleName, LocalDate dateOfBirth, String email, String phone, Image image) {
        User user = new User();
        user.setSurname(surname);
        user.setName(name);
        user.setMiddleName(middleName);
        user.setDateOfBirth(dateOfBirth);
        user.setEmail(email);
        user.setPhone(phone);
        user.setImage(image);
        usersRepository.save(user);
        return user;
    }

    public User edit(int id, String email, String phone) {
        User userToReturn = usersRepository.findById(id).orElse(null);
        if(userToReturn == null)
            return null;
        userToReturn.setEmail(email);
        userToReturn.setPhone(phone);
        usersRepository.save(userToReturn);
        return userToReturn;
    }

    public User edit(int id, String surname, String name, String middleName, LocalDate dateOfBirth) {
        User userToReturn = usersRepository.findById(id).orElse(null);
        if(userToReturn == null)
            return null;
        userToReturn.setSurname(surname);
        userToReturn.setName(name);
        userToReturn.setMiddleName(middleName);
        userToReturn.setDateOfBirth(dateOfBirth);
        usersRepository.save(userToReturn);
        return userToReturn;
    }

    public UserDTO getUserDTO(User user) {
        return convertToUserDTO(user);
    }

    @Override
    public ResponseEntity<Object> getUserDTOById(int id) {
        User user = usersRepository.findById(id).orElse(null);
        if(user == null){
            log.info("User not found");
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        }
        log.info("Getting userDTO");
        UserDTO userDTO = getUserDTO(user);
        return new ResponseEntity<>(userDTO,HttpStatus.OK);
    }

    public List<UserDTO> getUserDTOList(List<User> users) {
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users){
            userDTOS.add(convertToUserDTO(user));
        }
        return userDTOS;
    }

    @Override
    public ResponseEntity<Object> deleteUserById(int id) {
        User user = usersRepository.findById(id).orElse(null);
        if(user != null){
            usersRepository.delete(user);
            log.info("User with id - " + id + " has been deleted!");
            return new ResponseEntity<>("User with id - " + id + " has been deleted!", HttpStatus.OK);
        }
        log.info("User with id - " + id + " is not found!");
        return new ResponseEntity<>("User with id - " + id + " is not found!", HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Object> editContactInfoById(int id, String email, String phone) {
        User user = edit(id,email,phone);
        if(user == null){
            log.info("User not found");
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        }
        log.info("User's contact info with id - " + id + " has been edited");
        UserDTO userDTO = getUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getAllUsers() {
        List<User> users = usersRepository.findAll();
        List<UserDTO> userDTOS = getUserDTOList(users);
        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> editDetailedInfoById(int id, String surname, String name, String middleName, LocalDate dateOfBirth) {
        User user = edit(id,surname,name, middleName, dateOfBirth);
        if(user == null){
            log.info("User not found");
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        }
        log.info("User's detailed info with id - " + id + " has been edited");
        UserDTO userDTO = getUserDTO(user);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> createNewUser(String surname, String name, String middleName, LocalDate dateOfBirth, String email, String phone, MultipartFile file) throws IOException {
        Image newImage = imageService.saveImage(file);
        if(newImage == null){
            log.info("Image size = 0");
            return new ResponseEntity<>("Image size = 0", HttpStatus.BAD_REQUEST);
        }
        User user = saveNewUser(surname,name,middleName,dateOfBirth,email,phone,newImage);
        log.info("User was created with id " + user.getId());
        return new ResponseEntity<>(user.getId(), HttpStatus.OK);
    }

    private UserDTO convertToUserDTO(User user){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user,UserDTO.class);
    }
}

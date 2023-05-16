package com.UserStorage.services;

import com.UserStorage.dto.UserDTO;
import com.UserStorage.models.Image;
import com.UserStorage.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface UsersService {

    ResponseEntity<Object> getUserDTOById (int id);
    ResponseEntity<Object> deleteUserById(int id);
    ResponseEntity<Object> editContactInfoById (int id, String email, String phone);
    ResponseEntity<Object> getAllUsers();
    ResponseEntity<Object> editDetailedInfoById (int id, String surname, String name, String middleName, LocalDate dateOfBirth);
    ResponseEntity<Object> createNewUser(String surname, String name, String middleName, LocalDate dateOfBirth, String email, String phone, MultipartFile file) throws IOException;
}

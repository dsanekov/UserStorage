package com.UserStorage.services;

import com.UserStorage.models.User;
import com.UserStorage.repositories.ImagesRepository;
import com.UserStorage.repositories.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class UserServiceImpl implements UsersService{
    private final UsersRepository usersRepository;
    private final ImagesRepository imagesRepository;
    @Autowired
    public UserServiceImpl(UsersRepository usersRepository, ImagesRepository imagesRepository) {
        this.usersRepository = usersRepository;
        this.imagesRepository = imagesRepository;
    }

    @Override
    public User edit(int id, String email, String phone) {
        User userToReturn = usersRepository.findById(id).orElse(null);
        if(userToReturn == null)
            return null;
        userToReturn.setEmail(email);
        userToReturn.setPhone(phone);
        usersRepository.save(userToReturn);
        return userToReturn;
    }

    @Override
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
}

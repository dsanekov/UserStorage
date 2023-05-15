package com.UserStorage.services;

import com.UserStorage.models.User;

import java.time.LocalDate;

public interface UsersService {
    User edit (int id, String email, String phone);

    User edit(int id, String surname, String name, String middleName, LocalDate dateOfBirth);
}

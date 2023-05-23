package com.UserStorage.repositories;

import com.UserStorage.models.SecurityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityUserRepository extends JpaRepository<SecurityUser, Integer> {
    Optional<SecurityUser> findByUserName(String userName);
}

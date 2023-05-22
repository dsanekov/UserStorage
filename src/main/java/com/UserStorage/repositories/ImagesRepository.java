package com.UserStorage.repositories;

import com.UserStorage.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagesRepository extends JpaRepository<Image,Integer> {
    Image findImageByUserId(int userId);
}

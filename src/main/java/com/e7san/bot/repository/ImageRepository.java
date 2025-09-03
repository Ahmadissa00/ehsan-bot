package com.e7san.bot.repository;

import com.e7san.bot.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Image findByName(String name);
    boolean existsByName(String name);
}

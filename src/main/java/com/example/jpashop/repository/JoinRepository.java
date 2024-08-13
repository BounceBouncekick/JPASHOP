package com.example.jpashop.repository;

import com.example.jpashop.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinRepository extends JpaRepository<UserEntity,String> {

    Boolean existsByUsername(String username);
    UserEntity findByUsername(String username);
}
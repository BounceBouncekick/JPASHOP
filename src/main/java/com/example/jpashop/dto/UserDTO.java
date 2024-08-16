package com.example.jpashop.dto;

import com.example.jpashop.entity.UserEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserDTO {

    private String username;
    private String password;
    private Integer age;
    private String email;


    @Builder
    public UserDTO(String username, String password, Integer age, String email) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.email = email;
    }

    public static UserDTO fromEntity(UserEntity userEntity) {
        return UserDTO.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .age(userEntity.getAge())
                .email(userEntity.getEmail())
                .build();
    }
}

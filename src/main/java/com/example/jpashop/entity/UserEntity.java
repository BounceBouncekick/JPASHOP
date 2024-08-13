package com.example.jpashop.entity;

import com.example.jpashop.dto.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "shop_jpauser")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "아이디는 필수 항목입니다.")
    @Size(min=2, message = "아이디는 최소 두 글자 이상입니다")
    @Column(nullable = false,unique = true)
    private String username;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "나이는 필수 항목입니다.")
    @Positive(message = "나이는 양수여야 합니다.")
    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false,unique = true)
    @NotEmpty(message = "이메일은 필수 항목입니다.")
    private String email;

    private String role;

    @Builder
    public UserEntity(Long id, String username, String password, Integer age, String email, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.age = age;
        this.email = email;
        this.role = role;
    }

    public static UserEntity toDTO(UserDTO userDTO) {
        return UserEntity.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .age(userDTO.getAge())
                .email(userDTO.getEmail())
                .build();
    }
}

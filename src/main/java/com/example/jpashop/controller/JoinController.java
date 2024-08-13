package com.example.jpashop.controller;


import com.example.jpashop.dto.UserDTO;
import com.example.jpashop.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;
    @PostMapping("/join")
    public ResponseEntity<?> joinProcess(@RequestBody UserDTO userDTO) throws IOException {

        log.info("회원가입 유저 비밀번호 : {}", userDTO.getPassword());
        log.info("회원가입 유저 닉네임 : {}", userDTO.getUsername());

        joinService.save(userDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

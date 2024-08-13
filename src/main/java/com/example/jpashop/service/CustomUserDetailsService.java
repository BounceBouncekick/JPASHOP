package com.example.jpashop.service;

import com.example.jpashop.entity.UserEntity;
import com.example.jpashop.repository.JoinRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final JoinRepository joinRepository;

    public CustomUserDetailsService(JoinRepository joinRepository) {

        this.joinRepository = joinRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username :{}",username);

        //DB에서 조회
        UserEntity userData = joinRepository.findByUsername(username);
        log.info("userData :{}",userData);
        if (userData != null) {

            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(userData);
        }


        return null;
    }
}

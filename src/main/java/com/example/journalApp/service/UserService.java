package com.example.journalApp.service;

import com.example.journalApp.entity.UserEntity;
import com.example.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository; // Corrected spelling

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    public List<UserEntity> getAll() {
        return userRepository.findAll(); // Corrected method name
    }

    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteById(Long id) {

    }

    public UserEntity findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }


    @Transactional
    public void deleteUser(String username) {
        Optional<UserEntity> user = Optional.ofNullable(userRepository.findByUserName(username));
        user.ifPresent(userRepository::delete);
    }

    public String verify(UserEntity user) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword()));
        if(authentication.isAuthenticated())
            return jwtService.genrateToken(user.getUserName());
        return "fail";
    }

}
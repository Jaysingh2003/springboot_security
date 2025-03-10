package com.example.journalApp.service;

import com.example.journalApp.entity.UserEntity;
import com.example.journalApp.entity.UserPrincipal;
import com.example.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository; 


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = repository.findByUserName(username);
        if (user == null){
            System.out.println("user Not found");
            throw new UsernameNotFoundException("user not found");

        }



        return new UserPrincipal(user);
    }
}

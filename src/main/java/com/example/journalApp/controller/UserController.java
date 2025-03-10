package com.example.journalApp.controller;

import com.example.journalApp.entity.UserEntity;
import com.example.journalApp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/users")
public class UserController { // Corrected spelling

    @Autowired
    private UserService userService;




    @GetMapping
    public List<UserEntity> getAllUsers() { // Corrected method name
        return userService.getAll();
    }
    @PostMapping ("/login")
    public  String login(@RequestBody UserEntity user){
        return userService.verify(user) ;
    }

    @PostMapping("/register")
    public UserEntity createUser(@RequestBody UserEntity user) {
        return userService.saveUser(user);
    }


    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@RequestBody UserEntity user, @PathVariable String username) {
        UserEntity userInDB = userService.findByUserName(username);
        if (userInDB != null) {
            userInDB.setUserName(user.getUserName()); // Corrected method name
            userInDB.setPassword(user.getPassword()); // Corrected method name
            userInDB.setRole(user.getRole());
            userService.saveUser(userInDB); // Ensure this method exists in your service
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        UserEntity userInDB = userService.findByUserName(username);
        if (userInDB != null) {
            userService.deleteUser(username);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
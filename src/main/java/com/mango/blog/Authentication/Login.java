package com.mango.blog.Authentication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mango.blog.Repositiory.RegisterRepository;
import com.mango.blog.Repositiory.UserRepository;
import com.mango.blog.User.User;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Document(collection = "BlogData")
public class Login implements Authentication {

    @Field
    private String userName;
    private String password;

    @Autowired
    private UserRepository userRepository;

    public Login() {
    }

    public Login(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public Login(UserRepository repo){
        this.userRepository = repo;
    }

    @PostMapping("/login")
    public ResponseEntity LoginUser(@RequestParam String userName, @RequestParam String password) {

        boolean isValid = false;
        String error = null;
        User tempUser;
        try {
            tempUser = userRepository.findByUserName(userName);
        } catch (Exception e) {
            tempUser = null;
        }
        if (tempUser == null) {
            error = "User not found";
            return new ResponseEntity<>("User not found", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (tempUser.getUserPassword().equals(password)) {
            isValid = true;
        } else {
            error = "Incorrect password";
        }

        if (error != null)
            return new ResponseEntity<>(error, HttpStatus.FOUND);

        // if we do tokens the token would be created and sent here
        return new ResponseEntity<>(isValid, HttpStatus.FOUND);

    }

    public String getUsername() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}

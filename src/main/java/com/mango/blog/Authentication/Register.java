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

import java.util.UUID;

@RestController
@Document(collection = "BlogData")
public class Register implements  Authentication{
    @Id
    private String id;

    @Field
    private String userName;
    private String password;
    private String email;
    private Login login;

    @Autowired
    private UserRepository userRepository;

    public Register(){}

    public Register(String id, String userName, String password, String email, Login login){
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.login = login;
    }

    @PostMapping("/register")
    public ResponseEntity RegisterUser(@RequestParam String userName, @RequestParam String password, @RequestParam String email)
    {
        try
        {
            //String id =  UUID.randomUUID().toString();

            User user = new User(userName, password, email);
            userRepository.save(user);

            return new ResponseEntity<>("success", HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

// Getters
//  and 
// Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

}

package com.mango.blog.Authentication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mango.blog.Repositiory.RegisterRepository;
import com.mango.blog.Repositiory.UserRepository;
import com.mango.blog.User.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
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
    public ResponseEntity LoginUser(@RequestParam String userName, @RequestParam String password) throws NoSuchAlgorithmException {

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
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        String storedPass = tempUser.getUserPassword();
        Base64.Decoder decoder = Base64.getDecoder();
        String salt = storedPass.substring(0, 16);
        byte[] decodedSalt = decoder.decode(salt);
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        Base64.Encoder encoder = Base64.getEncoder();
        md.update(decodedSalt);
        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
        String hashedPass = salt + encoder.encodeToString(hashedPassword);
        if (storedPass.equals(hashedPass)) {
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

package com.mango.blog.Authentication;

import com.mango.blog.Repositiory.UserRepository;
import com.mango.blog.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@RestController
@Document(collection = "BlogData")
@CrossOrigin(origins = "http://localhost:3000")
public class Login implements Authentication {

    @Field
    private String userName;
    private String password;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtGenerator jwtGenerator;


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
        if (this.jwtGenerator == null) {
            this.jwtGenerator = new JwtGenerator();
        }
        User tempUser;
        try {
            tempUser = userRepository.findByUserName(userName);
        } catch (Exception e) {
            tempUser = null;
        }
        if (tempUser == null) {
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
            return new ResponseEntity<>(jwtGenerator.generatetoken(tempUser), HttpStatus.FOUND);
        } else {
            return new ResponseEntity<>("invalid password", HttpStatus.FOUND);
        }
        

    }

    public String getUsername() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}

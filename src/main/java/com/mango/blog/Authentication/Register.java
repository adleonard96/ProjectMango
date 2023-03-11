package com.mango.blog.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mango.blog.Repositiory.RegisterRepository;

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
@Document(collection = "AuthData")
public class Register implements  Authentication{
    @Id
    private String id;

    @Field
    private String userName;
    private String password;
    private String email;
    private String phoneNumber;
    private Login login;

    @Autowired
    private RegisterRepository registerRepository;

    public Register(){}

    public Register(String id, String userName, String password, String email, String phoneNumber, Login login){
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.login = login;
    }

    // @GetMapping("/login1")
    // public ResponseEntity LoginCheck1(@RequestBody Register register){
    //     try{
    //         List<Register> registerTable = registerRepository.findAll();
    //         boolean isValid = false;
    //         for (int i= 0; i < registerTable.size(); i++) {
    //             if(register.userName.equals(registerTable.get(i).userName) && register.password.equals(registerTable.get(i).password)){
    //                 System.out.println('w');
    //                 isValid = true;
    //                 break;
    //             }
    //         }
    //         return new ResponseEntity<>(isValid, HttpStatus.FOUND);
    //     }
    //     catch (Exception e)
    //     {
    //         return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
        
    // }



    @PostMapping("/register")
    public ResponseEntity addABookToLibrary(@RequestBody Register register)
    {
        try
        {
            Register createdRegister = registerRepository.save(new Register(register.getId(), register.getUserName(),
                    register.getPassword(), register.getEmail(), register.getPhoneNumber(), register.getLogin()));
            return new ResponseEntity<>(createdRegister, HttpStatus.CREATED);
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public RegisterRepository getRegisterRepository() {
        return registerRepository;
    }

    public void setRegisterRepository(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }



}

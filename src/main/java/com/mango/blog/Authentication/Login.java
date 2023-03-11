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
public class Login implements Authentication {
    
    @Field
    private String userName;
    private String password;

    @Autowired
    private RegisterRepository registerRepository;

    public Login(){}

    public Login(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }



    @GetMapping("/login")
    public ResponseEntity LoginCheck(@RequestBody Login login){
        System.out.println(login.userName);
        System.out.println(login.password);
        try{
            List<Register> registerTable = registerRepository.findAll();
            boolean isValid = false;
            for (int i= 0; i < registerTable.size(); i++) {
                Register temp = registerTable.get(i);
                if(login.userName.equals(temp.getUserName()) && login.password.equals(temp.getPassword())){
                    System.out.println('w');
                    isValid = true;
                    break;
                }
            }
            return new ResponseEntity<>(isValid, HttpStatus.FOUND);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }


    public String getUsername() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}

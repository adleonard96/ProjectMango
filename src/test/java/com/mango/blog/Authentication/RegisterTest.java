package com.mango.blog.Authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mango.blog.Repositiory.UserRepository;
import com.mango.blog.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegisterTest {
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    private User testUser1;

    @MockBean
    private UserRepository repo;

    @InjectMocks
    private Register login = new Register();

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException {
        MockitoAnnotations.openMocks(this);
        this.mvc = MockMvcBuilders.standaloneSetup(login).build();
        String password = "testPassword1";
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[12];
        random.nextBytes(salt);
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
        Base64.Encoder encoder = Base64.getEncoder();
        String encodedSalt = encoder.encodeToString(salt);
        String encodedHashedPassword = encoder.encodeToString(hashedPassword);
        password = encodedSalt + encodedHashedPassword;

        this.testUser1 = new User("testUser1", password, "test@emil.com");
    }
    @Test
    void RegisterUser() throws Exception {
        Mockito.when(repo.findByUserName("user1")).thenReturn(null);
        //register a user and assert 201 status code for created
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("userName", "user1")
                        .param("password", "password1")
                        .param("email", "testemail@email.com"))
                .andReturn();

        assert result.getResponse().getStatus() == 201;
    }
}

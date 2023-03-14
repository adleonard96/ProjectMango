package com.mango.blog.Authentication;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mango.blog.Repositiory.UserRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationTest {

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    private UserRepository repo;

    @InjectMocks
    private Login login = new Login(repo);

    @Autowired
    private MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mvc = MockMvcBuilders.standaloneSetup(login).build();
    }

    @Test
    void RegisterUser() throws Exception {
        //register a user and assert 201 status code for created 
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/register")
                .param("userName", "user1")
                .param("password", "password1")
                .param("email", "testemail@email.com"))
                .andReturn();

        assert result.getResponse().getStatus() == 201;
    }

    @Test
    void LoginUser() throws Exception {
        // login a correct user and assert true
        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/login")
                .param("userName", "testUser1")
                .param("password", "testPassword1"))
                .andReturn();
        assert result.getResponse().getContentAsString().equals("true");

        // Attempt for an incorrect usernmae and assert "User not found" since the
        // username does not exist
        MvcResult result2 = mvc.perform(MockMvcRequestBuilders.post("/login")
                .param("userName", "faketestUser1")
                .param("password", "testPassword1"))
                .andReturn();
        assert result2.getResponse().getContentAsString().equals("User not found");

        // Attempt for an incorrect password and assert "Incorrect password" since the
        // username does not exist
        MvcResult result3 = mvc.perform(MockMvcRequestBuilders.post("/login")
                .param("userName", "testUser1")
                .param("password", "faketestPassword1"))
                .andReturn();
        assert result3.getResponse().getContentAsString().equals("Incorrect password");
    }

}

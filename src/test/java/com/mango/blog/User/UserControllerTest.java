package com.mango.blog.User;

import com.mango.blog.Post.PostController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mango.blog.Repositiory.UserRepository;
import com.mango.blog.User.User;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    User user = new User("TestUser", "Password", "test@user.com");
    User user2 = new User("TestUser2", "P4$$w0rd", "TestingUser");

    @MockBean
    private UserRepository repo;

    @InjectMocks
    private UserController userController = new UserController(repo);

    @Autowired
    private MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders.standaloneSetup(userController).build();
    }
    @Test
    void addFavoritePost() throws Exception {
        user.createPost("Test Post 1", "This is a test post number 1", "TestUser", "Unit Testing");

        Mockito.when(repo.findByUserName(anyString())).thenReturn(user2);
        String invalidBodyWithoutPostID = "{\"username\":\"TestUser2\",\"postName\":\"" + user.getPosts().get(0).getPostName() + "\"}";
        mvc.perform(MockMvcRequestBuilders.post("/User/FavoritePost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBodyWithoutPostID))
                .andExpect(status().isBadRequest());

        Mockito.when(repo.findByUserName(anyString())).thenReturn(user2);
        String validBody = "{\"username\":\"TestUser2\",\"postID\":\"" + user.getPosts().get(0).getPostID() + "\",\"postName\":\"" + user.getPosts().get(0).getPostName() + "\"}";
        mvc.perform(MockMvcRequestBuilders.post("/User/FavoritePost")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validBody))
                .andExpect(status().isCreated());

        assert user2.getFavoritePosts().size() == 1;



    }

    @Test
    void removeFavoritePost() throws Exception {
        user.createPost("Test Post 1", "This is a test post number 1", "TestUser", "Unit Testing");
        user2.addFavoritePost(user.getPosts().get(0).getPostID(), user.getPosts().get(0).getPostName());

        Mockito.when(repo.findByUserName(anyString())).thenReturn(user2);
        String invalidBodyWithoutPostID = "{\"username\":\"TestUser2\"}";
        mvc.perform(MockMvcRequestBuilders.delete("/User/RemoveFavoritePost")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidBodyWithoutPostID))
                .andExpect(status().isBadRequest());


        Mockito.when(repo.findByUserName(anyString())).thenReturn(user2);
        String validBody = "{\"username\":\"TestUser2\",\"postID\":\"" + user.getPosts().get(0).getPostID() + "\"}";
        mvc.perform(MockMvcRequestBuilders.delete("/User/RemoveFavoritePost")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validBody))
                .andExpect(status().isOk());

        assert user2.getFavoritePosts().size() == 0;



    }

    @Test
    void getFavoritePosts() throws Exception {
        user.createPost("Test Post 1", "This is a test post number 1", "TestUser", "Unit Testing");
        user.createPost("Test Post 2", "This is a test post number 2", "TestUser", "Unit Testing");

        user2.addFavoritePost(user.getPosts().get(0).getPostID(), user.getPosts().get(0).getPostName());
        user2.addFavoritePost(user.getPosts().get(1).getPostID(), user.getPosts().get(1).getPostName());

        Mockito.when(repo.findByUserName(anyString())).thenReturn(user2);
        mvc.perform(MockMvcRequestBuilders.get("/User/FavoritePosts")
                .contentType(MediaType.APPLICATION_JSON)
                .param("username", "TestUser2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));

    }

    @Test
    void addUserInGroup(){
        user.addGroup("Baking");
        user.addUserToGroup("Baking", user2.getUserID(), user2.getUserName());


        Mockito.when(repo.findByUserName(anyString())).thenReturn(user);
        mvc.perform(MockMvcRequestBuilders.get("/User/UserGroups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));

    }

    @Test
    void getUserInGroups() {
        user.addGroup("Baking");
        user.addUserToGroup("Baking", user2.getUserID(), user2.getUserName());

        user.getUsersInGroup("Baking");

        Mockito.when(repo.findByUserName(anyString())).thenReturn(user);
        mvc.perform(MockMvcRequestBuilders.get("/User/UserGroups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser2"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));

        assert user.getUsersInGroup("Baking").size() == 1;

    }

    @Test
    void removeUserFromGroup() {
        user.addGroup("Business");
        user.addUserToGroup("Business", user2.getUserID(), user2.getUserName());
        user.removeUserFromGroup("Business", user2.getUserName());

        Mockito.when(repo.findByUserName(anyString())).thenReturn(user);
        mvc.perform(MockMvcRequestBuilders.get("/User/UserGroups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", "TestUser"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));

        assert user.getUsersInGroup("Business").size() == 0;
    }

    @Test
    void removeGroup() {
        // TODO: Implement removeGroup() test after create group method is implemented
        user.addGroup("Sports");
        user.removeGroup("Sports");

        assert user.getUserGroups("Sports").size()==0; //I'm hunting for getting the actual user groups but
    }

}
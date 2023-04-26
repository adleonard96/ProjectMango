package com.mango.blog.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mango.blog.Authentication.JwtGenerator;
import com.mango.blog.Repositiory.UserRepository;
import org.junit.Before;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public
class UserControllerTest {

    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    User user = new User("TestUser", "Password", "test@user.com");
    User user2 = new User("TestUser2", "P4$$w0rd", "TestingUser");
    private final JwtGenerator jwtGenerator = new JwtGenerator();
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
    public String createToken() {
        Mockito.when(repo.findByUserName(anyString())).thenReturn(user);
        Map<String,String> tokenMap = jwtGenerator.generatetoken(user);
        return tokenMap.get("token");
    }
    @Test
    public void addFavoritePost() throws Exception {
        user.createPost("Test Post 1", "This is a test post number 1", "TestUser", "Unit Testing");
        String token = createToken();
        Mockito.when(repo.findByUserName(anyString())).thenReturn(user2);
        String invalidBodyWithoutPostID = "{\"postName\":\"" + user.getPosts().get(0).getPostName() + "\"}";
        mvc.perform(MockMvcRequestBuilders.post("/User/FavoritePost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(invalidBodyWithoutPostID))
                .andExpect(status().isBadRequest());

        Mockito.when(repo.findByUserName(anyString())).thenReturn(user2);
        String validBody = "{\"username\":\"TestUser2\",\"postID\":\"" + user.getPosts().get(0).getPostID() + "\",\"postName\":\"" + user.getPosts().get(0).getPostName() + "\"}";
        mvc.perform(MockMvcRequestBuilders.post("/User/FavoritePost")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(validBody))
                .andExpect(status().isCreated());

        assert user2.getFavoritePosts().size() == 1;



    }

    @Test
    public void removeFavoritePost() throws Exception {
        user.createPost("Test Post 1", "This is a test post number 1", "TestUser", "Unit Testing");
        user2.addFavoritePost(user.getPosts().get(0).getPostID(), user.getPosts().get(0).getPostName());
        String token = createToken();
        Mockito.when(repo.findByUserName(anyString())).thenReturn(user2);
        String invalidBodyWithoutPostID = "{\"username\":\"TestUser2\"}";
        mvc.perform(MockMvcRequestBuilders.delete("/User/RemoveFavoritePost")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(invalidBodyWithoutPostID))
                .andExpect(status().isBadRequest());


        Mockito.when(repo.findByUserName(anyString())).thenReturn(user2);
        String validBody = "{\"username\":\"TestUser2\",\"postID\":\"" + user.getPosts().get(0).getPostID() + "\"}";
        mvc.perform(MockMvcRequestBuilders.delete("/User/RemoveFavoritePost")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(validBody))
                .andExpect(status().isOk());

        assert user2.getFavoritePosts().size() == 0;
    }

    @Test
    public void getFavoritePosts() throws Exception {
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
    public void addUserGroup() throws Exception {
        Mockito.when(repo.findByUserName(anyString())).thenReturn(user);
        String validBody = "{\"username\":\"" + user.getUserName() + "\",\"groupName\":\"TestGroup\"}";
        String token = createToken();
        mvc.perform(MockMvcRequestBuilders.post("/User/Group")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(validBody))
                .andExpect(status().isCreated());

        assert user.getGroups().size() == 1;

    }
    @Test
    public void getUserGroups() throws Exception {
        user.addGroup("TestGroup");
        Mockito.when(repo.findByUserName(anyString())).thenReturn(user);
        mvc.perform(MockMvcRequestBuilders.get("/User/Groups")
                .contentType(MediaType.APPLICATION_JSON)
                .param("username", "TestUser"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)));

    }

    @Test
    public void removeUserFromGroup() throws Exception {
        user.addGroup("TestGroup");
        user.addUserToGroup("TestGroup", "15", user2.getUserName());
        String token = createToken();
        Mockito.when(repo.findByUserName(anyString())).thenReturn(user);
        String validBody = "{\"groupName\":\"TestGroup\",\"userToRemove\":\"" + user2.getUserName() + "\"}";

        mvc.perform((MockMvcRequestBuilders.put("/User/RemoveUserFromGroup")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(validBody)))
                .andExpect(status().isOk());

    }

    @Test
    public void removeGroup() throws Exception {
        user.addGroup("TestGroup");
        Mockito.when(repo.findByUserName(anyString())).thenReturn(user);
        String token = createToken();
        String validBody = "{\"groupName\":\"TestGroup\"}";
        assert user.getGroups().size() == 1;

        mvc.perform((MockMvcRequestBuilders.delete("/User/RemoveGroup")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(validBody)))
                .andExpect(status().isOk());

        assert user.getGroups().size() == 0;
    }

    @Test
    public void getUserDetails() throws Exception{
        Mockito.when(repo.findByUserName(anyString())).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.get("/User")
                .contentType(MediaType.APPLICATION_JSON)
                .param("username", "TestUser"))
                .andExpect(status().isOk());
    }

    @Test
    public void getUsersInGroup() throws Exception{
        user.addGroup("TestGroup");
        user.addUserToGroup("TestGroup", "15", user2.getUserName());
        Mockito.when(repo.findByUserName(anyString())).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders.get("/User/Group")
                .contentType(MediaType.APPLICATION_JSON)
                .param("username", "TestUser")
                .param("groupName", "TestGroup"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)));
    }

    @Test
    public void getAllPostsForAUser() throws Exception{
        user.createPost("Test Post 1", "This is a test post number 1", "TestUser", "Unit Testing");
        user.createPost("Test Post 2", "This is a test post number 2", "TestUser", "Unit Testing");
        Mockito.when(repo.getAllPostsByUser(anyString())).thenReturn(user.getPosts().toString());

        mvc.perform(MockMvcRequestBuilders.get("/Posts/AllPostsByUser")
                .contentType(MediaType.APPLICATION_JSON)
                .param("userName", "TestUser"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
    }
}
package com.mango.blog.Post;

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
public class PostControllerTest {

    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    User user = new User("TestUser", "Password", "test@user.com");
    User user2 = new User("TestUser2", "P4$$w0rd", "TestingUser");


    @MockBean
    private UserRepository repo;

    @InjectMocks
    private PostController postController = new PostController(repo);

    @Autowired
    private MockMvc mvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test
    void createGenericPostPost() throws Exception {
        Mockito.when(repo.findByUserName(anyString())).thenReturn(user);
        String validBody = "{\"postName\":\"Test Post\",\"text\":\"This is a test post\",\"author\":\"TestUser\",\"genre\":\"UnitTest\"}";
        mvc.perform(MockMvcRequestBuilders
                        .post("/Posts/GeneralPost").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic YWRtaW46YWRtaW4=").content(validBody))
                .andExpect(status().isCreated());

        assert user.getPosts().size() == 1;
    }
    @Test
    void deleteGenericPostPost() throws Exception { //def didn't use mockito right here
        Mockito.when(repo.findByUserName(anyString())).thenReturn(user);
        user.createPost("Test", "This is a test post", "TestingUser", "Baking");
        Post test = user.getPosts().get(0);
        String postID = test.getPostID();
        String validBody = "{\"postID\":\"" + user.getPosts().get(0).getPostID() + "\",\"postName\":\"Test\",\"text\":\"This is a test post\",\"author\":\"TestingUser\",\"genre\":\"Baking\"}";
        mvc.perform(MockMvcRequestBuilders
                .delete("/GeneralPost").contentType(MediaType.APPLICATION_JSON)
                        .content(validBody))
                .andExpect(status().isCreated());

        assert user.getPosts().size() == 0;
    }

    @Test
    void getGenericPost() {
        user.createPost("Test", "This is a test post", "TestingUser", "Baking");
        Post post = user.getPosts().get(0);

        assert post != null;
    }

    @Test
    void postById() throws Exception {
        user.createPost("Test Post", "This is a test post", "TestUser", "UnitTest");
        Post test = user.getPosts().get(0);
        String postID = test.getPostID();

        Mockito.when(repo.getPostsById(anyString())).thenReturn(test.toString());

        mvc.perform(MockMvcRequestBuilders
                .get("/Posts/PostById").contentType(MediaType.APPLICATION_JSON)
                .param("postID", postID).header("Authorization", "Basic YWRtaW46YWRtaW4="))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postID").value(postID))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postName").value("Test Post"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("This is a test post"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("TestUser"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("UnitTest"));
    }


    @Test
    void getGenres() throws Exception {
        user.createPost("Test Post", "This is a test post", user.getUserName(), "UnitTest");
        user.createPost("Test Post 2", "This is a test post", user.getUserName(), "Mockito");
        user.createPost("Test Post 3", "This is a test post", user.getUserName(), "JUnit");
        String returnString = "[{\"_id\":\"UnitTest\",\"count\":1},{\"_id\":\"Mockito\",\"count\":1},{\"_id\":\"JUnit\",\"count\":1}]";

        Mockito.when(repo.getGenres()).thenReturn(returnString);

        mvc.perform(MockMvcRequestBuilders
                .get("/Posts/Genres").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void postsByGenre() throws Exception {
        user.createPost("Test Post", "This is a test post", user.getUserName(), "UnitTest");
        user.createPost("Test Post 2", "This is a test post", user.getUserName(), "Mockito");
        user.createPost("Test Post 3", "This is a test post", user.getUserName(), "JUnit");

        String returnString = "[{\"postID\":\"" + user.getPosts().get(0).getPostID() + "\",\"postName\":\"Test Post\",\"text\":\"This is a test post\",\"author\":\"TestUser\",\"createdOn\": {\"$date\": \"" + user.getPosts().get(0).getCreatedOn() + "\"}}";

        Mockito.when(repo.getPostsByGenre(anyString())).thenReturn(returnString);

        mvc.perform(MockMvcRequestBuilders
        .get("/Posts/ByGenre").contentType(MediaType.APPLICATION_JSON)
                .param("genre", "UnitTest"))
                .andExpect(status().isOk());
    }

    @Test
    void updatePost() throws Exception {
        user.createPost("Test Post", "This is a test post", user.getUserName(), "UnitTest");
        Post test = user.getPosts().get(0);
        String postID = test.getPostID();

        String validBody = "{\"postID\":\"" + postID + "\",\"postName\":\"" + test.getPostName() + "\",\"text\":\"This is an updated body\",\"author\":\"" + test.getAuthor() + "\",\"genre\":\"" + test.getGenre() + "\"}";

        Mockito.when(repo.findByUserName(anyString())).thenReturn(user);

        mvc.perform(MockMvcRequestBuilders
                .put("/Posts/UpdateGeneralPost").contentType(MediaType.APPLICATION_JSON).content(validBody)).andExpect(status().isOk());

        assert user.getPosts().get(0).getText().equals("This is an updated body");
    }
}
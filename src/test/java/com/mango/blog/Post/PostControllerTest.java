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
    void createGenericPostPost() {
    }

    @Test
    void getGenericPost() {
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

}
package com.mango.blog.Comment;

import com.mango.blog.Authentication.JwtGenerator;
import com.mango.blog.Repositiory.UserRepository;
import com.mango.blog.User.User;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @MockBean
    private UserRepository repo;
    @InjectMocks
    private CommentController commentController = new CommentController(repo);
    @Autowired
    private MockMvc mvc;

    private final JwtGenerator jwtGenerator = new JwtGenerator();

    User user = new User("user1", "password", "test@email.com");
    User user2 = new User("user2", "password", "test2@email.com");



    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }
    @Test
    void createCommentTest() throws Exception {
        user.createPost("Comment post", "This is a comment post", user.getUserName(), "UnitTesting");
        Mockito.when(repo.findByUserName("user1")).thenReturn(user); //Post author
        Mockito.when(repo.getPostsById("1234")).thenReturn(user.getPosts().get(0).toJsonString());
        Mockito.when(repo.findByUserName("user2")).thenReturn(user2); //commenter
        Mockito.when(repo.save(user)).thenReturn(user);

        String jsonBody = "{\"postID\": \"1234\", \"text\": \"This is a comment\"}";
        Map<String, String> tokenMap = jwtGenerator.generatetoken(user2);
        String token = tokenMap.get("token");

        mvc.perform(MockMvcRequestBuilders
                .post("/Comment")
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(jsonBody))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteComment() throws Exception {
        user.createPost("Comment post", "This is a comment post", user.getUserName(), "UnitTesting");
        Comment comment = new Comment("user2", "This is a comment");
        user.addComment(user.getPosts().get(0).getPostID(), comment);
        Mockito.when(repo.findByUserName("user1")).thenReturn(user); //Post author
        Mockito.when(repo.getPostsById(anyString())).thenReturn(user.getPosts().get(0).toJsonString());
        Mockito.when(repo.findByUserName("user2")).thenReturn(user2); //commenter
        Mockito.when(repo.save(user)).thenReturn(user);

        String jsonBody2 = "{\"postID\": \"1234\", \"commentID\": \"" + user.getPosts().get(0).getComments().get(0).getCommentID() + "\"}";
        String jsonBody = "{\"postID\":" +" \"" + user.getPosts().get(0).getPostID() + "\"" + ", \"commentID\": \"" + user.getPosts().get(0).getComments().get(0).getCommentID() + "\"}";
        Map<String, String> tokenMap = jwtGenerator.generatetoken(user2);
        String token = tokenMap.get("token");

        mvc.perform(MockMvcRequestBuilders
                .delete("/Comment")
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .content(jsonBody))
                .andExpect(status().isOk());
    }
}

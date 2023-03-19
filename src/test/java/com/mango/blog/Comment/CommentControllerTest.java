package com.mango.blog.Comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mango.blog.Repositiory.UserRepository;
import com.mango.blog.User.User;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.Console;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mango.blog.Authentication.Register;
import com.mango.blog.Comment.*;
import com.mango.blog.Post.GeneralPost;

import com.mango.blog.*;
import com.mango.blog.User.User;
import com.mango.blog.Comment.Comment;;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @MockBean
    @Autowired
    private UserRepository repo;

    @InjectMocks
    @Autowired
    private CommentController commentController = new CommentController(repo);

    User user = new User("user1", "password", "test@email.com");
    GeneralPost post = new GeneralPost("1234", new ArrayList<Comment>(), "user1", "this is the post", "TestUser",
            "testing");

    @Before
    public void setup() {
        repo = mock(UserRepository.class);
        commentController = new CommentController(repo);
    }

    @Test
    public void createCommentTest() throws JsonProcessingException {
        // Set up test data
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("postID", "1234");
        requestBody.put("text", "This is a comment");
        requestBody.put("userName", "user1");

        when(repo.findByUserName("user1")).thenReturn(user);
        when(repo.getPostsById("1234")).thenReturn(new ObjectMapper().writeValueAsString(post));
        user.getPosts().add(post); // add post to user

        // Call createComment
        ResponseEntity response = commentController.createComment(requestBody);

        // Assert the comment was created
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Comment created", response.getBody());

    }

    @Test
    public void deleteCommentTest() throws JsonProcessingException {
        user.getPosts().add(post); // add post to user
        // create a comment, add it to the post
        Comment comment = new Comment("user1", "This is a comment");
        post.getComments().add(comment);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("postID", "1234");
        requestBody.put("commentID", post.getComments().get(0).getCommentID());
        requestBody.put("userName", "user1");

        when(repo.findByUserName("user1")).thenReturn(user);
        when(repo.getPostsById("1234")).thenReturn(new ObjectMapper().writeValueAsString(post));

        // Call createComment
        ResponseEntity response = commentController.deleteComment(requestBody);

        // Assert the comment was deleted
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Comment deleted", response.getBody());
    }
}

package com.mango.blog.Comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
//@RunWith(MockitoJUnitRunner.class)
public class CommentControllerTest {

    @MockBean
    @Autowired
    private UserRepository repo;

    @InjectMocks
    @Autowired
    private CommentController commentController = new CommentController(repo);

    @Before
    public void setup() {
        repo = mock(UserRepository.class);
        commentController = new CommentController(repo);
        System.out.println(repo);
    }

    @Test
    public void createCommentTest() throws JsonProcessingException {
        System.out.println(repo);
        // Set up test data
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("postID", "1");
        requestBody.put("text", "This is a comment");
        requestBody.put("userName", "user1");

        User user = new User("user1", "password", "test@email.com");
        when(repo.findByUserName("user1")).thenReturn(user);

        GeneralPost post = new GeneralPost("1234", new ArrayList<Comment>(),"user1", "this is the post", "TestUser", "testing");
        when(repo.getPostsById("1")).thenReturn(new ObjectMapper().writeValueAsString(post));

        //Comment comment = new Comment(user.getUserName(), "This is a comment");

        //when(user.addComment(anyString(), comment )).thenReturn(true);
        // Call the controller method
        ResponseEntity response = commentController.createComment(requestBody);

        // Verify the response
        System.out.println(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Comment created", response.getBody());

        // Verify that the comment was added to the post author
        verify(repo, times(1)).findByUserName("user1");
        verify(repo, times(1)).getPostsById("1");
        verify(repo, times(1)).save(user);
    }
}

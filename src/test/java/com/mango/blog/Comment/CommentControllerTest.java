package com.mango.blog.Comment;

import com.mango.blog.Post.GeneralPost;
import com.mango.blog.Repositiory.UserRepository;
import com.mango.blog.User.User;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;

;

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

    /*
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
    */
}

package com.mango.blog.Comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mango.blog.Post.GeneralPost;
import com.mango.blog.Repositiory.UserRepository;
import com.mango.blog.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.mango.blog.Authentication.JwtGenerator.decodeToken;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CommentController {

    @Autowired
    private UserRepository repo;

    public CommentController(){}

    public CommentController(UserRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/Comment")
    public ResponseEntity createComment(@RequestBody Map<String, String> body, @RequestHeader("Authorization") String token) throws JsonProcessingException
    {
        if (!body.containsKey("postID")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("PostID not found");
        }
        if (!body.containsKey("text")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Text not found");
        }
        String author = decodeToken(token.split(" ")[1]);
        if (author == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unauthorized");
        }
        
        User commentUser = repo.findByUserName(author);
        if (commentUser == null) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("User not found");
        }
        String post = repo.getPostsById(body.get("postID"));
        if (post == null) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Post not found");
        }
        //System.out.println("\n\n\n\n" + post + "\n\n\n\n");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        GeneralPost postObj = mapper.readValue(post, GeneralPost.class);
        Comment comment = new Comment(commentUser.getUserName(), body.get("text"));
        User postAuthor = repo.findByUserName(postObj.getAuthor());
        if(!postAuthor.addComment(postObj.getPostID(), comment)){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Comment not added to post");
        }
        repo.save(postAuthor);
        return ResponseEntity.status(HttpStatus.OK).body("Comment created");
    }


    @DeleteMapping("/Comment")
    public ResponseEntity deleteComment(@RequestBody Map<String, String> body, @RequestHeader("Authorization") String token) throws JsonProcessingException{
        if (!body.containsKey("postID")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("PostID not found");
        }
        String author = decodeToken(token.split(" ")[1]);
        if (author == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unauthorized");
        }
        if (!body.containsKey("commentID")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("CommentID not found");
        }

        User commentUser = repo.findByUserName(author);
        if (commentUser == null) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("User not found");
        }
        String post = repo.getPostsById(body.get("postID"));
        if (post == null) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Post not found");
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        
        GeneralPost postObj = mapper.readValue(post, GeneralPost.class);
        User postAuthor = repo.findByUserName(postObj.getAuthor());
        if(!postAuthor.removeComment(body.get("postID"), body.get("commentID"))){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Comment not deleted");
        }

        repo.save(postAuthor);
        return ResponseEntity.status(HttpStatus.OK).body("Comment deleted");
    }


}

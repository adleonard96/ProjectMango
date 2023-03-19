package com.mango.blog.Comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import jakarta.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mango.blog.Post.GeneralPost;
import com.mango.blog.Repositiory.UserRepository;
import com.mango.blog.User.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.*;

import java.time.LocalDateTime;

import com.mango.blog.Post.PostController;
import com.mango.blog.Post.GeneralPost;
import com.mango.blog.Post.Post;



@RestController
public class CommentController {

    @Autowired
    private UserRepository repo;

    public CommentController(){}

    public CommentController(UserRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/Comment")
    public ResponseEntity createComment(@RequestBody Map<String, String> body) throws JsonProcessingException 
    {
        if (!body.containsKey("postID")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("PostID not found");
        }
        if (!body.containsKey("text")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Text not found");
        }
        if (!body.containsKey("userName")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Username not found");
        }

        User commentUser = repo.findByUserName(body.get("userName"));
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
        Comment comment = new Comment(commentUser.getUserName(), body.get("text"));
        User postAuthor = repo.findByUserName(postObj.getAuthor());
        if(!postAuthor.addComment(postObj.getPostID(), comment)){
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Comment not added to post");
        }
        repo.save(postAuthor);
        return ResponseEntity.status(HttpStatus.OK).body("Comment created");
    }

    @DeleteMapping("/Comment")
    public ResponseEntity deleteComment(@RequestBody Map<String, String> body) throws JsonProcessingException{
        if (!body.containsKey("postID")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("PostID not found");
        }
        if (!body.containsKey("userName")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Username not found");
        }        if (!body.containsKey("postID")) {
        }
        if (!body.containsKey("commentID")) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("CommentID not found");
        }

        User commentUser = repo.findByUserName(body.get("userName"));
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

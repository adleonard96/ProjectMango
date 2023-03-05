package com.mango.blog.Post;

import com.mongodb.client.*;
import com.mongodb.ExplainVerbosity;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import jakarta.validation.Valid;
import org.bson.Document;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mango.blog.Repositiory.UserRepository;
import com.mango.blog.User.User;
import org.bson.json.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


@RestController
public class PostController {


    final
    UserRepository repo;

    public PostController(UserRepository repo) {
        this.repo = repo;

    }

    // TODO - Get the username from the current signed in user instead of the body
    @PostMapping("/GeneralPost")
    public ResponseEntity createGenericPostPost(@RequestBody GeneralPost post){
        // Get the user from the database
        User user = repo.findByUserName(post.getAuthor());
        user.createPost(post.getPostName(), post.getText(), post.getGenre(), post.getAuthor());
        // Save the user to the database
        repo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post Created");
    }

    @GetMapping("/GeneralPost")
    public String getGenericPost(){
        User user = repo.findByUserName("Test");
        System.out.println(user.getUserName());
        // Get the first post from the user
        Post post = user.getPosts().get(0);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try{
            String json = mapper.writeValueAsString(post);
            return json;
        }catch (Exception e){
            System.out.println(e);
            return "Error";
        }
    }

    @GetMapping("/Posts/PostById")
    public String PostById(@RequestParam String postID){

        return repo.getPostsById(postID);
    }
}

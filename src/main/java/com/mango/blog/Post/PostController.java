package com.mango.blog.Post;


import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import org.bson.Document;


import jakarta.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mango.blog.Repositiory.UserRepository;
import com.mango.blog.User.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;


@RestController
public class PostController {


    final
    UserRepository repo;

    public PostController(UserRepository repo) {
        this.repo = repo;

    }

    // TODO - Get the username from the current signed in user instead of the body
    @PostMapping("Posts/GeneralPost")
    public ResponseEntity createGenericPostPost(@Valid @RequestBody GeneralPost post){
        // Get the user from the database
        User user = repo.findByUserName(post.getAuthor());
        user.createPost(post.getPostName(), post.getText(), post.getGenre(), post.getAuthor());
        // Save the user to the database
        repo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post Created");
    }
    @DeleteMapping("/GeneralPost")
    public String deleteGenericPostPost (@RequestBody GeneralPost post){
        User user = repo.findByUserName(post.getAuthor()); //Finding the user
        user.deletePost(post.getPostID(),post.getPostName(), post.getText(), post.getGenre(), post.getAuthor()); //sending the post to the chopping block
        repo.save(user); //save new user
        return "Post Deleted";
    }

    @GetMapping("/Posts/GeneralPost")
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
    public String postById(@RequestParam String postID){
        MongoClient mongoClient = MongoClients.create("mongodb+srv://MangoAdmin:TdINg8HrP5HLNLJU@projectmango.34hfodq.mongodb.net/?retryWrites=true&w=majority");
        MongoDatabase database = mongoClient.getDatabase("MangoDB");
        MongoCollection<Document> collection = database.getCollection("BlogData");
        AggregateIterable<Document> posts;
        posts = collection.aggregate(
                Arrays.asList(
                        Aggregates.unwind("$posts"),
                        Aggregates.replaceRoot("$posts"),
                        Aggregates.match(Filters.eq("postID", postID))
                )
        );
        return posts.first().toJson();
    }
    
    public String PostById(@RequestParam String postID){

        return repo.getPostsById(postID);
    }

    @GetMapping("/Posts/Genres")
    public String getGenres(){
        return repo.getGenres();
    }

    @GetMapping("/Posts/ByGenre")
    public String PostsByGenre(@RequestParam String genre){
        return repo.getPostsByGenre(genre);
    }

    @PutMapping("/Posts/UpdateGeneralPost")
    public String updatePost(@RequestBody GeneralPost post){
        User user = repo.findByUserName(post.getAuthor());
        user.updatePost(post.getPostID(), post.getPostName(), post.getText(), post.getGenre(), post.getAuthor());
        repo.save(user);
        return "Post Updated";

    }

    @GetMapping("/Posts/AllPosts")
    public ArrayList<String> allPosts(){
        return repo.getAllPosts();
    }
}

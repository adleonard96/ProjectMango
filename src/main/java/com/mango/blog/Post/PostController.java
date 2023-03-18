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

    @PostMapping("Posts/GeneralPost")
    public ResponseEntity<String> createGenericPostPost(@Valid @RequestBody GeneralPost post){
        // Get the user from the database
        User user = repo.findByUserName(post.getAuthor());
        if (user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
        user.createPost(post.getPostName(), post.getText(), post.getGenre(), post.getAuthor());
        // Save the user to the database
        repo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post Created");
    }
    @DeleteMapping("/GeneralPost")
    public ResponseEntity<String> deleteGenericPostPost (@RequestBody GeneralPost post){
        User user = repo.findByUserName(post.getAuthor()); //Finding the user
        if (user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
        user.deletePost(post.getPostID(),post.getPostName(), post.getText(), post.getGenre(), post.getAuthor()); //sending the post to the chopping block
        repo.save(user); //save new user
        return ResponseEntity.status(HttpStatus.CREATED).body("Post Deleted");
    }

    @GetMapping("/Posts/GeneralPost")
    public ResponseEntity<String> getGenericPost(){
        User user = repo.findByUserName("Test");
        if (user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
        System.out.println(user.getUserName());
        // Get the first post from the user
        Post post = user.getPosts().get(0);
        if (post == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post not found");
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try{
            String json = mapper.writeValueAsString(post);
            return ResponseEntity.status(HttpStatus.OK).body(json);
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @GetMapping("/Posts/PostById")
    public ResponseEntity<String> postById(@RequestParam String postID){
        if (postID == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post not found");
        }
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
        String json = posts.first().toJson();
        return ResponseEntity.status(HttpStatus.OK).body(json);
    }
    
    public String PostById(@RequestParam String postID){
        return repo.getPostsById(postID);
    }

    @GetMapping("/Posts/Genres")
    public ResponseEntity<String> getGenres(){
        String body = repo.getGenres();
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @GetMapping("/Posts/ByGenre")
    public ResponseEntity<String> PostsByGenre(@RequestParam String genre){

        String body = repo.getPostsByGenre(genre);
        return ResponseEntity.status(HttpStatus.OK).body(body);

    }

    @PutMapping("/Posts/UpdateGeneralPost")
    public ResponseEntity<String> updatePost(@RequestBody GeneralPost post){
        if (post.getAuthor() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Author not found");
        }
        if (post.getPostID() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post not found");
        }
        if (post.getPostName() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post name not found");
        }
        if (post.getText() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post text not found");
        }
        if (post.getGenre() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post genre not found");
        }
        User user = repo.findByUserName(post.getAuthor());
        if (user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

        user.updatePost(post.getPostID(), post.getPostName(), post.getText(), post.getGenre(), post.getAuthor());
        repo.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("Post Updated");

    }

    @GetMapping("/Posts/AllPosts")
    public ResponseEntity<String> allPosts(){
        String response = repo.getAllPosts().toString();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

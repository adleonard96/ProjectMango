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

    final
     UserRepository repo;

    public CommentController(UserRepository userRepository) {
        this.repo = userRepository;
    }

    // Create a Comment and add it tothe post

    // @PostMapping("/Comment/Create")
    // public ResponseEntity CreateComment(@RequestParam String postID, @RequestParam String text) {
    //     // get user matching post
    //     // MongoClient mongoClient = MongoClients.create(
    //     //         "mongodb+srv://MangoAdmin:TdINg8HrP5HLNLJU@projectmango.34hfodq.mongodb.net/?retryWrites=true&w=majority");
    //     // MongoDatabase database = mongoClient.getDatabase("MangoDB");
    //     // MongoCollection<Document> collection = database.getCollection("BlogData");
    //     // AggregateIterable<Document> posts;
    //     // posts = collection.aggregate(
    //     //         Arrays.asList(
    //     //                 Aggregates.unwind("$posts"),
    //     //                 Aggregates.replaceRoot("$posts"),
    //     //                 Aggregates.match(Filters.eq("postID", postID))));
    //     // String userName = posts.first().get("author").toString(); // get username
    @PostMapping("/Comment")
    public ResponseEntity createComment(@RequestBody Map<String, String> body) throws JsonProcessingException {
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
    




    // //     User user = userRepository.findByUserName(userName); // get user of post
    // //     System.out.println("\n\n\n\n\n" + user.getUserName() + "\n\n\n" + text);

    // //     ArrayList<Post> userPosts = user.getPosts();

    // //     System.out.print(userPosts.get(0));

    // //     for (int i = 0; i < userPosts.size(); i++) {
    // //         if (userPosts.get(i).getPostID().equals(postID)) {
    // //             Comment comment = new Comment(user, text); // create the comment
    // //             // user.addComment(postID, comment);
    // //            userPosts.get(i).addComment(comment);
    // //         }
    // //     }
    // //   userRepository.save(user);

    //     return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Comment not created.");

    // }

}

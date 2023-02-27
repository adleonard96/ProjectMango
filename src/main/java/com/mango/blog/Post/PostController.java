package com.mango.blog.Post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mango.blog.Repositiory.UserRepository;
import com.mango.blog.User.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// object mapper to convert json to java object
// import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class PostController {
    final
    UserRepository repo;

    public PostController(UserRepository repo) {
        this.repo = repo;
    }

    // TODO - Get the username from the current signed in user instead of the body
    @PostMapping("/GeneralPost")
    public String createGenericPostPost(@RequestBody GeneralPost post){
        // Get the user from the database
        User user = repo.findByUserName(post.getAuthor());
        user.createPost(post.getPostName(), post.getText(), post.getGenre(), post.getAuthor());
        // Save the user to the database
        repo.save(user);
        return "Post Created";
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
}

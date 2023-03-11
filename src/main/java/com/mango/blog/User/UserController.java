package com.mango.blog.User;

import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.validation.Valid;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mango.blog.Repositiory.UserRepository;
import com.mango.blog.User.User;
import org.bson.json.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@RestController
public class UserController {

    final UserRepository repo;

    public UserController(UserRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/User/FavoritePost")
    public ResponseEntity addFavoritePost(@Valid @RequestBody Map<String, String> body){
        if (!body.containsKey("username")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing username");
        }
        if (!body.containsKey("postID")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing postID");
        }
        if (!body.containsKey("postName")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing postName");
        }
        String username = body.get("username");
        String postID = body.get("postID");
        String postName = body.get("postName");
        User user = repo.findByUserName(username);
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if(user.isFavorite(postID)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post already favorited");
        }
        user.addFavoritePost(postID, postName);
        repo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post Added");
    }

    @DeleteMapping("/User/RemoveFavoritePost")
    public ResponseEntity removeFavoritePost(@Valid @RequestBody Map<String, String> body){
        if (!body.containsKey("username")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required");
        }
        if (!body.containsKey("postID")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("PostID is required");
        }
        String username = body.get("username");
        String postID = body.get("postID");
        User user = repo.findByUserName(username);
        if(!user.unfavoritePost(postID)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }
        repo.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("Post Removed");
    }

    @GetMapping("/User/FavoritePosts")
    public String getFavoritePosts(@Valid @RequestParam String username){
        User user = repo.findByUserName(username);
        if (user == null){
            return "User not found";
        }
        ArrayList<JsonObject> json = new ArrayList<>();
        try{
            ArrayList<HashMap<String, String>> posts = user.getFavoritePosts();
            for (HashMap<String, String> post : posts) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                String jsonPost = mapper.writeValueAsString(post);
                json.add(new JsonObject(jsonPost));
            }
            return json.toString();
        }catch (Exception e){
            System.out.println(e);
            return "Error";
        }
    }

}

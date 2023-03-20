package com.mango.blog.User;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
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
    public ResponseEntity<String> addFavoritePost(@Valid @RequestBody Map<String, String> body) {
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
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if (user.isFavorite(postID)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post already favorited");
        }
        user.addFavoritePost(postID, postName);
        repo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post Added");
    }

    @DeleteMapping("/User/RemoveFavoritePost")
    public ResponseEntity<String> removeFavoritePost(@Valid @RequestBody Map<String, String> body) {
        if (!body.containsKey("username")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required");
        }
        if (!body.containsKey("postID")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("PostID is required");
        }
        String username = body.get("username");
        String postID = body.get("postID");
        User user = repo.findByUserName(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if (!user.unfavoritePost(postID)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }
        repo.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("Post Removed");
    }

    @GetMapping("/User/FavoritePosts")
    public String getFavoritePosts(@Valid @RequestParam String username) {
        User user = repo.findByUserName(username);
        if (user == null) {
            return "User not found";
        }
        ArrayList<JsonObject> json = new ArrayList<>();
        try {
            ArrayList<HashMap<String, String>> posts = user.getFavoritePosts();
            for (HashMap<String, String> post : posts) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                String jsonPost = mapper.writeValueAsString(post);
                json.add(new JsonObject(jsonPost));
            }
            return json.toString();
        } catch (Exception e) {
            System.out.println(e);
            return "Error";
        }
    }

    @GetMapping("/User/Groups")
    public ResponseEntity<String> getGroups(@Valid @RequestParam String username) {
        if (username == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required");
        }
        User user = repo.findByUserName(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        try {
            HashMap<String, ArrayList<HashMap<String, String>>> groups = user.getUserGroups();
            ArrayList<String> groupNames = new ArrayList<>();
            for (String keys : groups.keySet()) {
                groupNames.add(keys);
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String json = mapper.writeValueAsString(groupNames);
            return ResponseEntity.status(HttpStatus.OK).body(json);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @PutMapping("/User/RemoveUserFromGroup")
    public ResponseEntity<String> removeUserFromGroup(@Valid @RequestBody Map<String, String> body) {
        if (!body.containsKey("username")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required");
        }
        if (!body.containsKey("groupName")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group name is required");
        }
        if (!body.containsKey("userToRemove")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User to remove is required");
        }
        String username = body.get("username");
        String groupName = body.get("groupName");
        String userToRemove = body.get("userToRemove");
        User user = repo.findByUserName(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if (!user.removeUserFromGroup(groupName, userToRemove)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found or user not in group");
        }
        repo.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("User removed from group");
    }

    @DeleteMapping("/User/RemoveGroup")
    public ResponseEntity<String> removeGroup(@Valid @RequestBody Map<String, String> body) {
        if (!body.containsKey("username")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required");
        }
        if (!body.containsKey("groupName")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group name is required");
        }
        String username = body.get("username");
        String groupName = body.get("groupName");
        User user = repo.findByUserName(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if (!user.removeGroup(groupName)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
        }
        repo.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("Group removed");
    }

    @GetMapping("/User")
    public ResponseEntity<String> getUserDetails(@RequestParam String username) {
        if (username == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required");
        }
        User user = repo.findByUserName(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            String json = mapper.writeValueAsString(user);
            return ResponseEntity.status(HttpStatus.OK).body(json);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/User/Group")
    public ResponseEntity<String> createGroup(@RequestBody Map<String, String> body) {
        if (!body.containsKey("username")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required");
        }
        if (!body.containsKey("groupName")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group name is required");
        }
        String username = body.get("username");
        String groupName = body.get("groupName");
        User user = repo.findByUserName(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (!user.addGroup(groupName)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group already exists");
        }
        repo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Group created");
    }

    @PutMapping("/User/Group")
    public ResponseEntity<String> addUserToGroup(@RequestBody Map<String, String> body) {
        if (!body.containsKey("username")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required");
        }
        if (!body.containsKey("groupName")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group name is required");
        }
        if (!body.containsKey("userToAdd")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User to add is required");
        }
        String username = body.get("username");
        String groupName = body.get("groupName");
        String userToAdd = body.get("userToAdd");
        User user = repo.findByUserName(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User userToAddObj = repo.findByUserName(userToAdd);
        if (userToAddObj == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User to add not found");
        }
        if (!user.addUserToGroup(groupName, userToAddObj.getUserID() ,userToAdd)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
        }
        repo.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("User added to group");
    }

    @GetMapping("/User/Group")
    public ResponseEntity<String> getUsersInGroup(@RequestParam String username, @RequestParam String groupName) {
        if (username == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is required");
        }
        if (groupName == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Group name is required");
        }
        User user = repo.findByUserName(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        try {
            ArrayList<HashMap<String, String>> users = user.getUsersInGroup(groupName);
            if (users == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found");
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            String json = mapper.writeValueAsString(users);
            return ResponseEntity.status(HttpStatus.OK).body(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
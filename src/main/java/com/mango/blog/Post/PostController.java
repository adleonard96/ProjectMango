package com.mango.blog.Post;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mango.blog.Repositiory.UserRepository;
import com.mango.blog.User.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

import static com.mango.blog.Authentication.JwtGenerator.decodeToken;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {


    final
    UserRepository repo;

    public PostController(UserRepository repo) {
        this.repo = repo;

    }
    @PostMapping("Posts/MultiMediaPost")
    public ResponseEntity<String> createMultiMediaPost(@RequestBody HashMap<String, String> body, @RequestHeader("Authorization") String token){
        String author = decodeToken(token.split(" ")[1]);
        if (author == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Author is null");
        }
        if (!body.containsKey("postName")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post Name is null");
        }
        if (!body.containsKey("text")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Text is null");
        }
        if (!body.containsKey("genre")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Genre is null");
        }
        if (!body.containsKey("media")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Media is null");
        }
        if (!body.containsKey("fileExtension")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File Extension is null");
        }
        User user = repo.findByUserName(author);
        if (user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
        user.createPost(body.get("postName"), body.get("text"), author, body.get("genre"), body.get("media"), body.get("fileExtension"));
        repo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post created");
    }
    @PostMapping("Posts/GeneralPost")
    public ResponseEntity<String> createGenericPostPost(@Valid @RequestBody GeneralPost post, @RequestHeader("Authorization") String token){
        // Get the user from the database
        //if (post.getAuthor() == null){
        //    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Author is null");
        //}
        String author = decodeToken(token.split(" ")[1]);
        if (author == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Author is null");
        } else {
            post.setAuthor(author);
        }

        if (post.getPostName() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post Name is null");
        }
        if (post.getText() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Text is null");
        }
        if (post.getGenre() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Genre is null");
        }
        User user = repo.findByUserName(post.getAuthor());

        if (user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
        user.createPost(post.getPostName(), post.getText(), post.getAuthor(), post.getGenre());
        // Save the user to the database
        repo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post Created");
    }
    @DeleteMapping("/GeneralPost")
    public ResponseEntity<String> deleteGenericPostPost (@RequestBody HashMap<String, String> postID, @RequestHeader("Authorization") String token){
        String author = decodeToken(token.split(" ")[1]);
        Post post;
        if (author == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Author is null");
        }
        User user = repo.findByUserName(author); //Finding the user
        if (user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }
        if (!postID.containsKey("postID")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post ID is null");
        }
        post = user.getPost(postID.get("postID"));
        //post = (GeneralPost) user.getPost(post.getPostID());
        if (!user.deletePost(post.getPostID(),post.getPostName(), post.getText(), post.getGenre(), post.getAuthor())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post not found");
        }  //sending the post to the chopping block
        ArrayList<String> userWhoFavorited = repo.getUsersWithWhoFavoritedAPost(post.getPostID(), post.getPostName());
        for (String userWhoFavoritedPost : userWhoFavorited){
            User userWhoFavoritedPostUser = repo.findByUserName(userWhoFavoritedPost);
            userWhoFavoritedPostUser.unfavoritePost(post.getPostID());
            repo.save(userWhoFavoritedPostUser);
        }
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
        String json = repo.getPostsById(postID);
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
    public ResponseEntity<String> updatePost(@RequestBody GeneralPost post, @RequestHeader("Authorization") String token){
        String author = decodeToken(token.split(" ")[1]);
        if (author == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Author is null");
        } else {
            post.setAuthor(author);
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
    @PutMapping("/Posts/MultiMediaPost")
    public ResponseEntity<String> updateMultiMediaPost(@RequestBody MultiMediaPost post, @RequestHeader("Authorization") String token){
        String author = decodeToken(token.split(" ")[1]);
        if (author == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Author is null");
        } else {
            post.setAuthor(author);
        }
        if (post.getPostID() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("PostID not found");
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
        if (post.getMedia() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post media not found");
        }
        User user = repo.findByUserName(post.getAuthor());
        if (user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found");
        }

        if (!user.updatePost(post.getPostID(), post.getPostName(), post.getText(), post.getGenre(), post.getAuthor(), post.getMedia(), post.getFileExtension())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Post not found");
        }
        repo.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("Post Updated");

    }

    @GetMapping("/Posts/AllPosts")
    public ResponseEntity<String> allPosts(){
        String response = repo.getAllPosts().toString();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/Posts/AllPostsByUser")
    public ResponseEntity<String> allPostsByUser(@RequestParam String userName){
        String response = repo.getAllPostsByUser(userName);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

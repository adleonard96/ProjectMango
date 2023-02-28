package com.mango.blog.User;

import com.mango.blog.Post.Post;
import com.mango.blog.Post.PostFactory;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Document(collection = "BlogData")
public class User {
    @Id
    private String userID;
    private String userName;
    private String userPassword;
    private String email;
    private ArrayList<Post> posts = new ArrayList<Post>();
    private HashMap<String, ArrayList<User>> userGroups;
    private PostFactory postFactory;

    public User(String userName, String userPassword, String email) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.email = email;
        userGroups = new HashMap<String, ArrayList<User>>();
        postFactory = new PostFactory();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }
    
    public Post getPost(String postID) {
        Post post = posts.get(postID);
        return post;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public HashMap<String, ArrayList<User>> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(HashMap<String, ArrayList<User>> userGroups) {
        this.userGroups = userGroups;
    }

    @BsonIgnore
    public void setPostFactory(PostFactory postFactory) {
        this.postFactory = postFactory;
    }

    public void createPost(String postName, String text, String author, String genre) {
        Post post = postFactory.createPost(postName, text, author ,genre);
        posts.add(post);
    }

    public void deletePost(String postID){
        Post post= getPost(postID); //retrieve the post object referenced by the ID  
        posts.remove(post); //Need the post object 
    }
}

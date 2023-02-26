package com.mango.blog.User;

import com.mango.blog.Post.Post;
import com.mango.blog.Post.PostFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Document(collection = "BlogUsers")
public class User {
    @Id
    private UUID userID;
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
}

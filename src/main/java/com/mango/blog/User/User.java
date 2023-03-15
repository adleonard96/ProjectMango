package com.mango.blog.User;

import com.mango.blog.Post.Post;
import com.mango.blog.Post.PostFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Data
@AllArgsConstructor
@Document(collection = "BlogData")
public class User {
    @Id
    private String userID;
    private String userName;
    private String userPassword;
    private String email;
    private ArrayList<Post> posts = new ArrayList<Post>();
    private HashMap<String, ArrayList<HashMap<String,String>>> userGroups;
    private ArrayList<HashMap<String, String>> favoritePosts = new ArrayList<>();
    @Transient
    private PostFactory postFactory;

    public User(){}

    public User(String userName, String userPassword, String email) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.email = email;
        this.userGroups = new HashMap<>();
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

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public HashMap<String, ArrayList<HashMap<String, String>>> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(HashMap<String, ArrayList<HashMap<String, String>>> userGroups) {
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


    public void deletePost(String postID, String postName, String text, String genre, String author){
        for(Post post: this.posts){
            if(post.getPostID() == postID){
                posts.remove(post);
            }
        }
    }

    public void updatePost(String postID, String postName, String text, String genre, String author) {
        for (Post post : posts) {
            if (post.getPostID().equals(postID)) {
                post.setPostName(postName);
                post.setText(text);
                post.setGenre(genre);
                post.setAuthor(author);
                post.setEditedOn(LocalDateTime.now());
                break;
            }
        }
    }

    public void addFavoritePost(String postID, String postName) {
        HashMap<String, String> favoritePost = new HashMap<>();
        favoritePost.put(postID, postName);
        favoritePosts.add(favoritePost);
    }

    public boolean unfavoritePost(String postID) {
        for (HashMap<String, String> favoritePost : favoritePosts) {
            if (favoritePost.containsKey(postID)) {
                favoritePosts.remove(favoritePost);
                return true;
            }
        }
        return false;
    }

    public ArrayList<HashMap<String, String>> getFavoritePosts() {
        return  this.favoritePosts;
    }

    public boolean isFavorite(String postID) {
        for (HashMap<String, String> favoritePost : favoritePosts) {
            if (favoritePost.containsKey(postID)) {
                return true;
            }
        }
        return false;
    }

    public boolean removeUserFromGroup(String groupName, String userToRemove) {
        if (userGroups.containsKey(groupName)) {
            ArrayList<HashMap<String, String>> users = userGroups.get(groupName);
            for (HashMap<String, String> user : users) {
                if (user.containsValue(userToRemove)) {
                    users.remove(user);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeGroup(String groupName) {
        if (userGroups.containsKey(groupName)) {
            userGroups.remove(groupName);
            return true;
        }
        return false;
    }
}

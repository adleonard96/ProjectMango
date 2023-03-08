package com.mango.blog.Post;

import com.mango.blog.Comment.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class MultiMediaPost implements Post{
    public String postID = null;
    public ArrayList<Comment> comments;
    public String author = null;
    public String postName = null;
    public String postBody = null;
    public LocalDateTime createdOn = LocalDateTime.now();
    public LocalDateTime editedOn = LocalDateTime.now();
    public String text = null;
    public String genre = null;

    public MultiMediaPost(String postName, String text, String author, String genre, String media) {
        comments = new ArrayList<>();
    }

    @Override
    public void createPost() {
        // TODO Auto-generated method stub

    }

    @Override
    public String getPostID() {
        return postID.toString();
    }

    @Override
    public LocalDateTime getCreatedOn() {
        return createdOn;
    }
}

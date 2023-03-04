package com.mango.blog.Post;

import com.mango.blog.Comment.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;


public interface Post {
    public UUID postID = null;
    public ArrayList<Comment> comments = new ArrayList<>();
    public String author = null;
    public String postName = null;
    public String postBody = null;
    public LocalDateTime createdOn = LocalDateTime.now();
    public LocalDateTime editedOn = LocalDateTime.now();
    public String text = null;
    public String genre = null;

    public void createPost();
}

package com.mango.blog.Post;

import com.mango.blog.Comment.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;


public interface Post {
    public ArrayList<Comment> comments = new ArrayList<>();
    public LocalDateTime createdOn = LocalDateTime.now();
    public LocalDateTime editedOn = LocalDateTime.now();

    public void createPost();

    public String getPostID();
    public LocalDateTime getCreatedOn();
}

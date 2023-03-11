package com.mango.blog.Post;

import com.mango.blog.Comment.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;



public interface Post {
    public ArrayList<Comment> comments = new ArrayList<>();
    public LocalDateTime createdOn = LocalDateTime.now();
    public LocalDateTime editedOn = LocalDateTime.now();


    public void createPost();
    public void deletePost();
    public String getPostID();

    public String getPostID();
    public LocalDateTime getCreatedOn();

    void setPostName(String postName);

    void setText(String text);

    void setGenre(String genre);

    void setAuthor(String author);

    void setEditedOn(LocalDateTime now);

    String getPostName();

    String getText();
    String getGenre();
    String getAuthor();
}

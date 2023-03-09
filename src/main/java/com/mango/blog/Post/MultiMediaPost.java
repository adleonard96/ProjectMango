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
    public String getPostID() {
        return postID.toString();
    }

    @Override
    public LocalDateTime getCreatedOn() {
        return createdOn;
    }
    @Override
    public void setPostName(String postName) {
        this.postName = postName;
    }
    @Override
    public void setText(String text) {
        this.text = text;
    }
    @Override
    public void setGenre(String genre) {
        this.genre = genre;
    }
    @Override
    public void setAuthor(String author) {
        this.author = author;
    }
    @Override
    public void setEditedOn(LocalDateTime now) {
        this.editedOn = now;
    }

    @Override
    public String getPostName() {
        return postName;
    }
    @Override
    public String getText() {
        return text;
    }
    @Override
    public String getGenre() {
        return genre;
    }
    @Override
    public String getAuthor() {
        return author;
    }

}

package com.mango.blog.Post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mango.blog.Comment.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class GeneralPost implements Post{
    public UUID postID = UUID.randomUUID();
    public ArrayList<Comment> comments;
    public String author;
    public String postName;
    public String postBody;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime createdOn = LocalDateTime.now();
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime editedOn = LocalDateTime.now();
    public String text;
    public String genre;

    public GeneralPost(String postName, String text, String author, String genre) {
        this.postName = postName;
        this.text = text;
        this.author = author;
        this.genre = genre;
        comments = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "{" +
                "postID:" + postID +
                ", comments:" + comments +
                ", author:'" + author + '\'' +
                ", postName:'" + postName + '\'' +
                ", postBody:'" + postBody + '\'' +
                ", createdOn:" + createdOn +
                ", editedOn:" + editedOn +
                ", text:'" + text + '\'' +
                ", genre:'" + genre + '\'' +
                '}';
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public void setEditedOn(LocalDateTime editedOn) {
        this.editedOn = editedOn;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public UUID getPostID() {
        return postID;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public String getAuthor() {
        return author;
    }

    public String getPostName() {
        return postName;
    }

    public String getPostBody() {
        return postBody;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public LocalDateTime getEditedOn() {
        return editedOn;
    }

    public String getText() {
        return text;
    }

    public String getGenre() {
        return genre;
    }

    @Override
    public void createPost() {
        // TODO Auto-generated method stub

    }

}

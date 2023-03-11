package com.mango.blog.Post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mango.blog.Comment.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Data
@AllArgsConstructor
public class GeneralPost implements Post{
    public String postID = UUID.randomUUID().toString();
    public ArrayList<Comment> comments;
    public String author;
    @NotBlank(message = "postName is required")
    public String postName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime createdOn = LocalDateTime.now();
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime editedOn = LocalDateTime.now();
    @NotBlank(message = "text is required")
    public String text;
    @NotBlank(message = "genre is required")
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
                ", createdOn:" + createdOn +
                ", editedOn:" + editedOn +
                ", text:'" + text + '\'' +
                ", genre:'" + genre + '\'' +
                '}';
    }

    @Override
    public void setAuthor(String author) {
        this.author = author;
    }
    @Override
    public void setPostName(String postName) {
        this.postName = postName;
    }


    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
    @Override
    public void setEditedOn(LocalDateTime editedOn) {
        this.editedOn = editedOn;
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
    public String getPostID() {
        return postID;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }
    @Override
    public String getAuthor() {
        return author;
    }
    @Override
    public String getPostName() {
        return postName;
    }

    @Override
    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public LocalDateTime getEditedOn() {
        return editedOn;
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
    public void deletePost(){

    }

}

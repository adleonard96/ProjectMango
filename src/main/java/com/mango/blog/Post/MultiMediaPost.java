package com.mango.blog.Post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mango.blog.Comment.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.PersistenceCreator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true, value = {"comments", "createdOn", "editedOn"})
public class MultiMediaPost implements Post{
    public String postID = UUID.randomUUID().toString();
    public ArrayList<Comment> comments;
    public String author = null;
    public String postName = null;
    public LocalDateTime createdOn = LocalDateTime.now();
    public LocalDateTime editedOn = LocalDateTime.now();
    public String text = null;
    public String genre = null;
    public String media = null;

    @PersistenceCreator
    public MultiMediaPost(String postName, String text, String author, String genre, String media) {
        this.postName = postName;
        this.text = text;
        this.author = author;
        this.genre = genre;
        this.media = media;
        comments = new ArrayList<>();
    }

    public String getMedia() {
        return media;
    }
    public void setMedia(String media) {
        this.media = media;
    }

    @Override
    public String getPostID() {
        return postID.toString();
    }

    @Override
    public ArrayList<Comment> getComments() {
        return comments;
    }

    
    public void addComment(Comment comment){
        this.comments.add(comment);
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
    public void deletePost(){

    }

    @Override
    public String getGenre() {
        return genre;
    }
    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public String toJsonString() {
        return null;
    }

    @Override
    public ArrayList<String> getCommentsID() {
        ArrayList<String> commentsID = new ArrayList<>();
        for (Comment comment : comments) {
            commentsID.add(comment.getCommentID());
        }
        return commentsID;
    }
}

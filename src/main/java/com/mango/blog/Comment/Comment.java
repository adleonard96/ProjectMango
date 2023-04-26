package com.mango.blog.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Comment {
    private String commentAuthor;
    private String commentID;
    private LocalDateTime createdOn;
    private LocalDateTime editedOn;
    private String text;
    private ArrayList<Comment> replies = new ArrayList<Comment>();

    public Comment(String commentAuthor, String text){
        this.commentAuthor = commentAuthor;
        this.commentID = UUID.randomUUID().toString();
        this.createdOn = LocalDateTime.now();
        this.editedOn = null;
        this.text = text;
        this.replies = new ArrayList<Comment>();
    }

    // public Comment(UserRepository repo){
    //     this.userRepository = repo;
    // }

    public Comment(){}

    public String getUser() {
        return commentAuthor;
    }

    public void setUser(String user) {
        this.commentAuthor = user;
    }

    public String getCommentID() {
        return commentID;
    }
    public String commentJson(){
        return "{" +
                "\"commentAuthor\":\"" + commentAuthor + '\"' +
                ", \"commentID\":\"" + commentID + '\"' +
                ", \"createdOn\":\"" + createdOn + '\"' +
                ", \"editedOn\":\"" + editedOn + '\"' +
                ", \"text\":\"" + text + '\"' +
                ", \"replies\":" + replies +
                '}';
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getEditedOn() {
        return editedOn;
    }

    public void setEditedOn(LocalDateTime editedOn) {
        this.editedOn = editedOn;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

package com.mango.blog.Post;

import com.mango.blog.Comment.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class GeneralPost implements Post{
    public UUID postID = null;
    public ArrayList<Comment> comments;
    public String author = null;
    public String postName = null;
    public String postBody = null;
    public LocalDateTime createdOn = LocalDateTime.now();
    public LocalDateTime editedOn = LocalDateTime.now();
    public String text = null;
    public String genre = null;

    public GeneralPost(String postName, String text, String author, String genre) {
        comments = new ArrayList<>();
    }

    @Override
    public void createPost() {
        // TODO Auto-generated method stub

    }

}

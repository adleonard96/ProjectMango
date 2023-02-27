package com.mango.blog.Comment;

import com.mango.blog.User.User;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.ArrayList;

public class Comment {
    private User user;
    private UUID commentID;
    private LocalDateTime createdOn;
    private LocalDateTime editedOn;
    private String text;
    private ArrayList<Comment> replies = new ArrayList<Comment>();

}

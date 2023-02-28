package com.mango.blog.Post;

public class PostFactory {
    public Post createPost(String postName, String text, String author, String genre){
            return new GeneralPost(postName, text, author, genre);
    }

    public Post createPost(String postName, String text, String author, String genre, String media){
        return new MultiMediaPost(postName, text, author, genre, media);
    }

}

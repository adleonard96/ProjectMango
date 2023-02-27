package com.mango.blog.DiscoveryFeed;

import com.mango.blog.Post.Post;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DiscoveryFeed {
    private static DiscoveryFeed instance = null;
    private DiscoveryFeed() {
    }
    private ArrayList<Post> posts = new ArrayList<Post>();
    private ArrayList<String> genres = new ArrayList<String>();

    public static DiscoveryFeed getInstance() {
        if (instance == null) {
            instance = new DiscoveryFeed();
        }
        return instance;
    }



}

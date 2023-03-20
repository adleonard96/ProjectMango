package com.mango.blog.Repositiory;

import com.mango.blog.User.User;
import com.mongodb.client.*;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;

import java.util.Arrays;
import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import org.bson.Document;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


import java.util.ArrayList;
import java.util.Arrays;

import static com.mongodb.client.model.Sorts.descending;

public interface UserRepository extends MongoRepository<User, String>{
    @Query(value = "{ 'userName' : ?0 }")
    User findByUserName(String userName);


    @Aggregation(pipeline = {"{'$unwind': {'path': '$posts' }}"})
    String getPosts();


    public default ArrayList<String> getAllPosts(){
        MongoClient mongoClient = MongoClients.create("mongodb+srv://MangoAdmin:TdINg8HrP5HLNLJU@projectmango.34hfodq.mongodb.net/?retryWrites=true&w=majority");
        MongoDatabase database = mongoClient.getDatabase("MangoDB");
        MongoCollection<Document> collection = database.getCollection("BlogData");

        AggregateIterable<Document> posts;
        posts = collection.aggregate(
            Arrays.asList(
                Aggregates.unwind("$posts"),
                Aggregates.replaceRoot("$posts"),
                Aggregates.sort(descending("createdOn")),
                Aggregates.project(Projections.fields(
                        Projections.include("postID","postName","author","createdOn"),
                         Projections.excludeId()
                ))
            )
        );
        ArrayList<String> json = new ArrayList<>();
        for (Document post : posts) {
            json.add(post.toJson());
        }
        return json;

    }

    public default String getPostsById(String postID){
        MongoClient mongoClient = MongoClients.create("mongodb+srv://MangoAdmin:TdINg8HrP5HLNLJU@projectmango.34hfodq.mongodb.net/?retryWrites=true&w=majority");
        MongoDatabase database = mongoClient.getDatabase("MangoDB");
        MongoCollection<Document> collection = database.getCollection("BlogData");
        AggregateIterable<Document> posts;
        posts = collection.aggregate(
                Arrays.asList(
                        Aggregates.unwind("$posts"),
                        Aggregates.replaceRoot("$posts"),
                        Aggregates.match(Filters.eq("postID", postID))
                )
        );

        return posts.first().toJson();
    }

    public default String getGenres(){
        MongoClient mongoClient = MongoClients.create("mongodb+srv://MangoAdmin:TdINg8HrP5HLNLJU@projectmango.34hfodq.mongodb.net/?retryWrites=true&w=majority");
        MongoDatabase database = mongoClient.getDatabase("MangoDB");
        MongoCollection<Document> collection = database.getCollection("BlogData");
        AggregateIterable<Document> posts;
        posts = collection.aggregate(
                Arrays.asList(
                        Aggregates.unwind("$posts"),
                        Aggregates.replaceRoot("$posts"),
                        Aggregates.project(Projections.fields(
                                Projections.include("genre"),
                                Projections.excludeId()
                        )),
                        Aggregates.group("$genre", Accumulators.sum("count", 1))
        ));
        ArrayList<String> json = new ArrayList<>();
        for(Document post : posts){
            json.add(post.toJson());
        }
        return json.toString();
    }

    public default String getPostsByGenre(String genre){
        MongoClient mongoClient = MongoClients.create("mongodb+srv://MangoAdmin:TdINg8HrP5HLNLJU@projectmango.34hfodq.mongodb.net/?retryWrites=true&w=majority");
        MongoDatabase database = mongoClient.getDatabase("MangoDB");
        MongoCollection<Document> collection = database.getCollection("BlogData");
        AggregateIterable<Document> posts;
        posts = collection.aggregate(
                Arrays.asList(
                        Aggregates.unwind("$posts"),
                        Aggregates.replaceRoot("$posts"),
                        Aggregates.match(Filters.eq("genre", genre)),
                        Aggregates.sort(descending("createdOn")),
                        Aggregates.project(Projections.fields(
                                Projections.include("postID","postName","author","createdOn"),
                                Projections.excludeId()
                        ))
                )
        );
        ArrayList<String> json = new ArrayList<>();
        for(Document post : posts){
            json.add(post.toJson());
        }
        return json.toString();
    };

    public default String getAllPostsByUser(String userName){
        MongoClient mongoClient = MongoClients.create("mongodb+srv://MangoAdmin:TdINg8HrP5HLNLJU@projectmango.34hfodq.mongodb.net/?retryWrites=true&w=majority");
        MongoDatabase database = mongoClient.getDatabase("MangoDB");
        MongoCollection<Document> collection = database.getCollection("BlogData");
        AggregateIterable<Document> posts;
        posts = collection.aggregate(
                Arrays.asList(
                        Aggregates.unwind("$posts"),
                        Aggregates.replaceRoot("$posts"),
                        Aggregates.match(Filters.eq("author", userName))
                )
        );
        ArrayList<String> json = new ArrayList<>();
        for(Document post : posts){
            json.add(post.toJson());
        }
        return json.toString();
    }
}

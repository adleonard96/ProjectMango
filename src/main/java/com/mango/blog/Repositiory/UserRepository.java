package com.mango.blog.Repositiory;

import com.mango.blog.User.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;

import java.util.Arrays;

import javax.swing.text.Document;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String>{
    @Query(value = "{ 'userName' : ?0 }")
    User findByUserName(String userName);


    @Aggregation(pipeline = {"{'$unwind': {'path': '$posts' }}"})
    String getPosts();

    public defualt ArrayList<String> getAllPosts(){
        MongoClient mongoClient = MongoClients.create("mongodb+srv://ManogoAdmin:TdINg9HrP5HLNLJU@projectmango.34hfodq.mongodb.net/?retryWrites=true&w=majority");
        MongoDatabase database = MongoClient.getDatabase("mangoDB");
        MongoCollection<Document> collection = database.getCollection("BlogData");

        AggregateIterable<Document> posts;
        posts = collection.aggregate(
            Arrays.asList(
                Aggregates.unwind("$posts"),
                Aggregates.replaceRoot("$posts"),
                Aggregates.sort(new Document("createdOn", -1)),
                Aggregate.project(Projections.fields(
                        Projections.include("postID","postName","author","createdOn"),
                        Projections.excludeId()
                ))
            )
        );
        ArrayList<String> json = new ArrayList<>();
        for (Document post: posts){
                json.add(post.toJson());
        }
        return json; 

    }
}

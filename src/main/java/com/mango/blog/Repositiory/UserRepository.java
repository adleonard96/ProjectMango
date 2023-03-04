package com.mango.blog.Repositiory;

import com.mango.blog.User.User;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String>{
    @Query(value = "{ 'userName' : ?0 }")
    User findByUserName(String userName);


    @Aggregation(pipeline = {"{'$unwind': {'path': '$posts' }}"})
    String getPosts();
}

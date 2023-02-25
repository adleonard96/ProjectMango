package com.mango.blog.Repositiory;

import com.mango.blog.User.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String>{
}

package com.mango.blog.Repositiory;
import com.mango.blog.Authentication.Register;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterRepository extends MongoRepository<Register, String>{
    
}

package com.mango.blog.Authentication;

import com.mango.blog.User.User;

import java.util.Map;

public interface JwtGeneratorInterface {
    Map<String, String> generatetoken (User user);
}

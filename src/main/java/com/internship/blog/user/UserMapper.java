package com.internship.blog.user;

import com.internship.blog.post.Post;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserMapper {

    public User toUser(UserDto userDto) {
        // create the user
        User user = new User();

        // set the fields of user
        user.setEmail(userDto.email());
        user.setPassword(userDto.password());
        user.setFullname(userDto.fullname());
        user.setPhotoUrl(userDto.photoUrl());
        user.setPosts(new ArrayList<Post>());

        return user;
    }
}

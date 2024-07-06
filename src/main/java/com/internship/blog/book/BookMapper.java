package com.internship.blog.book;

import com.internship.blog.post.Post;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BookMapper {

    public Book toBook(BookDto bookDto) {
        // create the book
        Book book = new Book();

        // set the fields of book
        book.setTitle(bookDto.title());
        book.setAuthorName(bookDto.authorName());
        book.setBookType(bookDto.bookType());
        book.setPhotoUrl(bookDto.photoUrl());
        book.setPosts(new ArrayList<Post>());

        return book;
    }
}

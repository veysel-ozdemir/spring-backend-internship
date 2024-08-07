package com.internship.blog.service;

import com.internship.blog.dto.PostDto;
import com.internship.blog.dto.PostEditDto;
import com.internship.blog.dto.PostResponseDto;
import com.internship.blog.enums.BookType;
import com.internship.blog.model.Book;
import com.internship.blog.model.Post;
import com.internship.blog.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class PostMapper {

    private final UserService userService;
    private final BookService bookService;

    public Post toPost(PostDto postDto) {
        // create the post
        Post post = new Post();

        // configure the user
        User user = userService.getUserById(postDto.userId());
        user.getPosts().add(post);

        // configure the book
        Book book = bookService.getBookByTitleAndAuthorName(
                postDto.bookTitle(),
                postDto.authorName()
        );
        if (book == null) {
            book = new Book();
            book.setTitle(postDto.bookTitle());
            book.setAuthorName(postDto.authorName());
            book.setBookType(BookType.fromString(postDto.bookType()));
            book.setPhotoUrl(postDto.bookPhotoUrl());
            book.setPosts(new ArrayList<Post>());
            // save the new created book to persist data
            bookService.saveBook(book);
        }
        book.getPosts().add(post);

        // set the fields of post
        post.setCommitDate(LocalDate.now());
        post.setReview(postDto.bookReview());
        post.setUser(user);
        post.setBook(book);

        return post;
    }

    public Post toUpdatedPost(PostEditDto postEditDto, Post presentPost) {
        // configure the book
        Book book = bookService.getBookByTitleAndAuthorName(
                postEditDto.bookTitle(),
                postEditDto.authorName()
        );
        if (book == null) {
            book = new Book();
            book.setTitle(postEditDto.bookTitle());
            book.setAuthorName(postEditDto.authorName());
            book.setBookType(BookType.fromString(postEditDto.bookType()));
            book.setPhotoUrl(postEditDto.bookPhotoUrl());
            book.setPosts(new ArrayList<Post>());
            // save the new created book to persist data
            bookService.saveBook(book);
        } else {
            book.setBookType(BookType.fromString(postEditDto.bookType()));
            book.setPhotoUrl(postEditDto.bookPhotoUrl());
        }
        book.getPosts().add(presentPost);

        // set the fields of post
        presentPost.setCommitDate(LocalDate.now());
        presentPost.setReview(postEditDto.bookReview());
        presentPost.setBook(book);

        return presentPost;
    }

    public PostResponseDto toPostResponseDto(Post post) {
        // format the date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate postDate = post.getCommitDate();
        String formattedDate = postDate.format(formatter);

        return new PostResponseDto(
                post.getId(),
                post.getReview(),
                formattedDate,
                post.getUser().getId(),
                post.getUser().getFullname(),
                post.getUser().getPhotoUrl(),
                post.getBook().getTitle(),
                post.getBook().getAuthorName(),
                post.getBook().getPhotoUrl(),
                post.getBook().getBookType().toString()
        );
    }
}

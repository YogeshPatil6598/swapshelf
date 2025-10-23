package com.swapshelf.service.book.impl;

import com.swapshelf.entity.Book;
import com.swapshelf.entity.User;
import com.swapshelf.repository.BookRepository;
import com.swapshelf.service.book.BookService;
import com.swapshelf.service.user.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service("bookService")
public class BookServiceImpl implements BookService {

    @Resource
    private BookRepository bookRepository;

    @Resource
    private UserService userService;

    @Override
    public List<Book> findByOwner(User user) {
        return bookRepository.findByOwner(user);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        if(bookRepository.findById(id).isPresent()) {
            return bookRepository.findById(id).get();
        }
        return null;
    }

    @Override
    public List<Book> findBooksByFilters(Long userId, String title, String author, String genre, String tag) {
        return bookRepository.findBooksByFilters(userId, title, author, genre, tag);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> searchBooks(String query) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCase(query, query, query);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<String> getAllTags() {
        return bookRepository.findAllTags();
    }




}

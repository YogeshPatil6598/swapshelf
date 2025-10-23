package com.swapshelf.service.book;

import com.swapshelf.entity.Book;
import com.swapshelf.entity.User;

import java.util.List;

public interface BookService {

    List<Book> findByOwner(User user);

    List<Book> getAllBooks();

    Book getBookById(Long id);

    List<Book> findBooksByFilters(Long userId, String title, String author, String genre, String tag);

    public List<Book> findAll();

    public List<Book> searchBooks(String query);

    public void deleteBook(Long id);
    List<String> getAllTags();

}

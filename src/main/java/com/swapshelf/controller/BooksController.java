package com.swapshelf.controller;

import com.swapshelf.entity.Book;
import com.swapshelf.entity.User;
import com.swapshelf.repository.BookRepository;
import com.swapshelf.service.book.BookService;
import com.swapshelf.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class BooksController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookRepository bookRepository;

    // Show all books from all users
    @GetMapping("/books")
    public String showAllBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String tag,
            Principal principal,
            Model model) {

        User currentUser = userService.findByUsername(principal.getName());

        List<Book> filteredBooks = bookService.findBooksByFilters(currentUser.getId(), title, author, genre, tag);
        model.addAttribute("books", filteredBooks);
        List<String> genres = bookRepository.findAllGenres();
        List<String> tags=bookRepository.findAllTags();
//        List<Book> books;

//        if (genre != null && !genre.isEmpty()) {
//            books = bookRepository.findByGenre(genre);
//        } else {
//            books = bookRepository.findAll();
//        }
//        if (tag != null && !tag.isEmpty()) {
//            books = bookRepository.findByTags(tag);
//        } else {
//            books = bookRepository.findAll();
//        }

        model.addAttribute("genres", genres);
        model.addAttribute("tags",tags);
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("selectedTag",tag);
//        model.addAttribute("books", books);

        return "all-books";
    }

    @GetMapping("/book/{id}/details")
    public String viewBookDetails(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "book-details";
    }





}

package com.swapshelf.controller;

import com.swapshelf.entity.Book;
import com.swapshelf.entity.BookExchangeRequest;
import com.swapshelf.entity.User;
import com.swapshelf.entity.enums.BookStatus;
import com.swapshelf.entity.enums.ExchangeRequestStatus;
import com.swapshelf.repository.BookExchangeRequestRepository;
import com.swapshelf.repository.BookRepository;
import com.swapshelf.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/exchange")
@RequiredArgsConstructor
public class BookExchangeRequestController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookExchangeRequestRepository exchangeRequestRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/request")
    public String requestExchange(@RequestParam Long bookId, Principal principal) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book ID"));

        User requester = userService.findByUsername(principal.getName());

        // Prevent requesting own book
        if (book.getOwner().getId().equals(requester.getId())) {
            return "redirect:/books?error=self-request";
        }

        BookExchangeRequest request = BookExchangeRequest.builder()
                .book(book)
                .requester(requester)
                .status(ExchangeRequestStatus.PENDING)
                .build();

        book.setStatus(BookStatus.REQUESTED);
        bookRepository.save(book);

        exchangeRequestRepository.save(request);

        return "redirect:/my-account/request-details/" + request.getId();
    }
}


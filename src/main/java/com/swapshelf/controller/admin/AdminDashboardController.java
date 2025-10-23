package com.swapshelf.controller.admin;

import com.swapshelf.dto.BookDto;
import com.swapshelf.dto.ExchangeRequestDto;
import com.swapshelf.dto.UserDto;
import com.swapshelf.entity.Book;
import com.swapshelf.entity.BookExchangeRequest;
import com.swapshelf.entity.User;
import com.swapshelf.entity.embeddable.Address;
import com.swapshelf.entity.enums.ExchangeRequestStatus;
import com.swapshelf.repository.UserRepository;
import com.swapshelf.service.book.BookService;
import com.swapshelf.service.exchangerequest.ExchangeRequestService;
import com.swapshelf.service.user.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


// AdminDashboardController.java

@Controller
@RequestMapping("/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private ExchangeRequestService exchangeRequestService;

    @Resource
    private UserRepository userRepository;

    @GetMapping("")
    public String getAdminDashboard(Model model, Principal principal) {
        return "admin/dashboard";
    }

    // GET /admin/users
//    @GetMapping("/users")
//    public String viewAllUsers(@RequestParam(value = "search", required = false) String search, Model model) {
//        List<User> users = (search != null && !search.isBlank()) ?
//                userService.searchUsers(search) :
//                userService.findAll();
//        model.addAttribute("users", users);
//        return "admin/dashboard";
//    }

    // POST /admin/users/delete
    @DeleteMapping("/users/{id}")
    @ResponseBody
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PutMapping("/users/{id}")
    @ResponseBody
    public ResponseEntity<String> updateUserProfile(@PathVariable Long id,
                                                    @RequestParam(required = false) String firstName,
                                                    @RequestParam(required = false) String lastName,
                                                    @RequestParam(required = false) String email,
                                                    @RequestParam(required = false) String mobileno,
                                                    @RequestParam(required = false) String line1,
                                                    @RequestParam(required = false) String line2,
                                                    @RequestParam(required = false) String city,
                                                    @RequestParam(required = false) String state,
                                                    @RequestParam(required = false) String postalCode) {

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
        }

        // Update only if value is provided
        if (firstName != null && !firstName.trim().isEmpty()) user.setFirstName(firstName.trim());
        if (lastName != null && !lastName.trim().isEmpty()) user.setLastName(lastName.trim());
        if (email != null && !email.trim().isEmpty()) user.setEmail(email.trim());
        if (mobileno != null && !mobileno.trim().isEmpty()) user.setMobileno(mobileno.trim());

        Address address = user.getAddress() != null ? user.getAddress() : new Address();

        if (line1 != null && !line1.trim().isEmpty()) address.setLine1(line1.trim());
        if (line2 != null && !line2.trim().isEmpty()) address.setLine2(line2.trim());
        if (city != null && !city.trim().isEmpty()) address.setCity(city.trim());
        if (state != null && !state.trim().isEmpty()) address.setState(state.trim());
        if (postalCode != null && !postalCode.trim().isEmpty()) address.setPostalCode(postalCode.trim());

        user.setAddress(address);
        userRepository.save(user);
        return ResponseEntity.ok("UPDATED");
    }




    // GET /admin/books
//    @GetMapping("/books")
//    public String viewAllBooks(@RequestParam(value = "search", required = false) String search, Model model) {
//        List<Book> books = (search != null && !search.isBlank()) ?
//                bookService.searchBooks(search) :
//                bookService.findAll();
//        model.addAttribute("books", books);
//        return "admin/dashboard";
//    }

    // POST /admin/books/delete
    @PostMapping("/books/delete")
    public String deleteBook(@RequestParam Long id) {
        bookService.deleteBook(id);
        return "redirect:/admin/books";
    }

    // GET /admin/exchange-requests
//    @GetMapping("/exchange-requests")
//    public String viewAllExchangeRequests(@RequestParam(value = "status", required = false) ExchangeRequestStatus status, Model model) {
//        List<BookExchangeRequest> requests = (status != null) ?
//                exchangeRequestService.findByStatus(status) :
//                exchangeRequestService.findAll();
//        model.addAttribute("requests", requests);
//        return "admin/dashboard";
//    }

    @GetMapping("/all-users")
    public String getUsers(Model model) {
        return "admin/all-users";
    }



    @GetMapping("/users")
    @ResponseBody
    public ResponseEntity<List<UserDto>> getUsers(HttpServletRequest request, Principal principal) {
        List<User> users =  userService.findAll();
        List<UserDto> userDto = new ArrayList<>();
        for(User us : users) {
            UserDto user = new UserDto();
            user.setId(us.getId());
            user.setEmail(us.getEmail());
            user.setFirstName(us.getFirstName());
            user.setLastName(us.getLastName());
            userDto.add(user);
        }
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/all-books")
    public String getBooks(Model model) {
        return "admin/all-books";
    }

    @GetMapping("/books")
    @ResponseBody
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<Book> books = bookService.getAllBooks(); // Replace with your method
        List<BookDto> bookDtos = new ArrayList<>();

        for (Book book : books) {
            BookDto dto = new BookDto();
            dto.setId(book.getId());
            dto.setTitle(book.getTitle());
            dto.setAuthor(book.getAuthor());
            dto.setStatus(book.getStatus().toString()); // If status is enum

            if (book.getOwner() != null) {
                UserDto ownerDto = new UserDto();
                ownerDto.setFirstName(book.getOwner().getFirstName());
                ownerDto.setLastName(book.getOwner().getLastName());

                dto.setOwner(ownerDto);
            }
            bookDtos.add(dto);
        }

        return new ResponseEntity<>(bookDtos, HttpStatus.OK);
    }

//
//    @GetMapping("/books")
//    @ResponseBody
//    public ResponseEntity<List<BookDto>> getBooks(HttpServletRequest request,Principal principal){
//        List<Book> books=bookService.findAll();
//        List<BookDto> bookDto=new ArrayList<>();
//
//
//
//    }

    @GetMapping("/all-exchange-request")
    public String getExchangeRequests(Model model)
    {
        return "admin/all-exchange-request";
    }
    @GetMapping("exchange-request")
    @ResponseBody
    public ResponseEntity<List<ExchangeRequestDto>> getAllExchangeRequests() {
        List<BookExchangeRequest> requests = exchangeRequestService.findAll(); // Your service method
        List<ExchangeRequestDto> dtos = new ArrayList<>();

        for (BookExchangeRequest req : requests) {
            ExchangeRequestDto dto = new ExchangeRequestDto();
            dto.setId(req.getId());
            dto.setStatus(req.getStatus().toString());
            dto.setCreatedAt(req.getCreatedAt());

            // Requester
            if (req.getRequester() != null) {
                UserDto requesterDto = new UserDto();
                requesterDto.setId(req.getRequester().getId());
                requesterDto.setFirstName(req.getRequester().getFirstName());
                requesterDto.setLastName(req.getRequester().getLastName());
                requesterDto.setUsername(req.getRequester().getUsername());
                dto.setRequester(requesterDto);
            }
            if (req.getBook() != null) {
                BookDto bookDto = new BookDto();
                bookDto.setId(req.getBook().getId());         // ✅ This is fine
                bookDto.setTitle(req.getBook().getTitle());
                dto.setBook(bookDto);
            }

// Return Book (optional)
            if (req.getRequestedExchangeBook() != null) {
                BookDto exchBookDto = new BookDto();
                exchBookDto.setId(req.getRequestedExchangeBook().getId());
                exchBookDto.setTitle(req.getRequestedExchangeBook().getTitle());
                dto.setRequestedExchangeBook(exchBookDto);


            }
            dtos.add(dto);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

//    @DeleteMapping("/admin/dashboard/users/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        userService.deleteById(id);  // implement this in service
//        return ResponseEntity.noContent().build();
//    }






}

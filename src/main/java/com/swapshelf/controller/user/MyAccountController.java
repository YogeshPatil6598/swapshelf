package com.swapshelf.controller.user;

import com.swapshelf.dto.AddressDTO;
import com.swapshelf.dto.UserDto;
import com.swapshelf.entity.Book;
import com.swapshelf.entity.BookExchangeRequest;
import com.swapshelf.entity.User;
import com.swapshelf.entity.embeddable.Address;
import com.swapshelf.entity.enums.BookStatus;
import com.swapshelf.entity.enums.ExchangeRequestStatus;
import com.swapshelf.repository.BookExchangeRequestRepository;
import com.swapshelf.repository.BookRepository;
import com.swapshelf.repository.UserRepository;
import com.swapshelf.service.book.BookService;
import com.swapshelf.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/my-account")
public class MyAccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookExchangeRequestRepository exchangeRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/books")
    public String showBooks(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("books", bookService.findByOwner(user));
        model.addAttribute("book", new Book()); // for the modal form
        return "my-account/books";
    }

    @PostMapping("/books")
    public String createBook(@ModelAttribute Book book,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             Principal principal) throws IOException, InterruptedException {

        // Handle image saving
        if (!imageFile.isEmpty()) {
            String uploadDir = "uploads/";
            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get(uploadDir, filename);
            Files.createDirectories(path.getParent());
            Files.write(path, imageFile.getBytes());

            uploadDir = "/uploads/";
            book.setImagePath(uploadDir + filename);
        }


        // Set current user
        User user = userService.findByUsername(principal.getName());
        book.setOwner(user);

        bookRepository.save(book);


        return "redirect:/my-account/books";
    }

    @DeleteMapping("/books/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteBook(@PathVariable Long id, Principal principal) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null && book.getOwner().getUsername().equals(principal.getName())) {
            bookRepository.delete(book);
            return ResponseEntity.ok("DELETED");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("NOT_ALLOWED");
    }

    @PutMapping("/books/{id}")
    @ResponseBody
    public ResponseEntity<String> updateBook(@PathVariable Long id,
                                             @RequestParam String title,
                                             @RequestParam String author,
                                             @RequestParam(required = false) String genre,
                                             @RequestParam(required = false) String tags,
                                             @RequestParam(required = false) String description,
                                             @RequestParam String status,
                                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                             Principal principal) throws IOException {

        Book book = bookRepository.findById(id).orElse(null);
        if (book == null || !book.getOwner().getUsername().equals(principal.getName())) {
            return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
        }

        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);
        book.setTags(tags);
        book.setDescription(description);
        BookStatus bookStatus = BookStatus.valueOf(status);
        book.setStatus(bookStatus);

        if (!imageFile.isEmpty()) {

            String uploadDir = "uploads/";
            String filename = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path path = Paths.get(uploadDir, filename);
            Files.createDirectories(path.getParent());
            Files.write(path, imageFile.getBytes());

            uploadDir = "/uploads/";
            book.setImagePath(uploadDir + filename);
        }


        bookRepository.save(book);
        return ResponseEntity.ok("UPDATED");
    }




    @GetMapping("/exchange-requests")
    public String viewRequests(Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());

        // Sent requests
        List<BookExchangeRequest> sent = exchangeRequestRepository.findByRequester(currentUser)
                .stream()
                .sorted(Comparator.comparing(BookExchangeRequest::getCreatedAt).reversed())
                .collect(Collectors.toList());;

        // Received requests (books owned by current user)
        List<BookExchangeRequest> received = exchangeRequestRepository.findByBookOwner(currentUser)
                .stream()
                .sorted(Comparator.comparing(BookExchangeRequest::getCreatedAt).reversed())
                .collect(Collectors.toList());;

        model.addAttribute("sentRequests", sent);
        model.addAttribute("receivedRequests", received);

        return "my-account/exchange-request"; // Create this HTML page
    }

    @GetMapping("/profile")
    public String viewProfile(Model model,Principal principal ){
            String username=principal.getName();
            User user = userRepository.findByUsername(username);
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setEmail(user.getEmail());
            userDto.setMobileno(user.getMobileno());
            if(Objects.isNull(user.getAddress())) {
                AddressDTO address = new AddressDTO();
                address.setLine1("");
                address.setCountry("India");
                address.setState("");
                address.setLine2("");
                address.setPostalCode("");
                address.setCity("");
                userDto.setAddress(address);
            } else {
                AddressDTO address = new AddressDTO();
                Address address1 = user.getAddress();
                address.setCity(address1.getCity());
                address.setCountry(address1.getCountry());
                address.setLine1(address1.getLine1());
                address.setLine2(address1.getLine2());
                address.setState(address1.getState());
                address.setPostalCode(address1.getPostalCode());
                userDto.setAddress(address);
            }
             model.addAttribute("user",userDto);

        return "my-account/profile";
    }

    //profile edit
    @PutMapping("/profile/{userId}")
    @ResponseBody
    public ResponseEntity<String> updateUserProfile(@PathVariable Long userId,
                                                    @RequestParam(required = false) String firstName,
                                                    @RequestParam(required = false) String lastName,
                                                    @RequestParam(required = false) String email,
                                                    @RequestParam(required = false) String mobileno,
                                                    @RequestParam(required = false) String line1,
                                                    @RequestParam(required = false) String line2,
                                                    @RequestParam(required = false) String city,
                                                    @RequestParam(required = false) String state,
                                                    @RequestParam(required = false) String postalCode,
                                                    Principal principal) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null || !user.getEmail().equals(principal.getName())) {
            return new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
        }


      //  user.setFirstName(firstName);
      //  user.setLastName(lastName);
       // user.setMobileno(mobileno);

        //Address address = new Address();
//        address.setLine1(line1);
//        address.setLine2(line2);
//        address.setCity(city);
//        address.setState(state);
//        address.setPostalCode(postalCode);
//
//        user.setAddress(address);
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


    @GetMapping("/request-details/{id}")
    public String viewRequest(@PathVariable("id") Long id, Model model, Principal principal){
        Optional<BookExchangeRequest> bookExchangeRequest = exchangeRequestRepository.findById(id);
        User currentUser = userService.findByUsername(principal.getName());
        if (bookExchangeRequest.isPresent()){
            BookExchangeRequest exReq = bookExchangeRequest.get();
            if(currentUser == exReq.getBook().getOwner()
                || (Objects.nonNull(exReq.getRequestedExchangeBook())
                        && currentUser == exReq.getRequestedExchangeBook().getOwner())
                || currentUser == exReq.getRequester()) {
                model.addAttribute("req", exReq);
               List<Book> all_books= exReq.getRequester().getBooks();
               if(!CollectionUtils.isEmpty(all_books))
               {
                 List<Book> filtered_books = all_books.stream().filter(n -> n.getStatus().equals(BookStatus.AVAILABLE)).toList();
                   model.addAttribute("books", filtered_books);
               }

                boolean noActionOnRequest = (exReq.getStatus().equals(ExchangeRequestStatus.PENDING));
                model.addAttribute("noActionOnRequest", noActionOnRequest);
                boolean ownerRequestingExchange = exReq.getStatus().equals(ExchangeRequestStatus.OWNER_PROPOSED_EXCHANGE);
                model.addAttribute("ownerRequestingExchange", ownerRequestingExchange);
                if(ownerRequestingExchange) {
                    model.addAttribute("requestedExchangeBook", exReq.getRequestedExchangeBook());
                }
                boolean isExchangeRequestedByCurrentOwner = exReq.getStatus().equals(ExchangeRequestStatus.OWNER_PROPOSED_EXCHANGE) &&
                        exReq.getRequestedExchangeBook().getOwner() == currentUser;
                model.addAttribute("isExchangeRequestedByCurrentOwner",isExchangeRequestedByCurrentOwner);
                boolean ownRequest = exReq.getRequester() == currentUser;
                model.addAttribute("ownRequest", ownRequest);
                return "my-account/request-details";

            }
        }

        return "redirect:/my-account/books";
    }

    @PostMapping("/request-details/accept")
    public String acceptRequest(@RequestParam Long id,
                                @RequestParam(required = false) String exchangeBookId, Model model, Principal principal){
        Optional<BookExchangeRequest> bookExchangeRequest = exchangeRequestRepository.findById(id);
        User currentUser = userService.findByUsername(principal.getName());
        if (bookExchangeRequest.isPresent()){
            BookExchangeRequest exReq = bookExchangeRequest.get();
            if(!exchangeBookId.isEmpty()) {
                long bookId = Long.parseLong(exchangeBookId);
                Optional<Book> requestedExchangeBook = bookRepository.findById(bookId);
                if(requestedExchangeBook.isPresent()) {
                    Book book = requestedExchangeBook.get();
                    book.setStatus(BookStatus.REQUESTED);
                    bookRepository.save(book);
                    exReq.setRequestedExchangeBook(book);
                    exReq.setStatus(ExchangeRequestStatus.OWNER_PROPOSED_EXCHANGE);
                    exchangeRequestRepository.save(exReq);
                }
            }
        }
        return "redirect:/my-account/request-details/" + id;
    }

    @PostMapping("/request-details/confirm")
    public String confirmRequest(@RequestParam Long id, Model model, Principal principal){
        Optional<BookExchangeRequest> bookExchangeRequest = exchangeRequestRepository.findById(id);
        User currentUser = userService.findByUsername(principal.getName());
        if (bookExchangeRequest.isPresent()){
            BookExchangeRequest exReq = bookExchangeRequest.get();
            exReq.setStatus(ExchangeRequestStatus.IN_USE);
            Book book = exReq.getBook();
            book.setStatus(BookStatus.EXCHANGED);
            bookRepository.save(book);

            if(Objects.nonNull(exReq.getRequestedExchangeBook())) {
                Book book1 = exReq.getRequestedExchangeBook();
                book1.setStatus(BookStatus.EXCHANGED);
                bookRepository.save(book1);
            }

            exchangeRequestRepository.save(exReq);
        }
        return "redirect:/my-account/request-details/" + id;
    }

    @PostMapping("/request-details/confirm-exchange")
    public String confirmExchange(@RequestParam Long id, Model model, Principal principal){
        Optional<BookExchangeRequest> bookExchangeRequest = exchangeRequestRepository.findById(id);
        User currentUser = userService.findByUsername(principal.getName());
        if (bookExchangeRequest.isPresent()){
            BookExchangeRequest exReq = bookExchangeRequest.get();
            exReq.setStatus(ExchangeRequestStatus.IN_USE);
            Book book = exReq.getBook();
            book.setStatus(BookStatus.EXCHANGED);
            if(Objects.nonNull(exReq.getRequestedExchangeBook())) {
                Book book1 = exReq.getRequestedExchangeBook();
                book1.setStatus(BookStatus.EXCHANGED);
                bookRepository.save(book1);
            }
            bookRepository.save(book);
            exchangeRequestRepository.save(exReq);
        }
        return "redirect:/my-account/request-details/" + id;
    }

    //complete exchange

    @PostMapping("/request-details/complete")
    public String completeExchange(@RequestParam Long id, Model model, Principal principal){
        Optional<BookExchangeRequest> bookExchangeRequest = exchangeRequestRepository.findById(id);
        User currentUser = userService.findByUsername(principal.getName());
        if (bookExchangeRequest.isPresent()){
            BookExchangeRequest exReq = bookExchangeRequest.get();
            exReq.setStatus(ExchangeRequestStatus.COMPLETED);
            Book book = exReq.getBook();
            book.setStatus(BookStatus.AVAILABLE);
            if(Objects.nonNull(exReq.getRequestedExchangeBook())) {
                Book book1 = exReq.getRequestedExchangeBook();
                book1.setStatus(BookStatus.AVAILABLE);
                bookRepository.save(book1);
            }
            bookRepository.save(book);
            exchangeRequestRepository.save(exReq);
        }
        return "redirect:/my-account/request-details/" + id;
    }

    @PostMapping("/request-details/reject")
    public String rejectRequest(@RequestParam Long id, Model model, Principal principal){
        Optional<BookExchangeRequest> bookExchangeRequest = exchangeRequestRepository.findById(id);
        User currentUser = userService.findByUsername(principal.getName());
        if (bookExchangeRequest.isPresent()){
            BookExchangeRequest exReq = bookExchangeRequest.get();
            if(currentUser == exReq.getBook().getOwner()) {
                exReq.setStatus(ExchangeRequestStatus.REJECTED);
                Book book = exReq.getBook();
                book.setStatus(BookStatus.AVAILABLE);
                bookRepository.save(book);
                exchangeRequestRepository.save(exReq);
            }
        }
        return "redirect:/my-account/request-details/" + id;
    }

//    @PostMapping("/requests/accept")
//    public String acceptRequest(@RequestParam Long requestId) {
//        // Your accept logic
//        return "redirect:/requests";
//    }
//
//    @PostMapping("/requests/reject")
//    public String rejectRequest(@RequestParam Long requestId) {
//        // Your reject logic
//        return "redirect:/requests";
//    }
//
//    @PostMapping("/requests/assign")
//    public String assignBook(@RequestParam Long requestId, @RequestParam Long bookId) {
//        // Logic to assign book to request
//        return "redirect:/requests";
//    }










}

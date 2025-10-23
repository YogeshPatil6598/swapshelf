package com.swapshelf.entity;

import com.swapshelf.entity.embeddable.Address;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String role;

    private String mobileno;

    @Getter
    @Setter
    @Embedded
    private Address address;

    private boolean enabled = true;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books = new ArrayList<>();

    // Requests made by this user to other users’ books
    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL)
    private List<BookExchangeRequest> exchangeRequestsMade;

}


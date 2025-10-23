package com.swapshelf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String role;

    private String mobileno;

    private AddressDTO address;
}

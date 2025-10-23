package com.swapshelf.dto;


import com.swapshelf.entity.User;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private Long id;

    private String title;
    private String author;
    private String genre;
    private String tags;
    private String status;
   private UserDto owner;
//    public void setStatus(String string) {
//    }
//
//    public void setOwnerUsername(String s) {
//    }
}

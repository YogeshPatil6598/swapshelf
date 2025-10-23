package com.swapshelf.entity.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
@Embeddable
public class Address {

    private String line1;
    private String line2;
    private String city;
    private String postalCode;
    private String state;
    private String country;

}

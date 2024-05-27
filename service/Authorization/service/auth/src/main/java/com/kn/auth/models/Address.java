package com.kn.auth.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Address {
    private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}

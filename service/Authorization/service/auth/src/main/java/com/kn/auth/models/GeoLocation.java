package com.kn.auth.models;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class GeoLocation {
    private double latitude;
    private double longitude;
}

package com.AirBnb.Final.Project.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Builder
public class HotelContactInfo {

    @Email(message = "must end with @gmail.com")
    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phone;

}

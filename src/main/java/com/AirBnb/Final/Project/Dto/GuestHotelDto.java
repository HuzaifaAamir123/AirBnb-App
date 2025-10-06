package com.AirBnb.Final.Project.Dto;


import com.AirBnb.Final.Project.Entity.HotelContactInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

//we use guest hotel dto in hotel info dto when user search hotels
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GuestHotelDto {

    private String name;

    private String city;

    private String[] photos;

    private String[] amenities;

    private HotelContactInfo contactInfo;

    private BigDecimal price;

}

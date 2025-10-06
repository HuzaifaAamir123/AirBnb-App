package com.AirBnb.Final.Project.Dto;

import com.AirBnb.Final.Project.Entity.Hotel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OnlyHotelInfoDto {

    private Hotel hotel;

    private BigDecimal price;

}

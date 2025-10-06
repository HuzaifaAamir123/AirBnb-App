package com.AirBnb.Final.Project.Dto;

import lombok.*;

import java.math.BigDecimal;

//we use guest room dto in hotel info dto when user search hotels
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuestRoomDto {

    private String name;

    private String[] photos;

    private String[] amenities;

    private BigDecimal price;


}

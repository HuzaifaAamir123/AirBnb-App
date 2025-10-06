package com.AirBnb.Final.Project.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


// we use hotel info dto when user searches the hotels
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HotelInfoDto {

    private GuestHotelDto hotel;

    private List<GuestRoomDto>rooms;

}

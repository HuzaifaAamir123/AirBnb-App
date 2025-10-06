package com.AirBnb.Final.Project.Service;

import com.AirBnb.Final.Project.Dto.AdminHotelDto;

import java.util.List;

public interface HotelService {

    AdminHotelDto createNewHotel(AdminHotelDto adminHotelDto);

    AdminHotelDto getHotelById(Long hotelId);

    AdminHotelDto updateHotel(Long hotelId, AdminHotelDto adminHotelDto);

    AdminHotelDto partialUpdateHotel(Long hotelId);

    void deleteHotelById(Long hotelId);

    List<AdminHotelDto> getAllHotelsForAdmin();
}

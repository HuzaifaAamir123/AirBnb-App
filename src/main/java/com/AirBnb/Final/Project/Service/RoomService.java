package com.AirBnb.Final.Project.Service;

import com.AirBnb.Final.Project.Dto.AdminRoomDto;

import java.util.List;

public interface RoomService {

    AdminRoomDto createNewRoom(Long hotelId, AdminRoomDto adminRoomDto);

    AdminRoomDto getRoomById(Long roomId);

    List<AdminRoomDto> getAllRoomsInHotel(Long hotelId);

    AdminRoomDto updateRoomById(Long roomId, AdminRoomDto adminRoomDto);

    void deleteRoomById(Long roomId);
}

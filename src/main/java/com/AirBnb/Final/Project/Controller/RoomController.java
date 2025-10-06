package com.AirBnb.Final.Project.Controller;


import com.AirBnb.Final.Project.Dto.AdminRoomDto;
import com.AirBnb.Final.Project.Service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/hotel/{hotelId}/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    ResponseEntity<AdminRoomDto> createNewRoom(@PathVariable Long hotelId, @RequestBody @Valid AdminRoomDto adminRoomDto){

        AdminRoomDto room=roomService.createNewRoom(hotelId, adminRoomDto);

        return new ResponseEntity<>(room, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{roomId}")
    ResponseEntity<AdminRoomDto> getRoomById(@PathVariable Long roomId){

        AdminRoomDto room=roomService.getRoomById(roomId);

        return ResponseEntity.ok(room);
    }

    @GetMapping(path = "/all")
    ResponseEntity<List<AdminRoomDto>> getAllRoomsInHotel(@PathVariable Long hotelId){

        List<AdminRoomDto> room=roomService.getAllRoomsInHotel(hotelId);

        return ResponseEntity.ok(room);
    }

    @PutMapping(path = "/{roomId}")
    ResponseEntity<AdminRoomDto> updateHotelById(@PathVariable Long roomId, @RequestBody @Valid AdminRoomDto adminRoomDto){

        AdminRoomDto room=roomService.updateRoomById(roomId, adminRoomDto);

        return new ResponseEntity<>(room, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{roomId}")
    ResponseEntity<Void> deleteRoomById(@PathVariable Long roomId){

        roomService.deleteRoomById(roomId);

        return ResponseEntity.noContent().build();
    }

}

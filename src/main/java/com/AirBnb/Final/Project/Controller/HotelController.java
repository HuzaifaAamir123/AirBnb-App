package com.AirBnb.Final.Project.Controller;

import com.AirBnb.Final.Project.Dto.AdminHotelDto;
import com.AirBnb.Final.Project.Service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/admin/hotel")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @PostMapping
    ResponseEntity<AdminHotelDto> createNewHotel(@RequestBody @Valid AdminHotelDto adminHotelDto){

        AdminHotelDto hotel=hotelService.createNewHotel(adminHotelDto);

        return new ResponseEntity<>(hotel, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{hotelId}")
    ResponseEntity<AdminHotelDto> getHotelById(@PathVariable Long hotelId){

        AdminHotelDto hotel=hotelService.getHotelById(hotelId);

        return ResponseEntity.ok(hotel);
    }

    @PutMapping(path = "/{hotelId}")
    ResponseEntity<AdminHotelDto> updateHotel(@PathVariable Long hotelId, @RequestBody @Valid AdminHotelDto adminHotelDto){

        AdminHotelDto hotel=hotelService.updateHotel(hotelId, adminHotelDto);

        return new ResponseEntity<>(hotel, HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{hotelId}")
    ResponseEntity<AdminHotelDto> partialUpdateHotel(@PathVariable Long hotelId){

        AdminHotelDto hotel=hotelService.partialUpdateHotel(hotelId);

        return new ResponseEntity<>(hotel, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{hotelId}")
    ResponseEntity<Void> deleteHotelById(@PathVariable Long hotelId){

        hotelService.deleteHotelById(hotelId);

        return ResponseEntity.noContent().build();
    }

}

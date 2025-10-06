package com.AirBnb.Final.Project.Controller;

import com.AirBnb.Final.Project.Dto.BookingDto;
import com.AirBnb.Final.Project.Dto.BookingRequestDto;

import com.AirBnb.Final.Project.Dto.GuestDto;
import com.AirBnb.Final.Project.Service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/booking")
@RequiredArgsConstructor
public class HotelBookingController {

    private final BookingService bookingService;

    @PostMapping(path = "/init")
    ResponseEntity<BookingDto> initiateBooking(@RequestBody @Valid BookingRequestDto bookingRequestDto){

        BookingDto booking=bookingService.initiateBooking(bookingRequestDto);

        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    @PutMapping(path = "/addGuest/{bookingId}")
    ResponseEntity<BookingDto> addGuestInBooking(@PathVariable Long bookingId, @RequestBody @Valid List<GuestDto>guestDtoList){

        BookingDto booking=bookingService.addGuestInBooking(bookingId,guestDtoList);

        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }


}

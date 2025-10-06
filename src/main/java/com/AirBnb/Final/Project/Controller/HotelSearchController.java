package com.AirBnb.Final.Project.Controller;

import com.AirBnb.Final.Project.Dto.GuestHotelDto;
import com.AirBnb.Final.Project.Dto.HotelInfoDto;
import com.AirBnb.Final.Project.Dto.HotelSearchRequestDto;
import com.AirBnb.Final.Project.Service.HotelSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/hotel/search")
@RequiredArgsConstructor
public class HotelSearchController {

    private final HotelSearchService hotelSearchService;

    @GetMapping
    ResponseEntity<List<GuestHotelDto>> searchHotelInPaginatedForm(@RequestBody @Valid HotelSearchRequestDto hotelSearchRequestDto){

        List<GuestHotelDto>guestHotelDtoList=hotelSearchService.searchHotelInPaginatedForm(hotelSearchRequestDto);

        return ResponseEntity.ok(guestHotelDtoList);
    }

    @GetMapping("/info/{hotelId}")
    ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId,@RequestBody @Valid HotelSearchRequestDto hotelSearchRequestDto){

        HotelInfoDto hotelInfoDto=hotelSearchService.getHotelInfo(hotelId,hotelSearchRequestDto);

        return ResponseEntity.ok(hotelInfoDto);
    }


}

package com.AirBnb.Final.Project.Service;


import com.AirBnb.Final.Project.Dto.*;
import com.AirBnb.Final.Project.Entity.Hotel;
import com.AirBnb.Final.Project.Entity.Inventory;
import com.AirBnb.Final.Project.Exception.ResourceNotFoundException;
import com.AirBnb.Final.Project.Repository.HotelRepository;
import com.AirBnb.Final.Project.Repository.InventoryRepository;
import com.AirBnb.Final.Project.Repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelSearchServiceImpl implements HotelSearchService{

    private final ModelMapper modelMapper;
    private final InventoryRepository inventoryRepository;
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    void searchUtilFun(HotelSearchRequestDto hotelSearchRequestDto){

        log.info("Customer or Guests Hotel Searching Criteria Checking start here");

        String city=hotelSearchRequestDto.getCity();
        LocalDate startDate=hotelSearchRequestDto.getStartDate();
        LocalDate endDate=hotelSearchRequestDto.getEndDate();
        Integer numberOfRooms=hotelSearchRequestDto.getNumberOfRooms();
        Integer adults=hotelSearchRequestDto.getAdults();

        Long stayDaysCount= ChronoUnit.DAYS.between(startDate,endDate)+1;

        List<Inventory>inventories=inventoryRepository.findByCity(city);
        if (inventories.isEmpty()){
            log.error("hotels are not available in city {}",city);
            throw new ResourceNotFoundException("hotels are not available in city "+city);
        }

        if (startDate.isBefore(LocalDate.now()) || endDate.isBefore(startDate)){
            log.error("invalid Date Range,Date Range must be in present or future");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"invalid Date Range,Date Range must be in present or future");
        }

        if(numberOfRooms>20){
            log.error("maximum number of Rooms Must be 20");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"maximum number of Rooms Must be 20");
        }

        int getMaximumAdult=inventoryRepository.getMaximumAdult(adults);
        if (adults>getMaximumAdult){
            log.error("maximum number of adults must be {}",getMaximumAdult);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"maximum number of adults must be "+getMaximumAdult);
        }

        log.info("Customer or Guests Hotel Searching Criteria is checked successfully");
    }


    @Override
    public List<GuestHotelDto> searchHotelInPaginatedForm(HotelSearchRequestDto hotelSearchRequestDto) {

        log.info("searchHotelInPaginatedForm method start here, here Customer Search the Hotels for Stay");

        searchUtilFun(hotelSearchRequestDto);

        String city=hotelSearchRequestDto.getCity();
        LocalDate startDate=hotelSearchRequestDto.getStartDate();
        LocalDate endDate=hotelSearchRequestDto.getEndDate();
        Integer numberOfRooms=hotelSearchRequestDto.getNumberOfRooms();
        Integer adults=hotelSearchRequestDto.getAdults();

        Long stayDaysCount= ChronoUnit.DAYS.between(startDate,endDate)+1;

        Pageable pageable= PageRequest.of(hotelSearchRequestDto.getPageNumber(),hotelSearchRequestDto.getPageSize());

        Page<GuestHotelDto> hotelDtoPage=inventoryRepository.searchHotelInPagination(
                city,
                startDate,
                endDate,
                pageable
        );

       List<GuestHotelDto>hotels=hotelDtoPage.getContent();


        hotels.forEach(hotel->{
            hotel.setName(hotel.getName().toUpperCase());
        });

        log.info("searchHotelInPaginatedForm method end here, here Customer Search the Hotels for Stay");

       return hotels;
    }

    @Override
    public HotelInfoDto getHotelInfo(Long hotelId, HotelSearchRequestDto hotelSearchRequestDto) {

        log.info("customer searching the particular hotel");

        Hotel hotel=hotelRepository.findById(hotelId)
                .orElseThrow(()->{
                    log.error("hotel with id:{} is not found",hotelId);
                    return new ResourceNotFoundException("hotel with id: "+hotelId+" is not found");
                });

        searchUtilFun(hotelSearchRequestDto);

        String city=hotelSearchRequestDto.getCity();
        LocalDate startDate=hotelSearchRequestDto.getStartDate();
        LocalDate endDate=hotelSearchRequestDto.getEndDate();
        Integer numberOfRooms=hotelSearchRequestDto.getNumberOfRooms();
        Integer adults=hotelSearchRequestDto.getAdults();

        OnlyHotelInfoDto guestHotel =inventoryRepository.getOnlyHotelInfo(
                hotel.getId()
                ,city,
                startDate,
                endDate);

        GuestHotelDto guestHotelDto=new GuestHotelDto(
                guestHotel.getHotel().getName().toUpperCase(),
                guestHotel.getHotel().getCity(),
                guestHotel.getHotel().getPhotos(),
                guestHotel.getHotel().getAmenities(),
                guestHotel.getHotel().getContactInfo(),
                guestHotel.getPrice()
                );



        List<OnlyRoomInfoDto> guestRooms=inventoryRepository.getOnlyRoomInfo(
                hotel.getId()
                ,city,
                startDate,
                endDate);

        List<GuestRoomDto>guestRoomDtoList=new ArrayList<>();


        for (OnlyRoomInfoDto roomInfoDto:guestRooms){

            GuestRoomDto guestRoomDto=GuestRoomDto.builder()
                    .name(roomInfoDto.getRoom().getName().toUpperCase())
                    .photos(roomInfoDto.getRoom().getPhotos())
                    .amenities(roomInfoDto.getRoom().getAmenities())
                    .price(roomInfoDto.getPrice())
                    .build();

            guestRoomDtoList.add(guestRoomDto);
        }

        log.info("customer searching the particular hotel successfully");

        return new HotelInfoDto(guestHotelDto,guestRoomDtoList);
    }

}

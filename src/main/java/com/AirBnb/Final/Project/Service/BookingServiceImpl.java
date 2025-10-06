package com.AirBnb.Final.Project.Service;

import com.AirBnb.Final.Project.Dto.BookingDto;
import com.AirBnb.Final.Project.Dto.BookingRequestDto;
import com.AirBnb.Final.Project.Dto.GuestDto;
import com.AirBnb.Final.Project.Entity.*;
import com.AirBnb.Final.Project.Enum.BookingStatus;
import com.AirBnb.Final.Project.Exception.BookingExpiredException;
import com.AirBnb.Final.Project.Exception.ResourceNotFoundException;
import com.AirBnb.Final.Project.Repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final GuestRepository guestRepository;

    private User getCurrentUser(){
        return  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    void bookingUtilFun(BookingRequestDto bookingRequestDto){

        log.info("Customer or Guests Hotel booking Criteria Checking start here");

        String city=bookingRequestDto.getCity();
        LocalDate checkInDate=bookingRequestDto.getCheckInDate();
        LocalDate checkOutDate=bookingRequestDto.getCheckOutDate();
        Integer numberOfRooms=bookingRequestDto.getNumberOfRooms();


        List<Inventory>inventories=inventoryRepository.findByCity(city);
        if (inventories.isEmpty()){
            log.error("hotels are not available in city {}",city);
            throw new ResourceNotFoundException("hotels are not available in city "+city);
        }

        if (checkInDate.isBefore(LocalDate.now()) || checkOutDate.isBefore(checkInDate)){
            log.info("invalid Date Range,Date Range must be in present or future");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"invalid Date Range,Date Range must be in present or future");
        }

        if(numberOfRooms>20){
            log.error("maximum number of Rooms Must be 20");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"maximum number of Rooms Must be 20");
        }


        log.info("Customer or Guests Hotel booking Criteria is checked successfully");
    }

    @Transactional
    @Override
    public BookingDto initiateBooking(BookingRequestDto bookingRequestDto) {

        log.info("customer or guest start the booking");

        Long hotelId=bookingRequestDto.getHotelId();
        Long roomId=bookingRequestDto.getRoomId();
        String city= bookingRequestDto.getCity();
        LocalDate checkInDate=bookingRequestDto.getCheckInDate();
        LocalDate checkOutDate=bookingRequestDto.getCheckOutDate();
        Integer numberOfRooms=bookingRequestDto.getNumberOfRooms();

        Long stayDaysCount= ChronoUnit.DAYS.between(checkInDate,checkOutDate)+1;

        bookingUtilFun(bookingRequestDto);

        Hotel hotel=hotelRepository.findById(hotelId)
                .orElseThrow(()->new ResourceNotFoundException("hotel with id: "+hotelId+" is not found"));

        List<Room>rooms=hotel.getRooms();

        if (rooms.isEmpty()){
            log.error("no rooms are available in {}",hotel.getName().toUpperCase());
            throw new ResourceNotFoundException("no rooms are available in "+hotel.getName().toUpperCase());
        }

        Room room=rooms.stream()
                .filter(room1 ->room1.getId().equals(roomId))
                .findFirst()
                .orElseThrow(()->{
                    log.error("room with id:{} is not available in {}",roomId,hotel.getName().toUpperCase());
                    return new ResourceNotFoundException("room with id: "+roomId+" is not available in "+hotel.getName().toUpperCase());
                });


        List<Inventory>inventories=inventoryRepository.findAndLockInventory(
          hotelId,roomId,city,checkInDate,checkOutDate,numberOfRooms);


        if (inventories.isEmpty() || stayDaysCount!=inventories.size()){
            log.info("no bookings are satisfy your criteria");
            throw new ResourceNotFoundException("no bookings are satisfy your criteria");
        }

        for (Inventory inventory:inventories){
            inventory.setReservedCount(inventory.getReservedCount()+bookingRequestDto.getNumberOfRooms());
            inventoryRepository.save(inventory);
        }


        BigDecimal bookingPrice=inventoryRepository.getBookingPrice(
                hotel.getId(),
                room.getId(),
                checkInDate,
                checkOutDate,
                city
        );


        Booking booking=Booking.builder()
                .hotel(hotel)
                .room(room)
                .bookingStatus(BookingStatus.RESERVED)
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .price(bookingPrice)
                .totalRooms(numberOfRooms)
                .user(getCurrentUser())
                .build();

        booking= bookingRepository.save(booking);

        BookingDto bookingDto=modelMapper.map(booking,BookingDto.class);
        bookingDto.setHotel(hotel.getName().toUpperCase());
        bookingDto.setRoom(room.getName().toUpperCase());

        log.info("customer or guest initiate the booking");

        return bookingDto;
    }

    @Transactional
    @Override
    public BookingDto addGuestInBooking(Long bookingId, List<GuestDto>guestDtos) {

        log.info("customer try to add there guest in booking");

        Booking booking=bookingRepository.findById(bookingId)
                .orElseThrow(()->{
                    log.error("booking with id:{} is not found",bookingId);
                    return new ResourceNotFoundException("booking with id:"+bookingId+" is not found");
                });

        if (!getCurrentUser().equals(booking.getUser())){
            log.error("booking is not link with user {}",getCurrentUser().getName());
            throw new AccessDeniedException("booking is not link with user "+getCurrentUser().getName());
        }

        if(hasBookingExpired(booking)){
            log.error("booking with id:{} is expired",booking.getId());
            throw new BookingExpiredException("booking with id:"+booking.getId()+" is expired");
        }

        List<Guest>guests=new ArrayList<>();

        for (GuestDto guestDto:guestDtos){

            Guest guest=modelMapper.map(guestDto,Guest.class);
            guest.setUser(getCurrentUser());
            guest= guestRepository.save(guest);
            guests.add(guest);
        }

        booking.getGuests().addAll(guests);
        booking.setBookingStatus(BookingStatus.GUEST_ADDED);
        booking=bookingRepository.save(booking);

        BookingDto bookingDto=modelMapper.map(booking,BookingDto.class);

        bookingDto.setHotel(booking.getHotel().getName().toUpperCase());
        bookingDto.setRoom(booking.getRoom().getName().toUpperCase());

        List<GuestDto>guestDtoList=guests.stream()
                        .map(guest ->{
                            GuestDto guestDto=modelMapper.map(guest,GuestDto.class);
                            guestDto.setName(guest.getName().toUpperCase());
                            return guestDto;
                        }).toList();

        bookingDto.setGuests(guestDtoList);

        log.info("customer added there guest in booking successfully");

        return bookingDto;
    }

    private boolean hasBookingExpired(Booking booking){
        return booking.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(10));
    }



}





package com.AirBnb.Final.Project.Service;

import com.AirBnb.Final.Project.Dto.BookingDto;
import com.AirBnb.Final.Project.Dto.BookingRequestDto;
import com.AirBnb.Final.Project.Dto.GuestDto;

import java.util.List;

public interface BookingService {

    BookingDto initiateBooking(BookingRequestDto bookingRequestDto);

    BookingDto addGuestInBooking(Long bookingId, List<GuestDto>guestDtos);

}

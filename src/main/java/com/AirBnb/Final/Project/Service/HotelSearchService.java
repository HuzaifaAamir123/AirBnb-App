package com.AirBnb.Final.Project.Service;

import com.AirBnb.Final.Project.Dto.GuestHotelDto;
import com.AirBnb.Final.Project.Dto.HotelInfoDto;
import com.AirBnb.Final.Project.Dto.HotelSearchRequestDto;

import java.util.List;

public interface HotelSearchService {

   List<GuestHotelDto>searchHotelInPaginatedForm(HotelSearchRequestDto hotelSearchRequestDto);

   HotelInfoDto getHotelInfo(Long hotelId,HotelSearchRequestDto hotelSearchRequestDto);
}

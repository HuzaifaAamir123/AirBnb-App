package com.AirBnb.Final.Project.Dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

// while searching the hotels ,user uses this dto to search hotels
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HotelSearchRequestDto {

    @Size(min = 5,max = 40,message = "city character must be in range 5 to 40")
    @NotNull(message = "hotel city must fill")
    private String city;

    @FutureOrPresent
    private LocalDate startDate;

    @FutureOrPresent
    private LocalDate endDate;

    @Positive(message = "you must search at least one room")
    private Integer numberOfRooms;

    private Integer adults;


    private Integer pageNumber;


    private Integer pageSize;
}

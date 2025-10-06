package com.AirBnb.Final.Project.Dto;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDto {

    @Positive(message = "hotel id must be positive")
    private Long hotelId;

    @Positive(message = "room id must be positive")
    private Long roomId;

    @Size(min = 5,max = 40,message = "city character must be in range 5 to 40")
    @NotNull(message = "hotel city must fill")
    private String city;

    @FutureOrPresent
    private LocalDate checkInDate;

    @FutureOrPresent
    private LocalDate checkOutDate;

    @Positive(message = "you must book at least one room")
    private Integer numberOfRooms;



}

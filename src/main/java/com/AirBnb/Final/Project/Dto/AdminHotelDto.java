package com.AirBnb.Final.Project.Dto;


import com.AirBnb.Final.Project.Entity.HotelContactInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminHotelDto {

    private Long id;

    @Size(min = 5,max = 40,message = "hotel name character must be in range 5 to 40")
    @NotNull(message = "hotel name must fill")
    private String name;

    @Size(min = 5,max = 40,message = "city character must be in range 5 to 40")
    @NotNull(message = "hotel city must fill")
    private String city;

    @NotEmpty(message = "hotel photos must be present")
    private String[] photos;

    @NotEmpty(message = "hotel amenities must be present")
    private String[] amenities;

    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "hh:mm:ss dd-MM-yyyy")
    private LocalDateTime createdAt;

    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "hh:mm:ss dd-MM-yyyy")
    private LocalDateTime updatedAt;

    private boolean active;

    private HotelContactInfo contactInfo;

}

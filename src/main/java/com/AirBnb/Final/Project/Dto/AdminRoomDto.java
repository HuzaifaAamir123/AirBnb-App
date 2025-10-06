package com.AirBnb.Final.Project.Dto;

import com.AirBnb.Final.Project.Annotation.RoomBasePriceAnnotation;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminRoomDto {

    private Long id;

    private String hotel;

    @Size(min = 5,max = 40,message = "room name character must be in range 5 to 40")
    @NotNull(message = "room name must fill")
    private String name;

    @NotEmpty(message = "room photos must be present")
    private String[] photos;

    @NotEmpty(message = "room amenities must be present")
    private String[] amenities;

    @PositiveOrZero(message = "total count must be zero or positive not be negative")
    private Integer totalCount;

    @Positive(message = "capacity must be positive not be negative")
    private Integer capacity;

    @RoomBasePriceAnnotation
    @Positive(message = "basePrice must be positive not be negative")
    private BigDecimal basePrice;

    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "hh:mm:ss dd-MM-yyyy")
    private LocalDateTime createdAt;

    @FutureOrPresent
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "hh:mm:ss dd-MM-yyyy")
    private LocalDateTime updatedAt;

}

package com.AirBnb.Final.Project.Dto;


import com.AirBnb.Final.Project.Entity.Guest;
import com.AirBnb.Final.Project.Entity.Hotel;
import com.AirBnb.Final.Project.Entity.Room;
import com.AirBnb.Final.Project.Entity.User;
import com.AirBnb.Final.Project.Enum.BookingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {

    private Long id;

    private String hotel;

    private String room;

    private String user;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private BigDecimal price;

    private BookingStatus bookingStatus;

    private int totalRooms;

    private List<GuestDto> guests;

}

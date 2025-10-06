package com.AirBnb.Final.Project.Dto;

import com.AirBnb.Final.Project.Entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class OnlyRoomInfoDto {

    private Room room;

    private BigDecimal price;

    public OnlyRoomInfoDto(Room room, Double price) {
        this.room = room;
        this.price = BigDecimal.valueOf(price);
    }
}

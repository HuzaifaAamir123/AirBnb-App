package com.AirBnb.Final.Project.Repository;


import com.AirBnb.Final.Project.Dto.GuestHotelDto;
import com.AirBnb.Final.Project.Dto.GuestRoomDto;
import com.AirBnb.Final.Project.Dto.OnlyHotelInfoDto;
import com.AirBnb.Final.Project.Dto.OnlyRoomInfoDto;
import com.AirBnb.Final.Project.Entity.Hotel;
import com.AirBnb.Final.Project.Entity.Inventory;
import com.AirBnb.Final.Project.Entity.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

    void deleteByRoom(Room room);

    void deleteByHotel(Hotel hotel);

    List<Inventory> findByCity(String city);

    @Query(
            """
            
            SELECT MAX(r.capacity) FROM Inventory i
            JOIN Room r ON i.room.id=r.id
            
            """
    )
    int getMaximumAdult(Integer adults);


    @Query("""
            
            SELECT DISTINCT new com.AirBnb.Final.Project.Dto.GuestHotelDto(i.hotel.name,i.hotel.city,i.hotel.photos,i.hotel.amenities,i.hotel.contactInfo,MIN(i.price)) FROM Inventory i
            WHERE
            i.city= :city
            AND
            i.date BETWEEN :startDate AND :endDate
            AND
            i.hotel.active= TRUE
            AND
            i.close= FALSE
            GROUP BY i.hotel.name,
                     i.hotel.city,
                     i.hotel.photos,
                     i.hotel.amenities,
                     i.hotel.contactInfo
            """)
    Page<GuestHotelDto> searchHotelInPagination(
            String city,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );


    @Query(
            """
            
            SELECT i FROM Inventory i
            WHERE
            i.hotel.id= :hotelId
            AND
            i.room.id= :roomId
            AND
            i.city= :city
            AND
            i.date BETWEEN :checkInDate AND :checkOutDate
            AND
            i.totalCount-i.bookedCount-i.reservedCount >= :numberOfRooms
            AND
            i.hotel.active= TRUE
            AND
            i.close= FALSE
            
            """
    )
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory> findAndLockInventory(
            Long hotelId,
            Long roomId,
            String city,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            Integer numberOfRooms);

    @Query(
            """
            
            SELECT i FROM Inventory i
            WHERE
            i.hotel.id= :hotelId
            AND
            i.room.id= :roomId
            AND
            i.date BETWEEN :checkInDate AND :checkOutDate
            AND
            i.totalCount-i.bookedCount >= :totalRooms
          
            """
    )
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory> findAndReserveInventory(
            Long hotelId,
            Long roomId,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            int totalRooms);

    @Modifying
    @Query(
            """
            
            UPDATE Inventory i
            SET i.reservedCount=i.reservedCount - :totalRooms,
                i.bookedCount=i.bookedCount + :totalRooms
            WHERE
            i.hotel.id= :hotelId
            AND
            i.room.id= :roomId
            AND
            i.date BETWEEN :checkInDate AND :checkOutDate
            AND
            i.totalCount-i.bookedCount >= :totalRooms
            AND
            i.reservedCount >= :totalRooms
            AND
            i.close= FALSE
            
            """
    )
    void confirmBooking(
            Long hotelId,
            Long roomId,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            int totalRooms
    );


    @Query(
            """
            
            SELECT DISTINCT new com.AirBnb.Final.Project.Dto.OnlyHotelInfoDto(i.hotel,MIN(i.price)) FROM Inventory i
            WHERE
            i.hotel.id= :hotelId
            AND
            i.city= :city
            AND
            i.date BETWEEN :startDate AND :endDate
            AND
            i.hotel.active= TRUE
            AND
            i.close= FALSE
            GROUP BY i.hotel
            
            """
    )
    OnlyHotelInfoDto getOnlyHotelInfo(
            Long hotelId,
            String city,
            LocalDate startDate,
            LocalDate endDate);

    @Query(
            """
            
            SELECT DISTINCT new com.AirBnb.Final.Project.Dto.OnlyRoomInfoDto(i.room,AVG(i.price)) FROM Inventory i
            WHERE
            i.hotel.id= :hotelId
            AND
            i.city= :city
            AND
            i.date BETWEEN :startDate AND :endDate
            AND
            i.close= FALSE
            AND
            i.hotel.active= TRUE
            GROUP BY i.room
            
            """
    )
    List<OnlyRoomInfoDto> getOnlyRoomInfo(
            Long hotelId,
            String city,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query(
            """
            
            SELECT AVG(i.price) FROM Inventory i
            WHERE
            i.hotel.id= :hotelId
            AND
            i.room.id= :roomId
            AND
            i.city= :city
            AND
            i.date BETWEEN :checkInDate AND :checkOutDate
            AND
            i.close= FALSE
            AND
            i.hotel.active= TRUE
            
            """
    )
    BigDecimal getBookingPrice(
            Long hotelId,
            Long roomId,
            LocalDate checkInDate,
            LocalDate checkOutDate,
            String city);


}

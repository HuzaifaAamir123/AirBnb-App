package com.AirBnb.Final.Project.Repository;


import com.AirBnb.Final.Project.Entity.Hotel;
import com.AirBnb.Final.Project.Entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long> {

    Optional<Room> findByName(String lowerCase);

    void deleteByHotel(Hotel hotel);

}

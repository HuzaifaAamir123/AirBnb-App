package com.AirBnb.Final.Project.Repository;


import com.AirBnb.Final.Project.Entity.Hotel;
import com.AirBnb.Final.Project.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Long> {

    Optional<Hotel> findByContactInfo_Email(String lowerCase);

    List<Hotel> findByOwner(User currentAdmin);

}

package com.AirBnb.Final.Project.Repository;

import com.AirBnb.Final.Project.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {


    Optional<Booking> findByCheckOutSessionId(String id);

}

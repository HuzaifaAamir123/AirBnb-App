package com.AirBnb.Final.Project.Repository;

import com.AirBnb.Final.Project.Entity.HotelMinPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface HotelMinPriceRepository extends JpaRepository<HotelMinPrice,Long> {

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE hotel_min_price RESTART IDENTITY",nativeQuery = true)
    void truncateHotelMinPrice();


}

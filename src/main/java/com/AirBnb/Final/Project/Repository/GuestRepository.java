package com.AirBnb.Final.Project.Repository;

import com.AirBnb.Final.Project.Entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<Guest,Long> {



}

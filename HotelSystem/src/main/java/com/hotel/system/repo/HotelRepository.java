package com.hotel.system.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.system.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, String> {

}

package com.hotel.system.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.hotel.system.Exception.ResourceNotFoundException;
import com.hotel.system.entity.Hotel;
import com.hotel.system.repo.HotelRepository;

@Service
public class HotelServiceImpl implements HotelService{

	@Autowired
	private HotelRepository hotelRepo;
	@Override
	public Hotel create(Hotel hotel) {
	
		String hotelId=UUID.randomUUID().toString();
		hotel.setId(hotelId);
		return hotelRepo.save(hotel);
	}

	@Override
	public List<Hotel> getAll() {
	
		return hotelRepo.findAll();
	}

	@Override
	public Hotel getHotelById(String id)
	{
	
		return hotelRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("This data is invalid"));
	}
	

}

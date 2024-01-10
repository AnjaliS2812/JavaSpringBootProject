package com.hotel.system.service;

import java.util.List;

import com.hotel.system.entity.Hotel;

public interface HotelService   {
	public  Hotel create(Hotel hotel);
	public List<Hotel>getAll();
	public Hotel getHotelById(String id);
	

	
}

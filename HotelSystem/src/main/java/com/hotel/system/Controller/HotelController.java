package com.hotel.system.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.system.entity.Hotel;
import com.hotel.system.service.HotelService;

@RestController
@RequestMapping("/Hotel")
public class HotelController {

	@Autowired
	private HotelService hotelService;
	
	@PostMapping
	public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel)
	{
		return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.create(hotel));
		
	}
	
	@GetMapping
	public ResponseEntity<List<Hotel>>getAll()
	{
		return ResponseEntity.status(HttpStatus.OK).body(hotelService.getAll());
		
	}
	@GetMapping("/hotel{hotelId}")
	public ResponseEntity<Hotel>getHotelById(@PathVariable String id)
	{
		return ResponseEntity.status(HttpStatus.OK).body(hotelService.getHotelById(id));
	}
}

package com.hotel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.model.Booking;
import com.hotel.repository.BookingRepository;

@Service
public class BookingService {

	@Autowired
	private BookingRepository repo;

	public void saveBooking(Booking booking) {
		repo.save(booking);
	}

	public List<Booking> getAllBookings() {
		return repo.findAll();
	}

	public void deleteBooking(int roomNo) {
		repo.deleteById(roomNo);
	}

	public boolean isRoomBooked(int roomNo) {
		return repo.existsById(roomNo);
	}

	// 🔥 ADD THIS METHOD
	public Booking getBookingByRoomNo(int roomNo) {
		return repo.findById(roomNo).orElse(null);
	}
}
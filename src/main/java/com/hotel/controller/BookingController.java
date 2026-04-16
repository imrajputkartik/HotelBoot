package com.hotel.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hotel.model.Booking;
import com.hotel.service.BookingService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@GetMapping("/bookRoom")
	public String openForm(@RequestParam(value = "roomNo", required = false) Integer roomNo, Model model) {

		if (roomNo != null) {
			boolean isBooked = bookingService.isRoomBooked(roomNo);

			if (isBooked) {
				model.addAttribute("error", "❌ Room already booked!");
				return "error-page";
			}

			model.addAttribute("roomNo", roomNo);
		}

		return "booking-form";
	}

	@PostMapping("/saveBooking")
	public String saveBooking(@ModelAttribute Booking booking, HttpSession session, Model model) {

		if (booking.getCheckIn().after(booking.getCheckOut()) || booking.getCheckIn().equals(booking.getCheckOut())) {

			model.addAttribute("error", "Check-out must be after Check-in");
			return "booking-form";
		}

		if (booking.getMobile() == null || !booking.getMobile().matches("\\d{10}")) {

			model.addAttribute("error", "Invalid mobile number");
			return "booking-form";
		}

		session.setAttribute("bookingData", booking);
		return "payment";
	}

	@RequestMapping(value="/confirmBooking", method=RequestMethod.POST)
	public String confirmBooking(HttpSession session, Model model) {

		Booking booking = (Booking) session.getAttribute("bookingData");

		if (booking != null) {

			boolean isBooked = bookingService.isRoomBooked(booking.getRoomNo());

			if (isBooked) {
				model.addAttribute("error", "❌ Room already booked!");
				return "room-already-booked";
			}

			bookingService.saveBooking(booking);

			model.addAttribute("booking", booking);
			session.removeAttribute("bookingData");
		}

		return "success";
	}
}
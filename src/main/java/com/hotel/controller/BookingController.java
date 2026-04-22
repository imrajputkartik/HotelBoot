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

	// 🔥 OPEN FORM (room + price dono handle)
	@GetMapping("/bookRoom")
	public String openForm(@RequestParam(value = "roomNo", required = false) Integer roomNo,
			@RequestParam(value = "price", required = false) Integer price, Model model) {

		if (roomNo != null) {
			boolean isBooked = bookingService.isRoomBooked(roomNo);

			if (isBooked) {
				model.addAttribute("error", "❌ Room already booked!");
				return "error-page";
			}

			model.addAttribute("roomNo", roomNo);
		}

		// ✅ PRICE UI ko bhejna (VERY IMPORTANT)
		model.addAttribute("price", price != null ? price : 0);

		return "booking-form";
	}

	// 🔥 SAVE BOOKING (FORM → PAYMENT)
	@PostMapping("/saveBooking")
	public String saveBooking(@ModelAttribute Booking booking, HttpSession session, Model model) {

		// ✅ DATE VALIDATION
		if (booking.getCheckIn().after(booking.getCheckOut()) || booking.getCheckIn().equals(booking.getCheckOut())) {

			model.addAttribute("error", "Check-out must be after Check-in");
			model.addAttribute("price", 0); // UI crash na ho
			return "booking-form";
		}

		// ✅ MOBILE VALIDATION
		if (booking.getMobile() == null || !booking.getMobile().matches("\\d{10}")) {

			model.addAttribute("error", "Invalid mobile number");
			model.addAttribute("price", 0);
			return "booking-form";
		}

		// 🚨 CRITICAL FIX (amount empty issue solve)
		if (booking.getAmount() == 0) {
			model.addAttribute("error", "Amount not calculated properly");
			model.addAttribute("price", 0);
			return "booking-form";
		}

		// ✅ SESSION STORE
		session.setAttribute("bookingData", booking);

		// ✅ PAYMENT PAGE ke liye amount bhej
		model.addAttribute("amount", booking.getAmount());

		return "payment";
	}

	// 🔥 CONFIRM BOOKING
	@PostMapping("/confirmBooking")
	public String confirmBooking(HttpSession session, Model model) {

		Booking booking = (Booking) session.getAttribute("bookingData");

		if (booking != null) {

			boolean isBooked = bookingService.isRoomBooked(booking.getRoomNo());

			if (isBooked) {
				model.addAttribute("error", "❌ Room already booked!");
				return "room-already-book"; // ✅ ensure file name same ho
			}

			bookingService.saveBooking(booking);

			model.addAttribute("booking", booking);

			session.removeAttribute("bookingData");
		}

		return "success";
	}
}
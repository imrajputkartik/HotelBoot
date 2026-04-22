package com.hotel.controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hotel.model.Booking;
import com.hotel.model.GuestHistory;
import com.hotel.service.BookingService;
import com.hotel.repository.GuestHistoryRepository; // 🔥 ADD

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

	@Autowired
	private BookingService bookingService;

	@Autowired
	private GuestHistoryRepository guestHistoryRepo; // 🔥 ADD

	@GetMapping("/dashboard")
	public String dashboard() {
		return "dashboard";
	}

	@GetMapping("/guests")
	public String showGuests(Model model) {
		List<Booking> bookings = bookingService.getAllBookings();
		model.addAttribute("bookings", bookings);
		return "guest-list";
	}

	@GetMapping("/rooms")
	public String rooms(Model model) {

		List<Booking> bookings = bookingService.getAllBookings();

		Set<Integer> bookedRooms = bookings.stream().map(Booking::getRoomNo).collect(Collectors.toSet());

		model.addAttribute("bookedRooms", bookedRooms);

		return "rooms";
	}

	@GetMapping("/services")
	public String services() {
		return "services";
	}

	@GetMapping("/checkout")
	public String checkout(Model model) {
		model.addAttribute("bookings", bookingService.getAllBookings());
		return "checkout";
	}

	// 🔥 UPDATED METHOD
	@GetMapping("/deleteBooking/{roomNo}")
	public String deleteBooking(@PathVariable int roomNo) {

		Booking b = bookingService.getBookingByRoomNo(roomNo);

		if (b != null) {

			GuestHistory g = new GuestHistory();

			g.setRoomNo(b.getRoomNo());
			g.setName(b.getName());
			g.setGender(b.getGender());
			g.setMembers(b.getMembers());
			g.setAddress(b.getAddress());
			g.setState(b.getState());
			g.setNationality(b.getNationality());
			g.setAdharNo(b.getAdharNo());
			g.setMobile(b.getMobile());
			g.setAmount(b.getAmount());

			g.setCheckIn(b.getCheckIn());
			g.setCheckOut(new java.util.Date());

			// 🔥 save in history
			guestHistoryRepo.save(g);
		}

		// 🔥 delete booking
		bookingService.deleteBooking(roomNo);

		// ✅ BACK TO CHECKOUT PAGE
		return "redirect:/checkout";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@GetMapping("/guestRecord")
	public String guestRecord(Model model) {

		List<GuestHistory> list = guestHistoryRepo.findAll();

		System.out.println("History Data: " + list); // 🔥 debug

		model.addAttribute("records", list);

		return "guest-record";
	}
}

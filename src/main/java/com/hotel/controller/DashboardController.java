package com.hotel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hotel.model.Booking;
import com.hotel.service.BookingService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @Autowired
    private BookingService bookingService;

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
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "rooms";
    }

    @GetMapping("/services")
    public String services() {
        return "services";
    }

    @GetMapping("/form")
    public String form() {
        return "form";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "checkout";
    }

    @GetMapping("/deleteBooking/{roomNo}")
    public String deleteBooking(@PathVariable int roomNo) {
        bookingService.deleteBooking(roomNo);
        return "redirect:/checkout";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
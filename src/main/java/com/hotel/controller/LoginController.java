package com.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hotel.model.User;
import com.hotel.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String loginPage() {
		return "login";
	}

	@PostMapping("/doLogin")
	public String doLogin(@RequestParam String username, @RequestParam String password, Model model,
			HttpSession session) {

		User user = userService.login(username, password);

		if (user != null) {
			session.setAttribute("user", user); // ✅ session store
			return "redirect:/dashboard";
		} else {
			model.addAttribute("error", "Invalid username or password");
			return "login";
		}
	}
}
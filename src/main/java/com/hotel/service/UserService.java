package com.hotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.model.User;
import com.hotel.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;

	public User login(String username, String password) {
		return repo.findByUsernameAndPassword(username, password);
	}
}
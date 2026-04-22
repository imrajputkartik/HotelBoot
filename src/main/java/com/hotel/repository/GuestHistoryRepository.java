package com.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hotel.model.GuestHistory;

public interface GuestHistoryRepository extends JpaRepository<GuestHistory, Integer> {

}
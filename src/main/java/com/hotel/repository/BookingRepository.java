package com.hotel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hotel.model.Booking;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByRoomNo(int roomNo);

}
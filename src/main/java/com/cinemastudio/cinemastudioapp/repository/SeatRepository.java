package com.cinemastudio.cinemastudioapp.repository;

import com.cinemastudio.cinemastudioapp.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, String> {
}

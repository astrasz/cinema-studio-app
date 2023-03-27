package com.cinemastudio.cinemastudioapp.repository;

import com.cinemastudio.cinemastudioapp.model.Hall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HallRepository extends JpaRepository<Hall, String> {
}

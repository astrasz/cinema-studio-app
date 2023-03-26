package com.cinemastudio.cinemastudioapp.repository;

import com.cinemastudio.cinemastudioapp.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, String> {
}

package com.cinemastudio.cinemastudioapp.repository;

import com.cinemastudio.cinemastudioapp.model.Chair;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChairRepository extends JpaRepository<Chair, String> {
}

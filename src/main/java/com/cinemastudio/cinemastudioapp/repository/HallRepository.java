package com.cinemastudio.cinemastudioapp.repository;

import com.cinemastudio.cinemastudioapp.model.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HallRepository extends JpaRepository<Hall, String> {
    @Query("select h from Hall h where h.id in :ids")
    List<Hall> findAllByIds(@Param("ids") List<String> ids);

}

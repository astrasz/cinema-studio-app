package com.cinemastudio.cinemastudioapp.repository;

import com.cinemastudio.cinemastudioapp.model.Movie;
import com.cinemastudio.cinemastudioapp.model.ShowTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowTimeRepository extends JpaRepository<ShowTime, String> {

    @Query("select s from ShowTime s where s.date in :dates")
    List<ShowTime> findAllByDate(@Param("dates") List<Date> dates);

    @Query("select s from ShowTime s where s.date = :date and s.movie = :movie")
    Optional<ShowTime> findByDateAndMovieId(@Param("date") Date date, @Param("movie") Movie movie);
}

package com.cinemastudio.cinemastudioapp.service;

import com.cinemastudio.cinemastudioapp.dto.MovieResponse;
import com.cinemastudio.cinemastudioapp.model.Movie;
import com.cinemastudio.cinemastudioapp.model.ShowTime;
import com.cinemastudio.cinemastudioapp.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {


    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<MovieResponse> getAll() {
        List<Movie> movieList = movieRepository.findAll();
        return movieList.stream().map(this::mapToMovieResponse).toList();
    }

    private MovieResponse mapToMovieResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .minutes(movie.getMinutes())
                .director(movie.getDirector())
                .country(movie.getCountry())
                .premiere(movie.getPremiere())
                .showTimes(movie.getShowTimes().stream().map(ShowTime::getDate).toList())
                .build();
    }
}

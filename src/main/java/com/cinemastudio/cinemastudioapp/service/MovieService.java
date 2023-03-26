package com.cinemastudio.cinemastudioapp.service;

import com.cinemastudio.cinemastudioapp.dto.MovieRequest;
import com.cinemastudio.cinemastudioapp.dto.MovieResponse;
import com.cinemastudio.cinemastudioapp.model.Movie;
import com.cinemastudio.cinemastudioapp.model.ShowTime;
import com.cinemastudio.cinemastudioapp.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class MovieService {


    private final MovieRepository movieRepository;
    private final ObjectMapper mapper;

    @Autowired
    public MovieService(MovieRepository movieRepository, ObjectMapper mapper) {
        this.movieRepository = movieRepository;
        this.mapper = mapper;
    }

    public List<MovieResponse> getAll() {
        List<Movie> movieList = movieRepository.findAll();
        return movieList.stream().map(this::mapToMovieResponse).toList();
    }

    public MovieResponse getOneById(String movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new NoSuchElementException("Movie cannot be found"));

        System.out.println(movie.toString());

        return mapper.convertValue(movie, MovieResponse.class);
    }

    public void create(MovieRequest movieRequest) {
        Movie movie = Movie.builder()
                .title(movieRequest.getTitle())
                .country(movieRequest.getCountry())
                .director(movieRequest.getDirector())
                .minutes(movieRequest.getMinutes())
                .premiere(movieRequest.getPremiere())
                .build();

        movieRepository.save(movie);
        log.info("Movie {} has been saved successfully", movie.getTitle());
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

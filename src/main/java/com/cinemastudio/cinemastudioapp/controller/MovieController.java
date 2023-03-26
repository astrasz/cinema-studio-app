package com.cinemastudio.cinemastudioapp.controller;

import com.cinemastudio.cinemastudioapp.dto.MovieResponse;
import com.cinemastudio.cinemastudioapp.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
public class MovieController implements ApiController{

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    @Override
    public List<MovieResponse> getAll() {
        return movieService.getAll();
    }

    @Override
    public Optional<MovieResponse> getOneById() {
        return null;
    }

    @Override
    public Optional<MovieResponse> update() {
        return null;
    }

    @Override
    public void remove() {

    }
}

package com.cinemastudio.cinemastudioapp.controller;

import com.cinemastudio.cinemastudioapp.dto.MovieRequest;
import com.cinemastudio.cinemastudioapp.dto.MovieResponse;
import com.cinemastudio.cinemastudioapp.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
public class MovieController implements ApiController<MovieRequest, MovieResponse> {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<List<MovieResponse>> getAll() {
        return ResponseEntity.ok(movieService.getAll());
    }

    @GetMapping("/{movieId}")
    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<MovieResponse> getOneById(@PathVariable final String movieId) {
        return ResponseEntity.ok(movieService.getOneById(movieId));
    }

    @Override
    public ResponseEntity<MovieResponse> update() {
        return null;
    }

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public void create(@Valid @RequestBody final MovieRequest movieRequest) {
        movieService.create(movieRequest);
    }

    @Override
    public void remove() {

    }
}

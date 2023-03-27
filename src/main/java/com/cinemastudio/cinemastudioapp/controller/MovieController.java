package com.cinemastudio.cinemastudioapp.controller;

import com.cinemastudio.cinemastudioapp.dto.MovieRequest;
import com.cinemastudio.cinemastudioapp.dto.MovieResponse;
import com.cinemastudio.cinemastudioapp.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController implements ApiController<MovieRequest, MovieResponse> {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    @Override
    public ResponseEntity<List<MovieResponse>> getAll() {
        return ResponseEntity.ok(movieService.getAll());
    }

    @GetMapping
    @Override
    public ResponseEntity<List<MovieResponse>> getAll(Integer pageNr, Integer number, String sortBy, String sortDir) {
        return null;
    }

    @GetMapping("/{movieId}")
    @Override
    public ResponseEntity<MovieResponse> getOneById(@PathVariable final String movieId) {
        return ResponseEntity.ok(movieService.getOneById(movieId));
    }

    @PutMapping("/{movieId}")
    @Override
    public ResponseEntity<MovieResponse> update(@PathVariable final String movieId, @RequestBody final MovieRequest movieRequest) {
        return ResponseEntity.ok(movieService.update(movieId, movieRequest));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public ResponseEntity<MovieResponse> create(@Valid @RequestBody final MovieRequest movieRequest) {
        return new ResponseEntity<>(movieService.create(movieRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/{movieId}")
    @Override
    public ResponseEntity<String> remove(@PathVariable final String movieId) {
        return ResponseEntity.ok(movieService.remove(movieId));
    }
}

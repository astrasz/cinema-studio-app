package com.cinemastudio.cinemastudioapp.controller;

import com.cinemastudio.cinemastudioapp.dto.MovieRequest;
import com.cinemastudio.cinemastudioapp.dto.MovieResponse;
import com.cinemastudio.cinemastudioapp.service.impl.MovieServiceImpl;
import com.cinemastudio.cinemastudioapp.util.ApiConstants;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@Slf4j
public class MovieController implements ApiController<MovieRequest, MovieResponse> {

    private final MovieServiceImpl movieServiceImpl;

    @Autowired
    public MovieController(MovieServiceImpl movieServiceImpl) {
        this.movieServiceImpl = movieServiceImpl;
    }

    @GetMapping
    @Override
    public ResponseEntity<List<MovieResponse>> getAll(
            @RequestParam(value = "pageNr", defaultValue = ApiConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNr,
            @RequestParam(value = "number", defaultValue = ApiConstants.DEFAULT_LIMIT_QUERY, required = false) Integer number,
            @RequestParam(value = "sortBy", defaultValue = ApiConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ApiConstants.DEFAULT_SORT_DIR, required = false) String sortDir
    ) {
        return ResponseEntity.ok(movieServiceImpl.getAll(pageNr, number, sortBy, sortDir));
    }


    @GetMapping("/{movieId}")
    @Override
    public ResponseEntity<MovieResponse> getOneById(@PathVariable final String movieId) {
        return ResponseEntity.ok(movieServiceImpl.getOneById(movieId));
    }

    @PutMapping("/{movieId}")
    @Override
    public ResponseEntity<MovieResponse> update(@PathVariable final String movieId, @Valid @RequestBody final MovieRequest movieRequest) {
        return ResponseEntity.ok(movieServiceImpl.update(movieId, movieRequest));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public ResponseEntity<MovieResponse> create(@Valid @RequestBody final MovieRequest movieRequest) {
        return new ResponseEntity<>(movieServiceImpl.create(movieRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/{movieId}")
    @Override
    public ResponseEntity<String> remove(@PathVariable final String movieId) {
        return ResponseEntity.ok(movieServiceImpl.remove(movieId));
    }
}

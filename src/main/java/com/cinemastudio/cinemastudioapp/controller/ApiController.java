package com.cinemastudio.cinemastudioapp.controller;


import com.cinemastudio.cinemastudioapp.dto.MovieRequest;
import com.cinemastudio.cinemastudioapp.dto.MovieResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

public interface ApiController<T, V> {
    ResponseEntity<List<V>> getAll();

    ResponseEntity<V> getOneById(@PathVariable final String id);

    ResponseEntity<V> update();

    void create(@RequestBody final T request);

    void remove();
}

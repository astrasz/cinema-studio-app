package com.cinemastudio.cinemastudioapp.controller;


import com.cinemastudio.cinemastudioapp.dto.MovieRequest;
import com.cinemastudio.cinemastudioapp.dto.MovieResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface ApiController<T, V> {
//    ResponseEntity<List<V>> getAll();

    ResponseEntity<List<V>> getAll(@RequestParam Integer pageNr,
                                   @RequestParam Integer number,
                                   @RequestParam String sortBy,
                                   @RequestParam String sortDir);


    ResponseEntity<V> getOneById(@PathVariable final String id);

    ResponseEntity<V> update(@PathVariable final String id, @RequestBody final T request);

    ResponseEntity<V> create(@RequestBody final T request);

    ResponseEntity<String> remove(@PathVariable final String id);
}

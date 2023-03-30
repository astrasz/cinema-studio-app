package com.cinemastudio.cinemastudioapp.service;

import com.cinemastudio.cinemastudioapp.dto.MovieRequest;
import com.cinemastudio.cinemastudioapp.dto.MovieResponse;

import java.util.List;

public interface MovieService {
    List<MovieResponse> getAll(Integer pageNr, Integer number, String sortBy, String sortDir);

    MovieResponse getOneById(String movieId);

    MovieResponse create(MovieRequest movieRequest);

    MovieResponse update(String movieId, MovieRequest movieRequest);

    String remove(String movieId);
}

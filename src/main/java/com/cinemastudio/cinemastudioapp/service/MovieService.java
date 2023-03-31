package com.cinemastudio.cinemastudioapp.service;

import com.cinemastudio.cinemastudioapp.dto.request.MovieRequest;
import com.cinemastudio.cinemastudioapp.dto.response.MovieResponse;

import java.util.List;

public interface MovieService {
    List<MovieResponse> getAll(Integer pageNr, Integer number, String sortBy, String sortDir);

    MovieResponse getOneById(String movieId);

    MovieResponse create(MovieRequest movieRequest);

    MovieResponse update(String movieId, MovieRequest movieRequest);

    String remove(String movieId);
}

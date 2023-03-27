package com.cinemastudio.cinemastudioapp.service;

import com.cinemastudio.cinemastudioapp.dto.MovieRequest;
import com.cinemastudio.cinemastudioapp.dto.MovieResponse;
import com.cinemastudio.cinemastudioapp.exception.ResourceNofFoundException;
import com.cinemastudio.cinemastudioapp.model.Movie;
import com.cinemastudio.cinemastudioapp.model.ShowTime;
import com.cinemastudio.cinemastudioapp.repository.MovieRepository;
import com.cinemastudio.cinemastudioapp.repository.ShowTimeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MovieService {


    private final MovieRepository movieRepository;
    private final ShowTimeRepository showTimeRepository;
    private final ObjectMapper mapper;

    @Autowired
    public MovieService(MovieRepository movieRepository, ObjectMapper mapper, ShowTimeRepository showTimeRepository) {
        this.movieRepository = movieRepository;
        this.mapper = mapper;
        this.showTimeRepository = showTimeRepository;
    }

    @Transactional(readOnly = true)
    public List<MovieResponse> getAll(Integer pageNr, Integer number, String sortBy, String sortDir) {

        int page = pageNr != null ? pageNr : 1;
        int limit = number != null ? number : 10;
        String by = sortBy != null ? sortBy : "date";
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(by).ascending() : Sort.by(by).descending();

        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Movie> showTimes = movieRepository.findAll(pageable);
        
        List<Movie> movieList = showTimes.getContent();
        return movieList.stream().map(this::mapToMovieResponse).toList();
    }

    @Transactional(readOnly = true)
    public MovieResponse getOneById(String movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new ResourceNofFoundException(Movie.class.getSimpleName(), "id", movieId));

        System.out.println(movie.toString());

        return mapper.convertValue(movie, MovieResponse.class);
    }

    @Transactional
    public MovieResponse create(MovieRequest movieRequest) {
        Movie movie = Movie.builder()
                .title(movieRequest.getTitle())
                .country(movieRequest.getCountry())
                .director(movieRequest.getDirector())
                .minutes(movieRequest.getMinutes())
                .premiere(movieRequest.getPremiere())
                .build();

        movieRepository.save(movie);
        log.info("Movie {} has been saved successfully", movie.getTitle());
        return mapToMovieResponse(movie);
    }

    @Transactional
    public MovieResponse update(String id, MovieRequest movieRequest) {

        Movie movie = movieRepository.findById(id).orElseThrow(() -> new ResourceNofFoundException(Movie.class.getSimpleName(), "id", id));


        movie.setTitle(movieRequest.getTitle());
        movie.setDirector(movieRequest.getDirector());
        movie.setCountry(movieRequest.getCountry());
        movie.setMinutes(movieRequest.getMinutes());
        movie.setPremiere(movieRequest.getPremiere());

        Movie updatedShowTimeMovie = updateShowTimes(movie, movieRequest);

        movieRepository.save(updatedShowTimeMovie);

        return mapToMovieResponse(updatedShowTimeMovie);
    }

    @Transactional
    public String remove(String id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new ResourceNofFoundException(Movie.class.getSimpleName(), "id", id));

        String title = movie.getTitle();

        movieRepository.delete(movie);
        return String.format("Movie %s has been deleted successfully", title);
    }

    private Movie updateShowTimes(Movie movie, MovieRequest movieRequest) {
        List<ShowTime> showTimeList = movie.getShowTimes();
        List<Date> showTimeDates = showTimeList.stream().map(ShowTime::getDate).toList();
        List<Date> movieRequestShowTimeDates = movieRequest.getShowTimes();

        if (showTimeDates.size() > 0 || movieRequestShowTimeDates.size() > 0) {
            boolean shouldShowTimesUpdate = false;
            for (Date date : showTimeDates) {
                if (!movieRequestShowTimeDates.contains(date)) {
                    shouldShowTimesUpdate = true;
                    break;
                }
            }

            for (Date mrDate : movieRequestShowTimeDates) {
                if (!showTimeDates.contains(mrDate)) {
                    shouldShowTimesUpdate = true;
                    break;
                }
            }

            if (shouldShowTimesUpdate) {
                List<ShowTime> newShowTimeList = showTimeRepository.findAllByDate(movieRequestShowTimeDates);
                movie.setShowTimes(newShowTimeList);
            }
        }

        return movie;
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

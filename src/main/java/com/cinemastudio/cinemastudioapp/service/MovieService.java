package com.cinemastudio.cinemastudioapp.service;

import com.cinemastudio.cinemastudioapp.dto.MovieRequest;
import com.cinemastudio.cinemastudioapp.dto.MovieResponse;
import com.cinemastudio.cinemastudioapp.exception.DuplicatedEntityException;
import com.cinemastudio.cinemastudioapp.exception.InvalidRequestParameterException;
import com.cinemastudio.cinemastudioapp.exception.ResourceNofFoundException;
import com.cinemastudio.cinemastudioapp.model.Movie;
import com.cinemastudio.cinemastudioapp.model.ShowTime;
import com.cinemastudio.cinemastudioapp.repository.MovieRepository;
import com.cinemastudio.cinemastudioapp.repository.ShowTimeRepository;
import com.cinemastudio.cinemastudioapp.util.ApiConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

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

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNr, number, sort);
        Page<Movie> showTimes = movieRepository.findAll(pageable);

        List<Movie> movieList = showTimes.getContent();
        return movieList.stream().map(this::mapToMovieResponse).toList();
    }

    @Transactional(readOnly = true)
    public MovieResponse getOneById(String movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new ResourceNofFoundException(Movie.class.getSimpleName(), "id", movieId));
        return mapper.convertValue(movie, MovieResponse.class);
    }

    @Transactional
    public MovieResponse create(MovieRequest movieRequest) {

        Map<String, String> propertiesForBuilder = checkAndReturnValuesForBuilder(movieRequest);

        try {
            Movie movie = Movie.builder()
                    .title(propertiesForBuilder.get("title"))
                    .country(movieRequest.getCountry())
                    .director(propertiesForBuilder.get("director"))
                    .minutes(movieRequest.getMinutes())
                    .premiere(ApiConstants.DEFAULT_DATE_FORMATTER.parse(movieRequest.getPremiere()))
                    .build();

            movieRepository.save(movie);
            return mapToMovieResponse(movie);
        } catch (ParseException exception) {
            throw new InvalidRequestParameterException("premiere", movieRequest.getPremiere());
        }
    }

    private Map<String, String> checkAndReturnValuesForBuilder(MovieRequest movieRequest) {
        Optional<Movie> savedMovie = movieRepository.findByTitleAndDirector(movieRequest.getTitle(), movieRequest.getTitle());
        if (savedMovie.isPresent()) {
            throw new DuplicatedEntityException(Movie.class.getSimpleName());
        }

        Map<String, String> properties = new HashMap<>();
        properties.put("title", movieRequest.getTitle());
        properties.put("director", movieRequest.getDirector());
        return properties;
    }

    @Transactional
    public MovieResponse update(String id, MovieRequest movieRequest) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new ResourceNofFoundException(Movie.class.getSimpleName(), "id", id));

        movie.setTitle(movieRequest.getTitle());
        movie.setDirector(movieRequest.getDirector());
        movie.setCountry(movieRequest.getCountry());
        movie.setMinutes(movieRequest.getMinutes());
        if (!Objects.equals(movieRequest.getPremiere(), "")) {
            try {
                movie.setPremiere(ApiConstants.DEFAULT_DATE_FORMATTER.parse(movieRequest.getPremiere()));

            } catch (ParseException exception) {
                MovieResponse movieResponse = mapToMovieResponse(movie);
                throw new InvalidRequestParameterException("date", Arrays.toString(movieResponse.getShowTimes().toArray()));
            }
        }
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

        Optional<Map<Integer, List<Timestamp>>> showTimesDatesLists = returnMapOfDatesListOrNothingToUpdate(movie, movieRequest);
        if (showTimesDatesLists != null && showTimesDatesLists.isPresent()) {
            List<Timestamp> showTimeDates = showTimesDatesLists.get().get(1);
            List<Timestamp> movieRequestShowTimeDates = showTimesDatesLists.get().get(2);

            Map<Integer, List<Date>> datesListsToUpdate = prepareShowTimesDatesListsToUpdate(showTimeDates, movieRequestShowTimeDates);
            return saveNewShowTimesList(movie, datesListsToUpdate.get(1), datesListsToUpdate.get(2));
        }
        return movie;
    }

    private Map<Integer, List<Date>> prepareShowTimesDatesListsToUpdate(List<Timestamp> showTimeDates, List<Timestamp> movieRequestShowTimeDates) {

        List<Date> showTimeDatesToRemove = new ArrayList<Date>();
        List<Date> showTimesDatesToAdd = new ArrayList<Date>();

        for (Date date : showTimeDates) {
            if (!movieRequestShowTimeDates.contains(date)) {
                showTimeDatesToRemove.add(date);
            }
        }

        for (Date mrDate : movieRequestShowTimeDates) {
            if (!showTimeDates.contains(mrDate)) {
                showTimesDatesToAdd.add(mrDate);
            }
        }
        HashMap<Integer, List<Date>> data = new HashMap<Integer, List<Date>>();
        data.put(1, showTimeDatesToRemove);
        data.put(2, showTimesDatesToAdd);

        return data;
    }

    private Movie saveNewShowTimesList(Movie movie, List<Date> showTimeDatesToRemove, List<Date> showTimesDatesToAdd) {
        List<ShowTime> showTimesToRemove = new ArrayList<>();

        movie.getShowTimes().forEach(showTime ->
        {
            if (showTimeDatesToRemove.contains(showTime.getDate())) {
                showTimesToRemove.add(showTime);
            }
        });
        for (ShowTime showTime : showTimesToRemove) {
            movie.getShowTimes().remove(showTime);
            showTimeRepository.delete(showTime);
        }

        for (Date dateToAdd : showTimesDatesToAdd) {
            ShowTime showTime = ShowTime.builder().date(dateToAdd).movie(movie).build();
            movie.getShowTimes().add(showTime);
            movieRepository.save(movie);
        }
        movieRepository.save(movie);
        return movie;
    }

    private Optional<Map<Integer, List<Timestamp>>> returnMapOfDatesListOrNothingToUpdate(Movie movie, MovieRequest movieRequest) {

        List<ShowTime> showTimeList = movie.getShowTimes();
        List<Timestamp> showTimeDates = showTimeList != null && !showTimeList.isEmpty() ? showTimeList.stream().map(showTime -> {
            return new Timestamp(showTime.getDate().getTime());
        }).toList() : new ArrayList<>();
        List<Timestamp> movieRequestShowTimeDates = movieRequest.getShowTimes() != null && !movieRequest.getShowTimes().isEmpty()
                ? convertListStringsToListTimestamps(movieRequest.getShowTimes())
                : new ArrayList<>();

        if (showTimeDates.size() > 0 || movieRequestShowTimeDates.size() > 0) {
            Map<Integer, List<Timestamp>> data = new HashMap<Integer, List<Timestamp>>();
            data.put(1, showTimeDates);
            data.put(2, movieRequestShowTimeDates);
            return Optional.of(data);
        }
        return Optional.empty();
    }

    private List<Timestamp> convertListStringsToListTimestamps(List<String> listDates) {
        return listDates.stream().map(date -> {
            try {
                return new Timestamp(ApiConstants.DEFAULT_DATE_FORMATTER.parse(date).getTime());
            } catch (ParseException e) {
                throw new InvalidRequestParameterException("date", Arrays.toString(listDates.toArray()));
            }
        }).toList();
    }

    private MovieResponse mapToMovieResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .minutes(movie.getMinutes())
                .director(movie.getDirector())
                .country(movie.getCountry())
                .premiere(movie.getPremiere())
                .showTimes(movie.getShowTimes() != null && !movie.getShowTimes().isEmpty()
                        ? movie.getShowTimes().stream().map(showTime -> {
                    return ApiConstants.DEFAULT_DATE_FORMATTER.format(showTime.getDate());
                }).toList()
                        : new ArrayList<String>())
                .build();
    }
}

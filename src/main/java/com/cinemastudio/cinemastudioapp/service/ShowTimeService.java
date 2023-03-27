package com.cinemastudio.cinemastudioapp.service;

import com.cinemastudio.cinemastudioapp.dto.ShowTimeRequest;
import com.cinemastudio.cinemastudioapp.dto.ShowTimeResponse;
import com.cinemastudio.cinemastudioapp.exception.ResourceNofFoundException;
import com.cinemastudio.cinemastudioapp.model.Movie;
import com.cinemastudio.cinemastudioapp.model.ShowTime;
import com.cinemastudio.cinemastudioapp.repository.MovieRepository;
import com.cinemastudio.cinemastudioapp.repository.ShowTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;


@Service
public class ShowTimeService {

    @Autowired
    private final ShowTimeRepository showTimeRepository;

    @Autowired
    private final MovieRepository movieRepository;

    @Autowired
    public ShowTimeService(ShowTimeRepository showTimeRepository, MovieRepository movieRepository) {
        this.showTimeRepository = showTimeRepository;
        this.movieRepository = movieRepository;
    }

    @Transactional(readOnly = true)
    public List<ShowTimeResponse> getAll(Integer pageNr, Integer number, String sortBy, String sortDir) {

        int page = pageNr != null ? pageNr : 1;
        int limit = number != null ? number : 10;
        String by = sortBy != null ? sortBy : "date";
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(by).ascending() : Sort.by(by).descending();

        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<ShowTime> showTimes = showTimeRepository.findAll(pageable);
        List<ShowTime> showTimeList = showTimes.getContent();

        return showTimeList.stream().map(this::mapToShowTimeResponse).toList();
    }

    @Transactional(readOnly = true)
    public ShowTimeResponse getOneById(String id) {
        ShowTime showTime = showTimeRepository.findById(id).orElseThrow(() -> new ResourceNofFoundException(ShowTime.class.getSimpleName(), "id", id));

        return mapToShowTimeResponse(showTime);
    }

    @Transactional
    public ShowTimeResponse create(ShowTimeRequest showTimeRequest) {
        ShowTime showTime = ShowTime.builder()
                .date(showTimeRequest.getDate())
                .build();
        if (showTimeRequest.getMovieId() != null) {
            Movie movie = movieRepository.findById(showTimeRequest.getMovieId()).orElseThrow(() -> new ResourceNofFoundException(Movie.class.getSimpleName(), "id", showTimeRequest.getMovieId()));
            showTime.setMovie(movie);
        }

        showTimeRepository.save(showTime);
        return mapToShowTimeResponse(showTime);
    }

    @Transactional
    public String remove(String id) {
        ShowTime showTime = showTimeRepository.findById(id).orElseThrow(() -> new ResourceNofFoundException(ShowTime.class.getSimpleName(), "id", id));

        showTimeRepository.delete(showTime);
        return String.format("Cinema show time %s has been deleted successfully", showTime.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

    }

    private ShowTimeResponse mapToShowTimeResponse(ShowTime showTime) {
        return ShowTimeResponse.builder()
                .id(showTime.getId())
                .date(showTime.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .movie(showTime.getMovie())
                .seats(showTime.getSeats())
                .build();
    }


}

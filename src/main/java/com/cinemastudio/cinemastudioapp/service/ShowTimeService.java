package com.cinemastudio.cinemastudioapp.service;

import com.cinemastudio.cinemastudioapp.dto.SeatRequest;
import com.cinemastudio.cinemastudioapp.dto.ShowTimeRequest;
import com.cinemastudio.cinemastudioapp.dto.ShowTimeResponse;
import com.cinemastudio.cinemastudioapp.exception.DuplicatedEntityException;
import com.cinemastudio.cinemastudioapp.exception.InvalidRequestParameterException;
import com.cinemastudio.cinemastudioapp.exception.ResourceNofFoundException;
import com.cinemastudio.cinemastudioapp.model.*;
import com.cinemastudio.cinemastudioapp.repository.MovieRepository;
import com.cinemastudio.cinemastudioapp.repository.ShowTimeRepository;
import com.cinemastudio.cinemastudioapp.util.ApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


@Service
public class ShowTimeService {

    @Autowired
    private final ShowTimeRepository showTimeRepository;

    @Autowired
    private final MovieRepository movieRepository;

    @Autowired
    private final SeatService seatService;

    @Autowired
    public ShowTimeService(ShowTimeRepository showTimeRepository, MovieRepository movieRepository, SeatService seatService) {
        this.showTimeRepository = showTimeRepository;
        this.movieRepository = movieRepository;
        this.seatService = seatService;
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
        Map<String, Object> propertiesForShowTimeBuilder = checkAndReturnValuesForBuilder(showTimeRequest);

        ShowTime showTime = ShowTime.builder()
                .date((Date) propertiesForShowTimeBuilder.get(Date.class.getSimpleName()))
                .movie((Movie) propertiesForShowTimeBuilder.get(Movie.class.getSimpleName()))
                .build();

        showTimeRepository.save(showTime);
        return mapToShowTimeResponse(showTime);
    }

    @Transactional
    public String remove(String id) {
        ShowTime showTime = showTimeRepository.findById(id).orElseThrow(() -> new ResourceNofFoundException(ShowTime.class.getSimpleName(), "id", id));
        LocalDate showTimeDate = showTime.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        showTimeRepository.delete(showTime);
        return String.format("Cinema show time %s has been deleted successfully", showTimeDate);
    }

    @Transactional
    public ShowTimeResponse setSeats(SeatRequest seatRequest, String showTimeId) {
        ShowTime showTime = showTimeRepository.findById(showTimeId).orElseThrow(() -> new ResourceNofFoundException(ShowTime.class.getSimpleName(), "id", showTimeId));

        ShowTime showTimeWithAudience = seatService.createMany(seatRequest, showTime);
        return mapToShowTimeResponse(showTimeWithAudience);
    }

    private Map<String, Object> checkAndReturnValuesForBuilder(ShowTimeRequest showTimeRequest) {
        Date showTimeDate = convertStringDateToDate(showTimeRequest.getDate());

        Movie movie = movieRepository.findById(showTimeRequest.getMovieId()).orElseThrow(() -> new ResourceNofFoundException(Movie.class.getSimpleName(), "id", showTimeRequest.getMovieId()));

        Optional<ShowTime> savedShowTimeResult = showTimeRepository.findByDateAndMovieId(showTimeDate, movie);
        if (savedShowTimeResult.isPresent()) {
            throw new DuplicatedEntityException(ShowTime.class.getSimpleName());
        }

        Map<String, Object> properties = new HashMap<>();
        properties.put(showTimeDate.getClass().getSimpleName(), showTimeDate);
        properties.put(movie.getClass().getSimpleName(), movie);
        return properties;
    }

    private Date convertStringDateToDate(String date) {
        Date convertedDate;
        try {
            convertedDate = ApiConstants.DEFAULT_DATE_FORMATTER.parse(date);
        } catch (ParseException exception) {
            throw new InvalidRequestParameterException("date", date);
        }
        return convertedDate;
    }

    private ShowTimeResponse mapToShowTimeResponse(ShowTime showTime) {

        List<Map<String, String>> seats;
        if (showTime.getSeats() != null) {
            seats = showTime.getSeats().stream().map(seat -> {
                Map<String, String> seatMap = new HashMap<String, String>();
                seatMap.put("hall", seat.getHall().getName());
                seatMap.put("row", String.valueOf(seat.getRow().getNumber()));
                seatMap.put("chair", String.valueOf(seat.getChair().getNumber()));
                return seatMap;
            }).toList();
        } else {
            seats = new ArrayList<>();
        }

        return ShowTimeResponse.builder()
                .id(showTime.getId())
                .date(ApiConstants.DEFAULT_DATE_FORMATTER.format(showTime.getDate()))
                .movieTitle(showTime.getMovie().getTitle())
                .seats(seats)
                .build();
    }
}

package com.cinemastudio.cinemastudioapp.service.impl;

import com.cinemastudio.cinemastudioapp.dto.request.SeatRequest;
import com.cinemastudio.cinemastudioapp.dto.request.ShowTimeRequest;
import com.cinemastudio.cinemastudioapp.dto.response.ShowTimeResponse;
import com.cinemastudio.cinemastudioapp.exception.DuplicatedEntityException;
import com.cinemastudio.cinemastudioapp.exception.InvalidRequestParameterException;
import com.cinemastudio.cinemastudioapp.exception.ResourceNofFoundException;
import com.cinemastudio.cinemastudioapp.model.*;
import com.cinemastudio.cinemastudioapp.repository.MovieRepository;
import com.cinemastudio.cinemastudioapp.repository.ShowTimeRepository;
import com.cinemastudio.cinemastudioapp.repository.TicketTypeRepository;
import com.cinemastudio.cinemastudioapp.service.ShowTimeService;
import com.cinemastudio.cinemastudioapp.util.ApiConstants;
import com.cinemastudio.cinemastudioapp.util.TicketsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class ShowTimeServiceImpl implements ShowTimeService {

    private final ShowTimeRepository showTimeRepository;

    private final MovieRepository movieRepository;

    private final SeatServiceImpl seatServiceImpl;

    private final TicketTypeRepository ticketTypeRepository;

    @Autowired
    public ShowTimeServiceImpl(ShowTimeRepository showTimeRepository, MovieRepository movieRepository,
                               SeatServiceImpl seatServiceImpl, TicketTypeRepository ticketTypeRepository) {
        this.showTimeRepository = showTimeRepository;
        this.movieRepository = movieRepository;
        this.seatServiceImpl = seatServiceImpl;
        this.ticketTypeRepository = ticketTypeRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ShowTimeResponse> getAll(Integer pageNr, Integer number, String sortBy, String sortDir) {

        int page = pageNr != null ? pageNr : 1;
        int limit = number != null ? number : 10;
        String by = sortBy != null ? sortBy : "date";
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(by).ascending()
                : Sort.by(by).descending();

        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<ShowTime> showTimes = showTimeRepository.findAll(pageable);
        List<ShowTime> showTimeList = showTimes.getContent();

        return showTimeList.stream().map(this::mapToShowTimeResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public ShowTimeResponse getOneById(String id) {
        ShowTime showTime = showTimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNofFoundException(ShowTime.class.getSimpleName(), "id", id));
        return mapToShowTimeResponse(showTime);
    }

    @Transactional
    @Override
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
    @Override
    public String remove(String id) {
        ShowTime showTime = showTimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNofFoundException(ShowTime.class.getSimpleName(), "id", id));
        LocalDate showTimeDate = showTime.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        showTimeRepository.delete(showTime);
        return String.format("Cinema show time %s has been deleted successfully", showTimeDate);
    }

    @Transactional
    @Override
    public ShowTimeResponse setSeats(SeatRequest seatRequest, String showTimeId) {
        ShowTime showTime = showTimeRepository.findById(showTimeId)
                .orElseThrow(() -> new ResourceNofFoundException(ShowTime.class.getSimpleName(), "id", showTimeId));

        ShowTime showTimeWithAudience = seatServiceImpl.createMany(seatRequest, showTime);
        return mapToShowTimeResponse(showTimeWithAudience);
    }

    private Map<String, Object> checkAndReturnValuesForBuilder(ShowTimeRequest showTimeRequest) {
        Date showTimeDate = convertStringDateToDate(showTimeRequest.getDate());

        Movie movie = movieRepository.findById(showTimeRequest.getMovieId()).orElseThrow(
                () -> new ResourceNofFoundException(Movie.class.getSimpleName(), "id", showTimeRequest.getMovieId()));

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

        TicketType regularType = ticketTypeRepository.findByName(String.valueOf(TicketsType.REGULAR).toLowerCase()).orElseThrow(() -> new ResourceNofFoundException(TicketType.class.getSimpleName(), null, null));
        TicketType halfPriceType = ticketTypeRepository.findByName(String.valueOf(TicketsType.HALF_PRICE).toLowerCase()).orElseThrow(() -> new ResourceNofFoundException(TicketType.class.getSimpleName(), null, null));

        List<Map<String, String>> seats;
        if (showTime.getSeats() != null) {
            seats = showTime.getSeats().stream().map(seat -> {
                Map<String, String> seatMap = new HashMap<String, String>();
                seatMap.put("seatId", seat.getId());
                seatMap.put("hall", seat.getHall().getName());
                seatMap.put("row", String.valueOf(seat.getRow().getNumber()));
                seatMap.put("chair", String.valueOf(seat.getChair().getNumber()));
                seatMap.put("state", String.valueOf(seat.getState()));
                seatMap.put("regular", String.valueOf(regularType.getPrice()));
                seatMap.put("regularId", String.valueOf(regularType.getId()));
                seatMap.put("halfPrice", String.valueOf(halfPriceType.getPrice()));
                seatMap.put("halfPriceId", String.valueOf(halfPriceType.getId()));
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

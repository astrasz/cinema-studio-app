package com.cinemastudio.cinemastudioapp.service;

import com.cinemastudio.cinemastudioapp.dto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ShowTimeService {
    List<ShowTimeResponse> getAll(Integer pageNr, Integer number, String sortBy, String sortDir);

    ShowTimeResponse getOneById(String showTimeId);

    ShowTimeResponse create(ShowTimeRequest showTimeRequest);

//    ShowTimeResponse update(String showTimeId, ShowTimeRequest showTimeRequest);

    String remove(String showTimeId);

    ShowTimeResponse setSeats(SeatRequest seatRequest, String showTimeId);
}

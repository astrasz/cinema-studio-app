package com.cinemastudio.cinemastudioapp.service;

import com.cinemastudio.cinemastudioapp.dto.SeatRequest;
import com.cinemastudio.cinemastudioapp.model.Chair;
import com.cinemastudio.cinemastudioapp.model.Hall;
import com.cinemastudio.cinemastudioapp.model.Row;
import com.cinemastudio.cinemastudioapp.model.ShowTime;

public interface SeatService {
    ShowTime createMany(SeatRequest seatRequest, ShowTime showTime);

    void create(ShowTime showTime, Hall hall, Row row, Chair chair);
}

package com.cinemastudio.cinemastudioapp.service.impl;

import com.cinemastudio.cinemastudioapp.dto.SeatRequest;
import com.cinemastudio.cinemastudioapp.exception.DemandedResourceInvalidException;
import com.cinemastudio.cinemastudioapp.model.*;
import com.cinemastudio.cinemastudioapp.repository.HallRepository;
import com.cinemastudio.cinemastudioapp.repository.SeatRepository;
import com.cinemastudio.cinemastudioapp.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SeatServiceImpl implements SeatService {
    @Autowired
    private final HallRepository hallRepository;

    @Autowired
    private final SeatRepository seatRepository;

    public SeatServiceImpl(HallRepository hallRepository, SeatRepository seatRepository) {
        this.hallRepository = hallRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    @Override
    public ShowTime createMany(SeatRequest seatRequest, ShowTime showTime) {
        List<String> hallsIds = seatRequest.getHallsIds();
        List<Hall> hallList = hallRepository.findAllByIds(hallsIds);

        boolean hallListValid = hallList.stream().allMatch(hall -> Hall.class.getSimpleName().equals(hall.getClass().getSimpleName()));
        if (!hallListValid) {
            throw new DemandedResourceInvalidException(Hall.class.getSimpleName());
        }

        for (Hall hall : hallList) {
            List<Row> rows = hall.getRows();
            for (Row row : rows) {
                List<Chair> chairs = row.getChairs();
                for (Chair chair : chairs) {
                    create(showTime, hall, row, chair);
                }
            }
        }
        return showTime;
    }

    @Transactional
    @Override
    public void create(ShowTime showTime, Hall hall, Row row, Chair chair) {
        Seat seat = Seat.builder()
                .showTime(showTime)
                .hall(hall)
                .row(row)
                .chair(chair)
                .build();
        seatRepository.save(seat);
    }
}

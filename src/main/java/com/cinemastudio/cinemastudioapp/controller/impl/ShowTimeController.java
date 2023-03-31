package com.cinemastudio.cinemastudioapp.controller.impl;

import com.cinemastudio.cinemastudioapp.controller.ApiController;
import com.cinemastudio.cinemastudioapp.dto.request.SeatRequest;
import com.cinemastudio.cinemastudioapp.dto.request.ShowTimeRequest;
import com.cinemastudio.cinemastudioapp.dto.response.ShowTimeResponse;
import com.cinemastudio.cinemastudioapp.service.impl.ShowTimeServiceImpl;
import com.cinemastudio.cinemastudioapp.util.ApiConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/showTimes")
@RestController
public class ShowTimeController implements ApiController<ShowTimeRequest, ShowTimeResponse> {

    private final ShowTimeServiceImpl showTimeServiceImpl;

    public ShowTimeController(ShowTimeServiceImpl showTimeServiceImpl) {
        this.showTimeServiceImpl = showTimeServiceImpl;
    }

    @GetMapping
    @Override
    public ResponseEntity<List<ShowTimeResponse>> getAll(
            @RequestParam(value = "pageNr", defaultValue = ApiConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNr,
            @RequestParam(value = "number", defaultValue = ApiConstants.DEFAULT_LIMIT_QUERY, required = false) Integer number,
            @RequestParam(value = "sortBy", defaultValue = ApiConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ApiConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return ResponseEntity.ok().body(showTimeServiceImpl.getAll(pageNr, number, sortBy, sortDir));
    }

    @GetMapping("/{showTimeId}")
    @Override
    public ResponseEntity<ShowTimeResponse> getOneById(@PathVariable String showTimeId) {
        return ResponseEntity.ok().body(showTimeServiceImpl.getOneById(showTimeId));
    }

    @Override
    public ResponseEntity<ShowTimeResponse> update(String id, ShowTimeRequest request) {
        return null;
    }

    @PostMapping
    @Override
    public ResponseEntity<ShowTimeResponse> create(@Valid @RequestBody ShowTimeRequest request) {
        return new ResponseEntity<>(showTimeServiceImpl.create(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{showTimeId}")
    @Override
    public ResponseEntity<String> remove(@PathVariable String showTimeId) {
        return ResponseEntity.ok().body(showTimeServiceImpl.remove(showTimeId));
    }

    @PostMapping("/{showTimeId}/seats")
    public ResponseEntity<ShowTimeResponse> setSeats(@Valid @RequestBody SeatRequest seatRequest, @PathVariable String showTimeId) {
        return ResponseEntity.ok().body(showTimeServiceImpl.setSeats(seatRequest, showTimeId));
    }
}

package com.cinemastudio.cinemastudioapp.controller;

import com.cinemastudio.cinemastudioapp.dto.ShowTimeRequest;
import com.cinemastudio.cinemastudioapp.dto.ShowTimeResponse;
import com.cinemastudio.cinemastudioapp.service.ShowTimeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/showTime")
@RestController
public class ShowTimeController implements ApiController<ShowTimeRequest, ShowTimeResponse> {

    private final ShowTimeService showTimeService;

    public ShowTimeController(ShowTimeService showTimeService) {
        this.showTimeService = showTimeService;
    }

    @GetMapping
    @Override
    public ResponseEntity<List<ShowTimeResponse>> getAll(
            @RequestParam Integer pageNr,
            @RequestParam Integer number,
            @RequestParam String sortBy,
            @RequestParam String sortDir) {
        return ResponseEntity.ok(showTimeService.getAll(pageNr, number, sortBy, sortDir));
    }

    @Override
    public ResponseEntity<List<ShowTimeResponse>> getAll() {
        return null;
    }

    @GetMapping("/{showTimeId}")
    @Override
    public ResponseEntity<ShowTimeResponse> getOneById(@PathVariable String showTimeId) {
        return ResponseEntity.ok(showTimeService.getOneById(showTimeId));
    }

    @Override
    public ResponseEntity<ShowTimeResponse> update(String id, ShowTimeRequest request) {
        return null;
    }

    @PostMapping
    @Override
    public ResponseEntity<ShowTimeResponse> create(@Valid @RequestBody ShowTimeRequest request) {
        return new ResponseEntity<>(showTimeService.create(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{showTimeId}")
    @Override
    public ResponseEntity<String> remove(@PathVariable String showTimeId) {
        return ResponseEntity.ok(showTimeService.remove(showTimeId));
    }
}

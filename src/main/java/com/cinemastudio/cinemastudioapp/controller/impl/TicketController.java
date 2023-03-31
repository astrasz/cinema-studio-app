package com.cinemastudio.cinemastudioapp.controller.impl;

import com.cinemastudio.cinemastudioapp.controller.ApiController;
import com.cinemastudio.cinemastudioapp.dto.request.TicketRequest;
import com.cinemastudio.cinemastudioapp.dto.response.TicketResponse;
import com.cinemastudio.cinemastudioapp.service.impl.TicketServiceImpl;
import com.cinemastudio.cinemastudioapp.util.ApiConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/tickets")
@RestController
public class TicketController implements ApiController<TicketRequest, TicketResponse> {

    private final TicketServiceImpl ticketService;

    @Autowired
    public TicketController(TicketServiceImpl ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    @Override
    public ResponseEntity<List<TicketResponse>> getAll(@RequestParam(value = "pageNr", defaultValue = ApiConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNr,
                                                       @RequestParam(value = "number", defaultValue = ApiConstants.DEFAULT_LIMIT_QUERY, required = false) Integer number,
                                                       @RequestParam(value = "sortBy", defaultValue = ApiConstants.DEFAULT_SORT_BY, required = false) String sortBy,
                                                       @RequestParam(value = "sortDir", defaultValue = ApiConstants.DEFAULT_SORT_DIR, required = false) String sortDir) {
        return ResponseEntity.ok().body(ticketService.getAll(pageNr, number, sortBy, sortDir));
    }

    @GetMapping("/{ticketId}")
    @Override
    public ResponseEntity<TicketResponse> getOneById(String id) {
        return ResponseEntity.ok().body(ticketService.getOneById(id));
    }

    @PutMapping("/{ticketId}")
    @Override
    public ResponseEntity<TicketResponse> update(@PathVariable String ticketId, @Valid @RequestBody TicketRequest request) {
        return ResponseEntity.ok().body(ticketService.updateStatus(ticketId, request.getStatusId()));
    }

    @PostMapping()
    @Override
    public ResponseEntity<TicketResponse> create(@Valid @RequestBody TicketRequest ticketRequest) {
        return new ResponseEntity<>(ticketService.create(ticketRequest), HttpStatus.CREATED);
    }


    @DeleteMapping("/{ticketId}")
    @Override
    public ResponseEntity<String> remove(@PathVariable String ticketId) {
        return ResponseEntity.ok().body(ticketService.remove(ticketId));
    }
}

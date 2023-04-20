package com.cinemastudio.cinemastudioapp.service;

import com.cinemastudio.cinemastudioapp.dto.request.MovieRequest;
import com.cinemastudio.cinemastudioapp.dto.request.TicketRequest;
import com.cinemastudio.cinemastudioapp.dto.response.MovieResponse;
import com.cinemastudio.cinemastudioapp.dto.response.TicketResponse;
import com.cinemastudio.cinemastudioapp.util.TicketStatus;

import java.util.List;

public interface TicketService {

    List<TicketResponse> getAll(Integer pageNr, Integer number, String sortBy, String sortDir);

    TicketResponse getOneById(String ticketId);

    TicketResponse create(TicketRequest ticketRequest);

    List<TicketResponse> createMany(List<TicketRequest> ticketRequestList);

    TicketResponse updateStatus(String ticketId, String status);

    String remove(String ticketId);
}

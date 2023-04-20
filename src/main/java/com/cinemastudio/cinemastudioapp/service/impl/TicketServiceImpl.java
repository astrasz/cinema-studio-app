package com.cinemastudio.cinemastudioapp.service.impl;

import com.cinemastudio.cinemastudioapp.dto.request.TicketRequest;
import com.cinemastudio.cinemastudioapp.dto.response.TicketResponse;
import com.cinemastudio.cinemastudioapp.exception.InvalidRequestParameterException;
import com.cinemastudio.cinemastudioapp.exception.ResourceNofFoundException;
import com.cinemastudio.cinemastudioapp.model.ApiUser;
import com.cinemastudio.cinemastudioapp.model.Seat;
import com.cinemastudio.cinemastudioapp.model.Ticket;
import com.cinemastudio.cinemastudioapp.model.TicketType;
import com.cinemastudio.cinemastudioapp.repository.ApiUserRepository;
import com.cinemastudio.cinemastudioapp.repository.SeatRepository;
import com.cinemastudio.cinemastudioapp.repository.TicketRepository;
import com.cinemastudio.cinemastudioapp.repository.TicketTypeRepository;
import com.cinemastudio.cinemastudioapp.service.TicketService;
import com.cinemastudio.cinemastudioapp.util.ApiConstants;
import com.cinemastudio.cinemastudioapp.util.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final ApiUserRepository apiUserRepository;
    private final SeatRepository seatRepository;
    private final TicketTypeRepository ticketTypeRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, ApiUserRepository apiUserRepository, SeatRepository seatRepository, TicketTypeRepository ticketTypeRepository) {
        this.ticketRepository = ticketRepository;
        this.apiUserRepository = apiUserRepository;
        this.seatRepository = seatRepository;
        this.ticketTypeRepository = ticketTypeRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<TicketResponse> getAll(Integer pageNr, Integer number, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNr, number, sort);
        Page<Ticket> tickets = ticketRepository.findAll(pageable);

        List<Ticket> ticketList = tickets.getContent();

        return ticketList.stream().map(this::mapToTicketResponse).toList();
    }


    @Transactional(readOnly = true)
    @Override
    public TicketResponse getOneById(String ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNofFoundException(Ticket.class.getSimpleName(), "id", ticketId));
        return mapToTicketResponse(ticket);
    }

    @Transactional
    @Override
    public TicketResponse create(TicketRequest ticketRequest) {

        Map<String, Object> linkedEntities = returnLinkedEntities(ticketRequest);
        Ticket ticket = Ticket.builder()
                .seat((Seat) linkedEntities.get(Seat.class.getSimpleName()))
                .user((ApiUser) linkedEntities.get(ApiUser.class.getSimpleName()))
                .type((TicketType) linkedEntities.get(TicketType.class.getSimpleName()))
                .build();

        return mapToTicketResponse(ticket);
    }

    public List<TicketResponse> createMany(List<TicketRequest> ticketRequestList) {
        List<TicketResponse> ticketResponseList = new ArrayList<>();
        for (TicketRequest request : ticketRequestList) {
            TicketResponse ticketResponse = create(request);
            ticketResponseList.add(ticketResponse);
        }
        return ticketResponseList;
    }

    private Map<String, Object> returnLinkedEntities(TicketRequest ticketRequest) {
        ApiUser user = apiUserRepository.findById(ticketRequest.getUserId()).orElseThrow(() -> new ResourceNofFoundException(ApiUser.class.getSimpleName(), "id", ticketRequest.getUserId()));
        Seat seat = seatRepository.findById(ticketRequest.getSeatId()).orElseThrow(() -> new ResourceNofFoundException(Seat.class.getSimpleName(), "id", ticketRequest.getSeatId()));
        TicketType type = ticketTypeRepository.findById(ticketRequest.getTypeId()).orElseThrow(() -> new ResourceNofFoundException(TicketType.class.getSimpleName(), "id", ticketRequest.getTypeId()));

        Map<String, Object> entities = new HashMap<>();
        entities.put(ApiUser.class.getSimpleName(), user);
        entities.put(Seat.class.getSimpleName(), seat);
        entities.put(TicketType.class.getSimpleName(), type);

        return entities;
    }

    @Transactional
    @Override
    public TicketResponse updateStatus(String ticketId, String status) {
        checkStatus(status);
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNofFoundException(Ticket.class.getSimpleName(), "id", ticketId));
        ticket.setStatus(TicketStatus.valueOf(status));
        ticketRepository.save(ticket);

        return mapToTicketResponse(ticket);
    }

    @Transactional
    @Override
    public String remove(String ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNofFoundException(Ticket.class.getSimpleName(), "id", ticketId));
        String ticketMovieTitle = ticket.getSeat().getShowTime().getMovie().getTitle();

        ticketRepository.delete(ticket);
        return String.format("Ticket for %s has been deleted successfully", ticketMovieTitle);
    }


    private TicketResponse mapToTicketResponse(Ticket ticket) {

        return TicketResponse.builder()
                .id(ticket.getId())
                .userId(ticket.getUser().getId())
                .userFirstName(ticket.getUser().getFirstname())
                .userListName(ticket.getUser().getLastname())
                .userEmail(ticket.getUser().getEmail())
                .typeName(ticket.getType().getName())
                .price(ticket.getType().getPrice().toString())
                .date(ApiConstants.DEFAULT_DATE_FORMATTER.format(ticket.getSeat().getShowTime().getDate()))
                .hallName(ticket.getSeat().getHall().getName())
                .row(ticket.getSeat().getRow().toString())
                .chair(ticket.getSeat().getChair().toString())
                .build();
    }


    private void checkStatus(String status) {
        if (!Arrays.stream(TicketStatus.values()).toList().contains(TicketStatus.valueOf(status))) {
            throw new InvalidRequestParameterException("status", status);
        }
    }


}

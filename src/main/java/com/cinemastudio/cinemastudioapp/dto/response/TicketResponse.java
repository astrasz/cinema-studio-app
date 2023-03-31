package com.cinemastudio.cinemastudioapp.dto.response;

import com.cinemastudio.cinemastudioapp.model.ApiUser;
import com.cinemastudio.cinemastudioapp.model.Seat;
import com.cinemastudio.cinemastudioapp.model.TicketType;
import com.cinemastudio.cinemastudioapp.util.TicketStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
public class TicketResponse {
    private String id;
    private String userId;
    private String userFirstName;
    private String userListName;
    private String userEmail;
    private String typeName;
    private String price;
    private String date;
    private String hallName;
    private String row;
    private String chair;
}

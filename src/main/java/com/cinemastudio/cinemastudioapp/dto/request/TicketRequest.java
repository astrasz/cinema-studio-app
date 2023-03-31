package com.cinemastudio.cinemastudioapp.dto.request;

import com.cinemastudio.cinemastudioapp.model.ApiUser;
import com.cinemastudio.cinemastudioapp.model.Seat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketRequest {

    @NotEmpty(message = "Seat id cannot be empty")
    private String seatId;

    @NotEmpty(message = "User id cannot be empty")
    private String userId;

    @NotEmpty(message = "Type id cannot be empty")
    private String typeId;

    private String statusId;

}

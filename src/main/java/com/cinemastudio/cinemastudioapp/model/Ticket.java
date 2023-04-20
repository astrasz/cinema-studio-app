package com.cinemastudio.cinemastudioapp.model;


import com.cinemastudio.cinemastudioapp.util.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "status", columnDefinition = "varchar(32) default 'SOLD'")
    @Enumerated(value = EnumType.STRING)
    private TicketStatus status;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private TicketType type;

    @OneToOne
    @JoinColumn(name = "seat_id", referencedColumnName = "id")
    private Seat seat;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private ApiUser user;
}

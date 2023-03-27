package com.cinemastudio.cinemastudioapp.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "hall_id", referencedColumnName = "id", nullable = false)
    private Hall hall;

    @ManyToOne
    @JoinColumn(name = "row_id", referencedColumnName = "id", nullable = false)
    private Row row;

    @ManyToOne
    @JoinColumn(name = "chair_id", referencedColumnName = "id", nullable = false)
    private Chair chair;

    @ManyToOne
    @JoinColumn(name = "show_time_id", referencedColumnName = "id", nullable = false)
    private ShowTime showTime;


}

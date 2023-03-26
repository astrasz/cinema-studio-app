package com.cinemastudio.cinemastudioapp.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "rows")
public class Row {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private int number;

    @OneToMany(mappedBy = "row", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chair> chairs;

    @OneToMany(mappedBy = "row", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats;
}


package com.cinemastudio.cinemastudioapp.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "chairs")
public class Chair {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private int number;

    @ManyToOne
    @JoinColumn(name = "row_id")
    private Row row;

    @OneToMany(mappedBy = "chair", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats;

}

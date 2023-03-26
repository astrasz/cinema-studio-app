package com.cinemastudio.cinemastudioapp.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "halls")
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @ManyToMany
    @JoinTable(name = "halls_rows",
            joinColumns = @JoinColumn(name = "hall_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "row_id", referencedColumnName = "id")
    )
    private List<Row> rows;

    @ManyToMany
    @JoinTable(name = "halls_chairs",
            joinColumns = @JoinColumn(name = "hall_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "char_id", referencedColumnName = "id")
    )
    private List<Chair> chairs;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats;
}

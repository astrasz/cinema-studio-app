package com.cinemastudio.cinemastudioapp.model;

import com.cinemastudio.cinemastudioapp.util.SeatState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "state", columnDefinition = "varchar(32) default 'AVAILABLE'")
    @Enumerated(value = EnumType.STRING)
    private SeatState state;

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

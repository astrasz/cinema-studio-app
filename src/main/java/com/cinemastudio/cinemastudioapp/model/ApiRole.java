package com.cinemastudio.cinemastudioapp.model;

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
@Table(name = "roles")
public class ApiRole {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private String id;
    private String name;
}

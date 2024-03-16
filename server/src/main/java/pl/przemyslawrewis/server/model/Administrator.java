package pl.przemyslawrewis.server.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Reprezentuje encję administratora w bazie danych.
 */
@Entity
@Table(name = "administratorzy")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_administratora")
    private Integer id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "haslo", nullable = false)
    private String haslo;

}

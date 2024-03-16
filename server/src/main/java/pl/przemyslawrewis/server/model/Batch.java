package pl.przemyslawrewis.server.model;


import jakarta.persistence.*;
import lombok.*;

/**
 * Reprezentuje encję partii politycznej w bazie danych.
 */
@Entity
@Table(name = "partie_polityczne")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Batch {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_partii")
    private Integer id;

    @Column(name = "nazwa_partii", nullable = false)
    private String nazwa;

    @Column(name = "opis_partii", columnDefinition = "TEXT")
    private String opis;

}


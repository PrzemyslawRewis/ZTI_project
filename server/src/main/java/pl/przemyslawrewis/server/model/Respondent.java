package pl.przemyslawrewis.server.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Reprezentuje encję respondenta w bazie danych.
 */
@Entity
@Table(name = "respondenci")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Respondent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_respondenta")
    private Integer id;

    @Column(name = "imie", nullable = false)
    private String imie;

    @Column(name = "nazwisko", nullable = false)
    private String nazwisko;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "haslo", nullable = false)
    private String haslo;

}

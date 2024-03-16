package pl.przemyslawrewis.klient.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Klasa reprezentująca partię.
 */
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Batch implements Serializable {
    /**
     * Identyfikator partii.
     */
    @Id
    private Integer id;

    /**
     * Nazwa partii.
     */
    private String nazwa;

    /**
     * Opis partii.
     */
    private String opis;
}



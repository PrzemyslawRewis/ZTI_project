package pl.przemyslawrewis.klient.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Klasa reprezentująca administratora systemu.
 */
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Administrator {
    /**
     * Identyfikator administratora.
     */
    @Id
    private Integer id;

    /**
     * Adres e-mail administratora.
     */
    private String email;

    /**
     * Hasło administratora.
     */
    private String haslo;
}


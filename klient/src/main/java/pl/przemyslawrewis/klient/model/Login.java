package pl.przemyslawrewis.klient.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Klasa reprezentująca dane logowania.
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Login {
    /**
     * Adres e-mail użytkownika.
     */
    private String email;

    /**
     * Hasło użytkownika.
     */
    private String password;
}


package pl.przemyslawrewis.klient.model;

import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Klasa reprezentująca respondentów.
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Respondent implements Serializable {

    /**
     * Konstruktor klasy Respondent.
     *
     * @param personName     Pole tekstowe zawierające imię respondenta.
     * @param personSurname  Pole tekstowe zawierające nazwisko respondenta.
     * @param personMail     Pole tekstowe zawierające adres e-mail respondenta.
     * @param personPassword Pole tekstowe zawierające hasło respondenta.
     */
    public Respondent(TextField personName, TextField personSurname, TextField personMail, PasswordField personPassword) {
        setImie(personName.getValue());
        setNazwisko(personSurname.getValue());
        setEmail(personMail.getValue());
        setHaslo(personPassword.getValue());
    }

    /**
     * Identyfikator respondenta.
     */
    @Id
    private Integer id;

    /**
     * Imię respondenta.
     */
    private String imie;

    /**
     * Nazwisko respondenta.
     */
    private String nazwisko;

    /**
     * Adres e-mail respondenta.
     */
    private String email;

    /**
     * Hasło respondenta.
     */
    private String haslo;

}


package pl.przemyslawrewis.klient.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Klasa reprezentująca wyniki ankiety.
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class SurveyResult {

    /**
     * Identyfikator wyniku ankiety.
     */
    private Integer id;

    /**
     * Czas zarejestrowania wyniku ankiety.
     */
    private Timestamp czas;

    /**
     * Respondent powiązany z wynikiem ankiety.
     */
    private Respondent respondent;

    /**
     * Partia popierana przez respondenta.
     */
    private Batch partia;

    /**
     * Konstruktor klasy SurveyResult.
     *
     * @param mail      Adres e-mail respondenta.
     * @param batchName Nazwa popieranej partii.
     */
    public SurveyResult(String mail, String batchName) {
        Respondent r = new Respondent();
        r.setEmail(mail);
        this.setRespondent(r);
        Batch b = new Batch();
        b.setNazwa(batchName);
        this.setPartia(b);
    }
}



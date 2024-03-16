package pl.przemyslawrewis.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.przemyslawrewis.server.model.Respondent;

import java.util.Optional;

/**
 * Repozytorium danych dla encji Respondent.
 */
@Repository
public interface RespondentRepository extends JpaRepository<Respondent, Integer> {

    /**
     * Metoda wyszukująca respondentów na podstawie adresu e-mail i hasła.
     *
     * @param email adres e-mail respondentów
     * @param haslo hasło respondentów
     * @return obiekt Optional przechowujący znalezionego respondenta (jeśli istnieje)
     */
    Optional<Respondent> findRespondentByEmailAndHaslo(String email, String haslo);

    /**
     * Metoda wyszukująca respondentów na podstawie adresu e-mail.
     *
     * @param email adres e-mail respondentów
     * @return obiekt Optional przechowujący znalezionego respondenta (jeśli istnieje)
     */
    Optional<Respondent> findRespondentByEmail(String email);
}

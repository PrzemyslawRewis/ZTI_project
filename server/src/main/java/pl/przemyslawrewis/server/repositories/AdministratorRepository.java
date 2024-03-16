package pl.przemyslawrewis.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.przemyslawrewis.server.model.Administrator;

import java.util.Optional;

/**
 * Repozytorium danych dla encji Administrator.
 */
@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

    /**
     * Metoda wyszukująca administratora na podstawie adresu email i hasła.
     *
     * @param email adres email administratora
     * @param haslo hasło administratora
     * @return obiekt Optional przechowujący znalezionego administratora (jeśli istnieje)
     */
    Optional<Administrator> findAdministratorByEmailAndHaslo(String email, String haslo);

    /**
     * Metoda wyszukująca administratora na podstawie adresu email.
     *
     * @param email adres email administratora
     * @return obiekt Optional przechowujący znalezionego administratora (jeśli istnieje)
     */
    Optional<Administrator> findAdministratorByEmail(String email);

}


package pl.przemyslawrewis.server.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.przemyslawrewis.server.model.Batch;

import java.util.Optional;

/**
 * Repozytorium danych dla encji Batch.
 */
@Repository
public interface BatchRepository extends JpaRepository<Batch, Integer> {

    /**
     * Metoda wyszukująca partię na podstawie nazwy.
     *
     * @param nazwa nazwa partii
     * @return obiekt Optional przechowujący znalezioną partię (jeśli istnieje)
     */
    Optional<Batch> findBatchByNazwa(String nazwa);
}
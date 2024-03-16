package pl.przemyslawrewis.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.przemyslawrewis.server.model.Batch;
import pl.przemyslawrewis.server.model.SurveyResult;


import java.util.ArrayList;
import java.util.List;

/**
 * Repozytorium danych dla encji SurveyResult.
 */
@Repository
public interface SurveyResultRepository extends JpaRepository<SurveyResult, Integer> {

    /**
     * Metoda wyszukująca wyniki badań na podstawie partii.
     *
     * @param batch partia
     * @return lista wyników badań dla danej partii
     */
    ArrayList<SurveyResult> findSurveyResultsByPartia(Batch batch);

    /**
     * Metoda wyszukująca najnowsze wyniki badań dla wszystkich respondentów.
     *
     * @return lista najnowszych wyników badań dla wszystkich respondentów
     */
    @Query("SELECT s FROM SurveyResult s WHERE s.czas IN (SELECT MAX(s2.czas) FROM SurveyResult s2 GROUP BY s2.respondent)")
    List<SurveyResult> findLatestSurveyResultsForAllRespondents();


    @Override
    void delete(SurveyResult entity);
}

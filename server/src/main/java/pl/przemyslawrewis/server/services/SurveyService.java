package pl.przemyslawrewis.server.services;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.przemyslawrewis.server.model.Batch;
import pl.przemyslawrewis.server.model.Respondent;
import pl.przemyslawrewis.server.model.SurveyResult;
import pl.przemyslawrewis.server.repositories.BatchRepository;
import pl.przemyslawrewis.server.repositories.RespondentRepository;
import pl.przemyslawrewis.server.repositories.SurveyResultRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Serwis obsługujący operacje na wynikach badań.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SurveyService {

    private final SurveyResultRepository resultRepository;
    private final RespondentRepository respondentRepository;
    private final BatchRepository batchRepository;


    /**
     * Metoda zapisująca wynik badania.
     *
     * @param response        obiekt odpowiedzi HTTP
     * @param newSurveyResult nowy wynik badania do zapisania
     */
    public void saveSurveyResult(HttpServletResponse response, SurveyResult newSurveyResult) {
        Optional<Batch> batch = batchRepository.findBatchByNazwa(newSurveyResult.getPartia().getNazwa());
        Optional<Respondent> respondent = respondentRepository.findRespondentByEmail(newSurveyResult.getRespondent().getEmail());
        if (batch.isPresent() && respondent.isPresent()) {
            newSurveyResult.setRespondent(respondent.get());
            newSurveyResult.setPartia(batch.get());
            newSurveyResult.setCzas(Timestamp.valueOf(LocalDateTime.now()));
            resultRepository.save(newSurveyResult);
            response.setStatus(HttpServletResponse.SC_CREATED);
        }
    }
}

package pl.przemyslawrewis.server.services;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.przemyslawrewis.server.model.Administrator;
import pl.przemyslawrewis.server.model.Batch;
import pl.przemyslawrewis.server.model.Login;
import pl.przemyslawrewis.server.model.SurveyResult;
import pl.przemyslawrewis.server.repositories.AdministratorRepository;
import pl.przemyslawrewis.server.repositories.BatchRepository;
import pl.przemyslawrewis.server.repositories.SurveyResultRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Serwis obsługujący operacje administratora.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AdministratorService {

    private final AdministratorRepository administratorRepository;
    private final SurveyResultRepository resultRepository;
    private final BatchRepository batchRepository;

    /**
     * Metoda realizujaca dodawanie nowego administratora.
     *
     * @param response         obiekt odpowiedzi HTTP
     * @param newAdministrator nowy administrator do dodania
     */
    public void addAdministrator(HttpServletResponse response, Administrator newAdministrator) {
        Optional<Administrator> administrator = administratorRepository.findAdministratorByEmail(newAdministrator.getEmail());
        if (administrator.isPresent()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            administratorRepository.save(newAdministrator);
            response.setStatus(HttpServletResponse.SC_CREATED);
        }
    }

    /**
     * Metoda realizująca logowanie administratora.
     *
     * @param response obiekt odpowiedzi HTTP
     * @param login    dane logowania administratora
     */
    public void loginAdministrator(HttpServletResponse response, Login login) {
        Optional<Administrator> respondent = administratorRepository.findAdministratorByEmailAndHaslo(login.getEmail(), login.getPassword());
        if (respondent.isPresent()) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    /**
     * Metoda realizujaca edycje partii.
     *
     * @param response     obiekt odpowiedzi HTTP
     * @param batchName    nazwa partii do zedytowania
     * @param updatedBatch nowe dane partii
     */
    public void updateBatch(HttpServletResponse response, String batchName, Batch updatedBatch) {
        Optional<Batch> batch = batchRepository.findBatchByNazwa(batchName);
        if (batch.isPresent()) {
            Batch batchToUpdate = batch.get();
            batchToUpdate.setOpis(updatedBatch.getOpis().isBlank() ? batchToUpdate.getOpis() : updatedBatch.getOpis());
            batchToUpdate.setNazwa(updatedBatch.getNazwa().isBlank() ? batchToUpdate.getOpis() : updatedBatch.getNazwa());
            batchRepository.save(batchToUpdate);
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Metoda zwracająca listę  najnowszych wyników badań dla wszystkich respondentów.
     *
     * @return lista wyników badań
     */
    public List<SurveyResult> getSurveyResults() {
        return resultRepository.findLatestSurveyResultsForAllRespondents();
    }

    /**
     * Metoda realizujaca dodawanie partii.
     *
     * @param response obiekt odpowiedzi HTTP
     * @param newBatch nowa partia do dodania
     */
    public void addParty(HttpServletResponse response, Batch newBatch) {
        Optional<Batch> batch = batchRepository.findBatchByNazwa(newBatch.getNazwa());
        if (batch.isPresent()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            batchRepository.save(newBatch);
            response.setStatus(HttpServletResponse.SC_CREATED);
        }
    }

    /**
     * Metoda realizujaca usuwanie partii.
     *
     * @param response obiekt odpowiedzi HTTP
     * @param name     nazwa partii do usuniecia
     */
    public void deleteParty(HttpServletResponse response, String name) {
        Optional<Batch> batch = batchRepository.findBatchByNazwa(name);
        if (batch.isPresent()) {
            ArrayList<SurveyResult> resultsToDelete = resultRepository.findSurveyResultsByPartia(batch.get());
            resultRepository.deleteAll(resultsToDelete);
            batchRepository.deleteById(batch.get().getId());
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }

    /**
     * Metoda zwracająca listę wszystkich partii politycznych.
     *
     * @return lista partii politycznych
     */
    public List<Batch> getAllParties() {
        return batchRepository.findAll();
    }
}

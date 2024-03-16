package pl.przemyslawrewis.server.services;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.przemyslawrewis.server.model.Batch;
import pl.przemyslawrewis.server.model.Login;
import pl.przemyslawrewis.server.model.Respondent;
import pl.przemyslawrewis.server.repositories.BatchRepository;
import pl.przemyslawrewis.server.repositories.RespondentRepository;

import java.util.List;
import java.util.Optional;

/**
 * Serwis obsługujący operacje respondenta.
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RespondentService {

    private final RespondentRepository respondentRepository;
    private final BatchRepository batchRepository;

    /**
     * Metoda realizujaca rejestrację nowego respondenta.
     *
     * @param response       obiekt odpowiedzi HTTP
     * @param newRespondent  nowy respondent do zarejestrowania
     */
    public void registerRespondent(HttpServletResponse response, Respondent newRespondent) {
        Optional<Respondent> respondent = respondentRepository.findRespondentByEmail(newRespondent.getEmail());
        if (respondent.isPresent()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            respondentRepository.save(newRespondent);
            response.setStatus(HttpServletResponse.SC_CREATED);
        }

    }

    /**
     * Metoda realizujaca logowanie respondenta.
     *
     * @param response obiekt odpowiedzi HTTP
     * @param login    dane logowania respondent
     */
    public void loginRespondent(HttpServletResponse response, Login login) {
        Optional<Respondent> respondent = respondentRepository.findRespondentByEmailAndHaslo(login.getEmail(), login.getPassword());
        if (respondent.isPresent()) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
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

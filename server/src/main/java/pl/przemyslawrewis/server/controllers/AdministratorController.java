package pl.przemyslawrewis.server.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.przemyslawrewis.server.model.Administrator;
import pl.przemyslawrewis.server.model.Batch;
import pl.przemyslawrewis.server.model.Login;
import pl.przemyslawrewis.server.model.SurveyResult;
import pl.przemyslawrewis.server.services.AdministratorService;

import java.util.List;

/**
 * Kontroler obsługujący żądania dotyczące administratora.
 */
@RestController
@RequestMapping("/administrator")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdministratorController {

    private final AdministratorService administratorService;

    /**
     * Endpoint obsługujący dodawanie administratora.
     *
     * @param response   obiekt HttpServletResponse
     * @param respondent obiekt Administrator reprezentujący dodawanego administratora
     */
    @PostMapping("/add")
    public void addAdministrator(HttpServletResponse response, @RequestBody Administrator respondent) {
        administratorService.addAdministrator(response, respondent);
    }

    /**
     * Endpoint obsługujący logowanie administratora.
     *
     * @param response obiekt HttpServletResponse
     * @param login    obiekt Login reprezentujący dane logowania administratora
     */
    @PostMapping("/login")
    public void loginAdministrator(HttpServletResponse response, @RequestBody Login login) {
        administratorService.loginAdministrator(response, login);
    }

    /**
     * Endpoint obsługujący aktualizację danych partii politycznej.
     *
     * @param response     obiekt HttpServletResponse
     * @param batchName    nazwa partii politycznej do zaktualizowania
     * @param updatedBatch obiekt Batch reprezentujący zaktualizowane dane partii politycznej
     */
    @PutMapping("/batches/{name}")
    public void updateParty(HttpServletResponse response, @PathVariable("name") String batchName, @RequestBody Batch updatedBatch) {
        administratorService.updateBatch(response, batchName, updatedBatch);
    }

    /**
     * Endpoint obsługujący dodawanie nowej partii politycznej.
     *
     * @param response obiekt HttpServletResponse
     * @param newBatch obiekt Batch reprezentujący nową partię polityczną do dodania
     */
    @PostMapping("/batches")
    public void addParty(HttpServletResponse response, @RequestBody Batch newBatch) {
        administratorService.addParty(response, newBatch);
    }

    /**
     * Endpoint obsługujący usuwanie partii politycznej.
     *
     * @param response  obiekt HttpServletResponse
     * @param batchName nazwa partii politycznej do usunięcia
     */
    @DeleteMapping("/batches/{name}")
    public void deleteParty(HttpServletResponse response, @PathVariable("name") String batchName) {
        administratorService.deleteParty(response, batchName);
    }

    /**
     * Endpoint obsługujący pobieranie wyników badań.
     *
     * @return lista obiektów SurveyResult reprezentujących wyniki badań
     */
    @GetMapping("/survey-results")
    public List<SurveyResult> getSurveyResults() {
        return administratorService.getSurveyResults();
    }

    /**
     * Endpoint obsługujący pobieranie wszystkich partii politycznych.
     *
     * @return lista obiektów Batch reprezentujących partię polityczne
     */
    @GetMapping("/batches")
    public List<Batch> getAllParties() {
        return administratorService.getAllParties();
    }

}

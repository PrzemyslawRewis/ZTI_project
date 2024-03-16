package pl.przemyslawrewis.server.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.przemyslawrewis.server.model.Batch;
import pl.przemyslawrewis.server.model.Login;
import pl.przemyslawrewis.server.model.Respondent;
import pl.przemyslawrewis.server.services.RespondentService;

import java.util.List;


@RestController
@RequestMapping("/respondent")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RespondentController {

    private final RespondentService respondentService;

    /**
     * Endpoint obsługujący rejestracje respondenta.
     *
     * @param response   obiekt HttpServletResponse
     * @param respondent obiekt Respondent reprezentujący dodawanego respondenta.
     */
    @PostMapping("/register")
    public void registerRespondent(HttpServletResponse response, @RequestBody Respondent respondent) {
        respondentService.registerRespondent(response, respondent);
    }

    /**
     * Endpoint obsługujący logowanie respondenta.
     *
     * @param response obiekt HttpServletResponse
     * @param login    obiekt Login reprezentujący dane logowania respondenta.
     */
    @PostMapping("/login")
    public void loginRespondent(HttpServletResponse response, @RequestBody Login login) {
        respondentService.loginRespondent(response, login);
    }

    /**
     * Endpoint obsługujący pobieranie wszystkich partii politycznych.
     *
     * @return lista obiektów Batch reprezentujących partię polityczne
     */
    @GetMapping("/batches")
    public List<Batch> getAllParties() {
        return respondentService.getAllParties();
    }
}

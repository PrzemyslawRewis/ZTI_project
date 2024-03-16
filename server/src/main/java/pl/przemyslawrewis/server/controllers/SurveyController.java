package pl.przemyslawrewis.server.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.przemyslawrewis.server.model.SurveyResult;
import pl.przemyslawrewis.server.services.SurveyService;

@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SurveyController {

    private final SurveyService surveyService;

    /**
     * Endpoint obsługujący dodawanie nowego wyniku badania.
     *
     * @param response     obiekt HttpServletResponse
     * @param surveyResult obiekt SurveyResult reprezentujący nowy wyniku badania.
     */
    @PostMapping("/saveResult")
    public void saveSurveyResult(HttpServletResponse response, @RequestBody SurveyResult surveyResult) {
        surveyService.saveSurveyResult(response, surveyResult);
    }

}


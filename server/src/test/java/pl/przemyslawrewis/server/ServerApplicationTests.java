package pl.przemyslawrewis.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.przemyslawrewis.server.aspects.LoggingAspect;
import pl.przemyslawrewis.server.controllers.AdministratorController;
import pl.przemyslawrewis.server.controllers.RespondentController;
import pl.przemyslawrewis.server.controllers.SurveyController;
import pl.przemyslawrewis.server.services.AdministratorService;
import pl.przemyslawrewis.server.services.RespondentService;
import pl.przemyslawrewis.server.services.SurveyService;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Testy jednostkowe dla klasy ServerApplication.
 */
@SpringBootTest
class ServerApplicationTests {

    @Autowired
    private LoggingAspect myAspect;
    @Autowired
    private AdministratorController administratorController;
    @Autowired
    private RespondentController respondentController;
    @Autowired
    private SurveyController surveyController;
    @Autowired
    private AdministratorService administratorService;
    @Autowired
    private RespondentService respondentService;
    @Autowired
    private SurveyService surveyService;


    /**
     * Sprawdza, czy aplikacja jest w stanie poprawnie załadować kontekst Springa.
     */
    @Test
    void contextLoads() {
    }

    /**
     * Sprawdza, czy kontrolery zostały poprawnie załadowane i wstrzyknięte przez Springa.
     *
     * @throws Exception w przypadku błędu podczas testu
     */
    @Test
    void controllersLoads() throws Exception {
        assertThat(administratorController).isNotNull();
        assertThat(respondentController).isNotNull();
        assertThat(surveyController).isNotNull();
    }

    /**
     * Sprawdza, czy serwisy zostały poprawnie załadowane i wstrzyknięte przez Springa.
     *
     * @throws Exception w przypadku błędu podczas testu
     */
    @Test
    void servicesLoads() throws Exception {
        assertThat(administratorService).isNotNull();
        assertThat(respondentService).isNotNull();
        assertThat(surveyService).isNotNull();
    }


    /**
     * Sprawdza, czy aspekt LoggingAspect został poprawnie załadowany i wstrzyknięty przez Springa.
     *
     * @throws Exception w przypadku błędu podczas testu
     */
    @Test
    void aspectsLoads() throws Exception {
        assertThat(myAspect).isNotNull();
    }

}

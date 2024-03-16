package pl.przemyslawrewis.klient.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pl.przemyslawrewis.klient.model.Batch;
import pl.przemyslawrewis.klient.model.Login;
import pl.przemyslawrewis.klient.model.SurveyResult;

import java.util.ArrayList;

import static pl.przemyslawrewis.klient.utils.ViewUtils.prepareCustomPolishLogin;
import static pl.przemyslawrewis.klient.utils.ViewUtils.prepareHttpHeaders;

/**
 * Widok panelu respondenta.
 */
@Route(value = "respondent", layout = MainView.class)
@PageTitle("Panel respondenta")
@PreserveOnRefresh
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Service
public class RespondentMainView extends VerticalLayout {
    private final RestTemplate restTemplate;
    private boolean isLoggedIn;
    private LoginForm loginForm;
    private String mail;

    /**
     * Konstruktor klasy RespondentMainView.
     * Inicjalizuje obiekt klasy RestTemplate oraz formularz logowania.
     * Wyświetla formularz logowania lub panel respondenta, w zależności od stanu zalogowania.
     * @param restTemplateBuilder Obiekt klasy RestTemplateBuilder do tworzenia instancji RestTemplate.
     */
    public RespondentMainView(@Autowired RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.loginForm = new LoginForm();
        if (isLoggedIn) {
            showRespondentPanel();
        } else {
            showLoginForm();
        }
    }

    /**
     * Metoda wyświetlająca formularz logowania.
     * Ustawia formularz logowania w centrum widoku.
     * Obsługuje zdarzenie logowania i autoryzacji użytkownika.
     */
    private void showLoginForm() {
        loginForm = new LoginForm(prepareCustomPolishLogin());
        setAlignItems(Alignment.CENTER);
        loginForm.addLoginListener(e -> {
            boolean isAuthenticated = authenticate(e.getUsername(), e.getPassword());
            if (isAuthenticated) {
                isLoggedIn = true;
                mail = e.getUsername();
                removeAll();
                showRespondentPanel();
            } else {
                loginForm.setError(true);
            }
        });
        add(loginForm);
    }

    /**
     * Metoda wyświetlająca panel respondenta.
     * Wyświetla przyciski i interakcje dostępne w panelu respondenta.
     * Obsługuje akcje przycisków, takie jak wyświetlanie informacji o partiach, ankieta, wylogowanie.
     */
    private void showRespondentPanel() {
        removeAll();
        Button showBatchesInfoButton = new Button("Partie");
        Button showSurveyButton = new Button("Ankieta");
        Button logoutButton = new Button("Wyloguj się");
        Button saveSurveyButton = new Button("Zapisz ankiete");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(showBatchesInfoButton, showSurveyButton, logoutButton);
        add(horizontalLayout);
        Grid<Batch> batchGrid = prepareBatchGrid();
        Select<String> batchSelect = prepareBatchSelect();
        showBatchesInfoButton.addClickListener(buttonClicked -> showBatchesInfo(saveSurveyButton, horizontalLayout, batchGrid, batchSelect));
        showSurveyButton.addClickListener(buttonClicked -> showSurvey(saveSurveyButton, horizontalLayout, batchGrid, batchSelect));
        logoutButton.addClickListener(buttonClicked -> logout());
    }

    /**
     * Metoda przygotowująca rozwijaną listę z nazwami partii.
     *
     * @return Obiekt klasy Select<String> zawierający rozwijaną listę z nazwami partii.
     */
    private Select<String> prepareBatchSelect() {
        Select<String> batchSelect = new Select<>();
        ArrayList<String> batchNames = new ArrayList<>();
        Batch[] batches = getBatches();
        for (Batch batch : batches) {
            batchNames.add(batch.getNazwa());
        }
        batchSelect.setItems(batchNames);
        batchSelect.setValue(batchNames.get(0));
        return batchSelect;
    }
    /**
     * Metoda przygotowująca siatkę (Grid) z informacjami o partiach.
     *
     * @return Obiekt klasy Grid<Batch> zawierający siatkę z informacjami o partiach.
     */
    private Grid<Batch> prepareBatchGrid() {
        Grid<Batch> batchGrid = new Grid<>(Batch.class);
        batchGrid.setItems(getBatches());
        batchGrid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        batchGrid.getColumnByKey("id").setVisible(false);
        return batchGrid;
    }

    /**
     * Metoda wyświetlająca sondaż.
     * Usuwa siatkę z informacjami o partiach i dodaje rozwijaną listę partii.
     * Dodaje przycisk "Zapisz ankietę".
     *
     * @param saveSurveyButton Przycisk "Zapisz ankietę".
     * @param horizontalLayout Obiekt klasy HorizontalLayout zawierający przyciski.
     * @param batchGrid        Siatka (Grid) z informacjami o partiach.
     * @param batchSelect      Obiekt klasy Select<String> zawierający rozwijaną listę partii.
     */
    private void showSurvey(Button saveSurveyButton, HorizontalLayout horizontalLayout, Grid<Batch> batchGrid, Select<String> batchSelect) {
        remove(batchGrid);
        add(batchSelect);
        horizontalLayout.add(saveSurveyButton);
        saveSurveyButton.addClickListener(saveButtonClicked -> saveSurvey(batchSelect.getValue()));
    }

    /**
     * Metoda wyświetlająca informacje o partiach.
     * Usuwa rozwijaną listę partii i dodaje siatkę z informacjami o partiach.
     *
     * @param saveSurveyButton Przycisk "Zapisz ankietę".
     * @param horizontalLayout Obiekt klasy HorizontalLayout zawierający przyciski.
     * @param batchGrid        Siatka (Grid) z informacjami o partiach.
     * @param batchSelect      Obiekt klasy Select<String> zawierający rozwijaną listę partii.
     */
    private void showBatchesInfo(Button saveSurveyButton, HorizontalLayout horizontalLayout, Grid<Batch> batchGrid, Select<String> batchSelect) {
        remove(batchSelect);
        horizontalLayout.remove(saveSurveyButton);
        add(batchGrid);
    }

    /**
     * Metoda obsługująca wylogowanie użytkownika.
     * Usuwa wszystkie komponenty z widoku, ustawia stan zalogowania na false i wyświetla formularz logowania.
     */
    private void logout() {
        removeAll();
        isLoggedIn = false;
        mail = "";
        showLoginForm();
    }

    /**
     * Metoda autoryzująca użytkownika na podstawie podanych danych logowania.
     *
     * @param mail     Adres e-mail użytkownika.
     * @param password Hasło użytkownika.
     * @return Wartość logiczna informująca o sukcesie autoryzacji.
     */
    private boolean authenticate(String mail, String password) {
        return postCredentials(mail, password).is2xxSuccessful();
    }

    /**
     * Metoda wysyłająca dane logowania na serwer w celu autoryzacji.
     *
     * @param mail     Adres e-mail użytkownika.
     * @param password Hasło użytkownika.
     * @return Obiekt klasy HttpStatusCode reprezentujący status odpowiedzi HTTP.
     */
    private HttpStatusCode postCredentials(String mail, String password) {
        Login credentials = new Login(mail, password);
        String url = "http://localhost:8080/respondent/login";
        HttpEntity<Login> entity = new HttpEntity<>(credentials, prepareHttpHeaders());
        try {
            return restTemplate.postForEntity(url, entity, Login.class).getStatusCode();
        } catch (HttpClientErrorException e) {
            loginForm.setError(true);
            return HttpStatusCode.valueOf(401);
        } catch (Exception e) {
            Notification.show("Wystapił bład podczas zapisywania wyników ankiety");
            return HttpStatusCode.valueOf(500);
        }
    }
    /**
     * Metoda zapisująca wynik ankiety.
     *
     * @param batchName Nazwa wybranej partii.
     */
    private void saveSurvey(String batchName) {
        SurveyResult result = new SurveyResult(mail, batchName);
        String url = "http://localhost:8080/survey/saveResult";
        HttpEntity<SurveyResult> entity = new HttpEntity<>(result, prepareHttpHeaders());
        try {
            if (restTemplate.postForEntity(url, entity, SurveyResult.class).getStatusCode().is2xxSuccessful())
                Notification.show("Poprawnie zapisano wynik sondażu");
        } catch (Exception e) {
            Notification.show("Wystapił bład podczas zapisywania wyników ankiety");
        }
    }

    /**
     * Metoda pobierająca informacje o partiach z serwera.
     *
     * @return Tablica obiektów klasy Batch zawierających informacje o partiach.
     */
    private Batch[] getBatches() {
        String url = "http://localhost:8080/respondent/batches";
        try {
            return this.restTemplate.getForObject(url, Batch[].class);
        } catch (Exception e) {
            Notification.show("Wystapił błąd podczas pobierania listy partii");
            return new Batch[0];
        }
    }
}


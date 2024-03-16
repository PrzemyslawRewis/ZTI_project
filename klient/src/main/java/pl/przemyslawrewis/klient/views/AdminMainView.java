package pl.przemyslawrewis.klient.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.przemyslawrewis.klient.model.Administrator;
import pl.przemyslawrewis.klient.model.Batch;
import pl.przemyslawrewis.klient.model.Login;
import pl.przemyslawrewis.klient.model.SurveyResult;

import static pl.przemyslawrewis.klient.utils.ViewUtils.*;

import java.util.*;

/**
 * Widok panelu admina.
 */
@Route(value = "admin", layout = MainView.class)
@PageTitle("Logowanie administratora")
@Service
@PreserveOnRefresh
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AdminMainView extends VerticalLayout {
    private final RestTemplate restTemplate;
    private boolean isLoggedIn;
    private LoginForm loginForm;
    private Grid<Batch> batchGrid;
    private Grid<Map.Entry<String, Double>> batchSupportGrid;
    private Select<String> batchSelect;

    /**
     * Konstruktor klasy AdminMainView.
     * Wyświetla formularz logowania lub panel admina, w zależności od stanu zalogowania.
     *
     * @param restTemplateBuilder Obiekt klasy RestTemplateBuilder do tworzenia instancji RestTemplate.
     */
    public AdminMainView(@Autowired RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.batchGrid = new Grid<>();
        this.batchSupportGrid = new Grid<>();
        this.loginForm = new LoginForm();
        this.batchSelect = new Select<>();
        if (isLoggedIn) {
            showAdminPanel();
        } else {
            showLoginForm();
        }
    }

    /**
     * Metoda wyświetlająca formularz logowania.
     * Ustawia formularz logowania w centrum widoku.
     * Obsługuje zdarzenie logowania i autoryzacji admina.
     */
    private void showLoginForm() {
        loginForm = new LoginForm();
        LoginI18n adminI18 = prepareCustomPolishAddAdmin();
        adminI18.getForm().setTitle("Zaloguj się do panelu administratora");
        loginForm.setI18n(adminI18);
        setAlignItems(Alignment.CENTER);
        loginForm.addLoginListener(e -> {
            boolean isAuthenticated = authenticate(e.getUsername(), e.getPassword());
            if (isAuthenticated) {
                isLoggedIn = true;
                removeAll();
                showAdminPanel();
            } else {
                loginForm.setError(true);
            }
        });
        add(loginForm);
    }

    /**
     * Metoda wyświetlająca panel admina.
     * Wyświetla przyciski i interakcje dostępne w panelu admina.
     * Obsługuje akcje przycisków, takie jak wyświetlanie informacji o partiach i ich poparciu, dodanie admina, usuwanie, edycja, dodanie partii, wylogowanie.
     */
    private void showAdminPanel() {
        removeAll();
        Button showBatchesInfoButton = new Button("Partie");
        Button showAddBatchFormButton = new Button("Dodawanie partii");
        Button showEditBatchFormButton = new Button("Edytwanie partii");
        Button showBatchDeleteButton = new Button("Usuwanie partii");
        Button logoutButton = new Button("Wyloguj się");
        Button deleteBatchButton = new Button("Usuń partię");
        Button addAdminButton = new Button("Dodaj nowego admina");
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(showBatchesInfoButton, showAddBatchFormButton, showEditBatchFormButton, showBatchDeleteButton, addAdminButton, logoutButton);
        add(horizontalLayout);
        this.batchGrid = prepareBatchGrid();
        this.batchSupportGrid = prepareBatchSupportGrid();
        this.batchSelect = prepareBatchSelect();
        LoginForm adminAddForm = prepareAddAdminForm();
        FormLayout batchAddForm = createAddBatchForm();
        FormLayout batchEditForm = createEditBatchForm();
        showAddBatchFormButton.addClickListener(buttonClicked -> showBatchForm(horizontalLayout, batchAddForm));
        showEditBatchFormButton.addClickListener(buttonClicked -> showBatchForm(horizontalLayout, batchEditForm));
        showBatchesInfoButton.addClickListener(buttonClicked -> showBatchesInfo(horizontalLayout));
        showBatchDeleteButton.addClickListener(buttonClicked -> showBatchDelete(deleteBatchButton, horizontalLayout));
        addAdminButton.addClickListener(buttonClicked -> showAddAdminForm(horizontalLayout, adminAddForm));
        logoutButton.addClickListener(event -> logout());
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
     * Metoda przygotowująca siatkę (Grid) z informacjami o poparciu dla wszystkich partii.
     *
     * @return Grid<Map.Entry < String, Double>> zawierający siatkę z informacjami o partiach i ich poparciu w procentach.
     */
    private Grid<Map.Entry<String, Double>> prepareBatchSupportGrid() {
        Grid<Map.Entry<String, Double>> batchSupportGrid = new Grid<>();
        batchSupportGrid.addColumn(Map.Entry::getKey).setHeader("Nazwa");
        batchSupportGrid.addColumn(entry -> entry.getValue() + "%").setHeader("Poparcie");
        batchSupportGrid.setItems(calculatePercent().entrySet());
        return batchSupportGrid;
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
     * Metoda tworząca formularz dodawania admina.
     * Obsługuje zdarzenie dodania nowego administratora.
     *
     * @return Obiekt klasy LoginForm.
     */
    private LoginForm prepareAddAdminForm() {
        LoginForm adminAddForm = new LoginForm(prepareCustomPolishAddAdmin());
        setAlignItems(Alignment.CENTER);
        adminAddForm.addLoginListener(e -> {
            boolean alreadyExist = adminAlreadyExist(e.getUsername(), e.getPassword());
            if (!alreadyExist) {
                Notification.show("Poprawnie dodano admina");
                remove(adminAddForm);
                showAdminPanel();
            } else {
                adminAddForm.setError(true);
            }
        });
        return adminAddForm;
    }

    /**
     * Tworzy układ formularza dodawania partii.
     *
     * @return Układ formularza dodawania partii
     */
    private FormLayout createAddBatchForm() {
        TextField batchName = createFormTextField("Nazwa partii");
        TextField batchDescription = createFormTextField("Opis partii");
        Button addBatchButton = new Button("Dodaj partię");
        FormLayout formLayout = new FormLayout();
        formLayout.add(batchName, batchDescription, addBatchButton);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), new FormLayout.ResponsiveStep("600px", 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        addBatchButton.addClickListener(e -> {
            addBatch(batchName, batchDescription);
            refreshBatches();
        });
        return formLayout;
    }

    /**
     * Tworzy układ formularza edytowania partii.
     *
     * @return Układ formularza edytowania partii
     */
    private FormLayout createEditBatchForm() {
        TextField newBatchName = createFormTextField("Nowa nazwa partii");
        TextField newBatchDescription = createFormTextField("Nowy opis partii");
        Button editBatchButton = new Button("Edytuj partię");
        FormLayout formLayout = new FormLayout();
        formLayout.add(batchSelect, newBatchName, newBatchDescription, editBatchButton);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), new FormLayout.ResponsiveStep("600px", 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        editBatchButton.addClickListener(e -> {
            editBatch(batchSelect, newBatchName, newBatchDescription);
            refreshBatches();
        });
        return formLayout;
    }

    /**
     * Wyświetla formularz dodawania lub edycji partii.
     *
     * @param horizontalLayout Układ poziomy
     * @param batchEditForm    Formularz edycji/dodawania partii
     */
    private void showBatchForm(HorizontalLayout horizontalLayout, FormLayout batchEditForm) {
        removeAll();
        add(horizontalLayout);
        add(batchEditForm);
    }

    /**
     * Wyświetla informacje o partiach.
     *
     * @param horizontalLayout Układ poziomy
     */
    private void showBatchesInfo(HorizontalLayout horizontalLayout) {
        removeAll();
        add(horizontalLayout);
        add(new H3("Poparcie"));
        add(batchSupportGrid);
        add(new H3("Informacje o partiach"));
        add(batchGrid);
    }

    /**
     * Wyświetla widok usuwania partii.
     *
     * @param deleteBatchButton Przycisk usuwania partii
     * @param horizontalLayout  Układ poziomy
     */
    private void showBatchDelete(Button deleteBatchButton, HorizontalLayout horizontalLayout) {
        removeAll();
        horizontalLayout.add(deleteBatchButton);
        add(horizontalLayout);
        add(batchSelect);
        deleteBatchButton.addClickListener(event -> {
            deleteBatch(batchSelect.getValue());
            refreshBatches();
            horizontalLayout.remove(deleteBatchButton);

        });
    }

    /**
     * Wyświetla formularz dodawania administratora.
     *
     * @param horizontalLayout Układ poziomy
     * @param adminAddForm     Formularz dodawania administratora
     */
    private void showAddAdminForm(HorizontalLayout horizontalLayout, LoginForm adminAddForm) {
        removeAll();
        add(horizontalLayout);
        add(adminAddForm);
    }

    /**
     * Metoda obsługująca wylogowanie admina.
     * Usuwa wszystkie komponenty z widoku, ustawia stan zalogowania na false i wyświetla formularz logowania.
     */
    private void logout() {
        removeAll();
        isLoggedIn = false;
        showLoginForm();
    }

    /**
     * Dodaje nową partię.
     *
     * @param batchName        Pole tekstowe z nazwą partii
     * @param batchDescription Pole tekstowe z opisem partii
     */
    private void addBatch(TextField batchName, TextField batchDescription) {
        if (allFormTextFieldsAreNotEmpty(batchName, batchDescription)) {
            handleResponseFromBatchCreation(postBatch(batchName, batchDescription));
            clearFormTextFields(batchName, batchDescription);
        }
    }

    /**
     * Edytuje wybraną partię.
     *
     * @param batchSelect      Selektor partii
     * @param batchName        Pole tekstowe z nazwą partii
     * @param batchDescription Pole tekstowe z opisem partii
     */
    private void editBatch(Select<String> batchSelect, TextField batchName, TextField batchDescription) {
        if (allFormTextFieldsAreNotEmpty(batchName, batchDescription)) {
            putBatch(batchSelect, batchName, batchDescription);
            clearFormTextFields(batchName, batchDescription);
        }
    }

    /**
     * Metoda wysyłająca żądanie usunięcia partii do serwera.
     *
     * @param batchName nazwa partii.
     */
    private void deleteBatch(String batchName) {
        String url = "http://localhost:8080/administrator/batches/{name}";
        String encodedName = UriComponentsBuilder.fromUriString(url).buildAndExpand(batchName).toUriString();
        try {
            restTemplate.delete(encodedName);
            Notification.show("Partia została usunięta pomyślnie");
        } catch (Exception e) {
            Notification.show("Wystapił bład podczas usuwania partii");
        }
    }

    /**
     * Metoda autoryzująca admina na podstawie podanych danych logowania.
     *
     * @param mail     Adres e-mail admina.
     * @param password Hasło admina.
     * @return Wartość logiczna informująca o sukcesie autoryzacji.
     */
    private boolean authenticate(String mail, String password) {
        return postCredentials(mail, password).is2xxSuccessful();
    }

    /**
     * Metoda sprawdzajaca czy admin, którego dodajemy już istnieje na podstawie podanych danych logowania.
     *
     * @param mail     Adres e-mail admina.
     * @param password Hasło admina.
     * @return Wartość logiczna informująca o istnieniu admina.
     */
    private boolean adminAlreadyExist(String mail, String password) {
        return !postAdmin(mail, password).is2xxSuccessful();
    }

    /**
     * Metoda wysyłająca dane logowania na serwer w celu autoryzacji.
     *
     * @param mail     Adres e-mail admina.
     * @param password Hasło admina.
     * @return Obiekt klasy HttpStatusCode reprezentujący status odpowiedzi HTTP.
     */
    private HttpStatusCode postCredentials(String mail, String password) {
        Login credentials = new Login(mail, password);
        String url = "http://localhost:8080/administrator/login";
        HttpEntity<Login> entity = new HttpEntity<>(credentials, prepareHttpHeaders());
        try {
            return restTemplate.postForEntity(url, entity, Login.class).getStatusCode();
        } catch (HttpClientErrorException e) {
            loginForm.setError(true);
            return HttpStatusCode.valueOf(401);
        } catch (Exception e) {
            Notification.show("Wystapił bład podczas logowania");
            return HttpStatusCode.valueOf(500);
        }
    }

    /**
     * Metoda wysyłająca żądanie dodania admina do serwera.
     *
     * @param mail     pole tekstowe z mailem admina.
     * @param password pole tekstowe z hasłem admina.
     * @return status kod odpowiedzi HTTP.
     */
    private HttpStatusCode postAdmin(String mail, String password) {
        Administrator administrator = new Administrator();
        administrator.setEmail(mail);
        administrator.setHaslo(password);
        String url = "http://localhost:8080/administrator/add";
        HttpEntity<Administrator> entity = new HttpEntity<>(administrator, prepareHttpHeaders());
        try {
            return restTemplate.postForEntity(url, entity, Administrator.class).getStatusCode();
        } catch (HttpClientErrorException e) {
            loginForm.setError(true);
            return HttpStatusCode.valueOf(401);
        } catch (Exception e) {
            Notification.show("Wystapił bład podczas dodawania admina");
            return HttpStatusCode.valueOf(500);
        }
    }

    /**
     * Metoda pobierająca informacje o partiach z serwera.
     *
     * @return Tablica obiektów klasy Batch zawierających informacje o partiach.
     */
    private Batch[] getBatches() {
        String url = "http://localhost:8080/administrator/batches";
        try {
            return this.restTemplate.getForObject(url, Batch[].class);
        } catch (Exception e) {
            Notification.show("Wystapił błąd podczas pobierania listy partii");
            return new Batch[0];
        }
    }

    /**
     * Metoda pobierająca informacje o wynikach sondażu z serwera.
     *
     * @return Tablica obiektów klasy SurveyResult zawierających informacje o wynikach.
     */
    private SurveyResult[] getSurveyResults() {
        String url = "http://localhost:8080/administrator/survey-results";
        try {
            return this.restTemplate.getForObject(url, SurveyResult[].class);
        } catch (Exception e) {
            Notification.show("Wystapił błąd podczas pobierania wyników");
            return new SurveyResult[0];
        }
    }

    /**
     * Metoda wysyłająca żądanie zaktualizowania partii do serwera.
     *
     * @param batchSelect         nazwa partii którą chcemy zaktualizować
     * @param newBatchName        pole tekstowe z nową nazwą partii.
     * @param newBatchDescription pole tekstowe z nowym opisem partii.
     */
    private void putBatch(Select<String> batchSelect, TextField newBatchName, TextField newBatchDescription) {
        Batch batch = new Batch();
        batch.setNazwa(newBatchName.getValue());
        batch.setOpis(newBatchDescription.getValue());
        String url = "http://localhost:8080/administrator/batches/{name}";
        String encodedName = UriComponentsBuilder.fromUriString(url).buildAndExpand(batchSelect.getValue()).toUriString();
        try {
            restTemplate.put(encodedName, batch);
            Notification.show("Partia została zedytowana pomyślnie");
        } catch (Exception e) {
            Notification.show("Wystapił bład podczas zapisywania wyników ankiety");
            clearFormTextFields(newBatchName, newBatchDescription);
        }
    }

    /**
     * Metoda wysyłająca żądanie dodania partii do serwera.
     *
     * @param batchName        pole tekstowe z nazwą partii.
     * @param batchDescription pole tekstowe z opisem partii.
     * @return status kod odpowiedzi HTTP.
     */
    private HttpStatusCode postBatch(TextField batchName, TextField batchDescription) {
        Batch batch = new Batch();
        batch.setNazwa(batchName.getValue());
        batch.setOpis(batchDescription.getValue());
        String url = "http://localhost:8080/administrator/batches";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Batch> entity = new HttpEntity<>(batch, headers);
        try {
            return restTemplate.postForEntity(url, entity, Batch.class).getStatusCode();
        } catch (HttpClientErrorException e) {
            clearFormTextFields(batchName, batchDescription);
            return HttpStatusCode.valueOf(400);
        } catch (Exception e) {
            clearFormTextFields(batchName, batchDescription);
            return HttpStatusCode.valueOf(500);
        }
    }

    /**
     * Metoda obsługująca odpowiedź HTTP po wysłaniu żądania dodania partii.
     *
     * @param statusCode status kod odpowiedzi HTTP.
     */
    private void handleResponseFromBatchCreation(HttpStatusCode statusCode) {
        if (statusCode.is2xxSuccessful()) {
            Notification.show("Partia utworzona poprawnie");
        } else if (statusCode.is4xxClientError()) {
            Notification.show("Partia o takiej nazwie już istnieje");
        } else {
            Notification.show("Wystapił nieoczekiwany błąd");
        }
    }

    /**
     * Metoda służąca do aktualizowania partii w komponentach.
     */
    private void refreshBatches() {
        ArrayList<String> batchNames = new ArrayList<>();
        Batch[] batches = getBatches();
        for (Batch batch : batches) {
            batchNames.add(batch.getNazwa());
        }
        batchSelect.setItems(batchNames);
        batchSelect.setValue(batchNames.get(0));
        batchGrid.setItems(batches);
        batchSupportGrid.setItems(calculatePercent().entrySet());
    }

    /**
     * Metoda służąca do obliczania poparcia w procentach dla poszczególnych partii
     *
     * @return Map<String, Double> Kluczem to nazwa partii, a wartość to jej aktualne poparcie w procentach.
     */
    private Map<String, Double> calculatePercent() {
        SurveyResult[] surveyResults = getSurveyResults();
        List<SurveyResult> surveyResultList = new ArrayList<>(Arrays.asList(surveyResults));
        int totalResults = surveyResultList.size();
        Map<String, Integer> batchCountMap = new HashMap<>();
        for (SurveyResult surveyResult : surveyResultList) {
            String batchName = surveyResult.getPartia().getNazwa();
            batchCountMap.put(batchName, batchCountMap.getOrDefault(batchName, 0) + 1);
        }
        Map<String, Double> batchPercentageMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : batchCountMap.entrySet()) {
            String batchName = entry.getKey();
            int count = entry.getValue();
            double percentage = (count / (double) totalResults) * 100;
            batchPercentageMap.put(batchName, percentage);
        }
        return batchPercentageMap;
    }

    /**
     * Metoda przygotowująca niestandardowe ustawienia dla polskiego formularza dodawania admina.
     *
     * @return Obiekt LoginI18n z niestandardowymi ustawieniami.
     */
    private LoginI18n prepareCustomPolishAddAdmin() {
        LoginI18n i18n = prepareCustomPolishLogin();
        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Dodaj nowego administratora");
        i18nForm.setUsername("Email");
        i18nForm.setPassword("Hasło");
        i18nForm.setSubmit("Dodaj");
        i18nForm.setForgotPassword("");
        i18n.setForm(i18nForm);
        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Błąd");
        i18nErrorMessage.setMessage("Administrator o takim mailu istnieje!");
        i18n.setErrorMessage(i18nErrorMessage);
        LoginOverlay loginOverlay = new LoginOverlay();
        loginOverlay.setI18n(i18n);
        return i18n;
    }
}

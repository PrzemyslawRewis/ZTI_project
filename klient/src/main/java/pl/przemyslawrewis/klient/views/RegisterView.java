package pl.przemyslawrewis.klient.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
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
import pl.przemyslawrewis.klient.model.Respondent;

import static pl.przemyslawrewis.klient.utils.ViewUtils.*;

/**
 * Widok rejestracji respondenta.
 */
@Route(value = "register", layout = MainView.class)
@PageTitle("Rejestracja respondenta")
@Service
@PreserveOnRefresh
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RegisterView extends VerticalLayout {
    private final RestTemplate restTemplate;

    /**
     * Konstruktor widoku rejestracji respondenta.
     *
     * @param restTemplateBuilder builder do tworzenia instancji RestTemplate.
     */
    RegisterView(@Autowired RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        TextField personName = createFormTextField("Imię");
        TextField personSurname = createFormTextField("Nazwisko");
        TextField personMail = createFormTextField("Mail");
        PasswordField personPassword = createFormPasswordField("Hasło");
        Button addPersonButton = new Button("Zarejstruj się");
        FormLayout formLayout = new FormLayout();
        formLayout.add(personName, personSurname, personMail, personPassword, addPersonButton);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1, FormLayout.ResponsiveStep.LabelsPosition.TOP), new FormLayout.ResponsiveStep("600px", 1, FormLayout.ResponsiveStep.LabelsPosition.ASIDE));
        add(formLayout);
        addPersonButton.addClickListener(e -> addRespondent(personName, personSurname, personMail, personPassword));
    }

    /**
     * Metoda obsługująca dodanie nowego respondenta po kliknięciu przycisku "Zarejestruj się".
     *
     * @param personName     pole tekstowe z imieniem respondenta.
     * @param personSurname  pole tekstowe z nazwiskiem respondenta.
     * @param personMail     pole tekstowe z adresem e-mail respondenta.
     * @param personPassword pole hasłowe respondenta.
     */
    private void addRespondent(TextField personName, TextField personSurname, TextField personMail, PasswordField personPassword) {
        if (fieldsAreNotEmpty(personName, personSurname, personMail, personPassword)) {
            handleResponse(postPerson(personName, personSurname, personMail, personPassword));
            clearFormFields(personName, personSurname, personMail, personPassword);
        }
    }
    /**
     * Metoda obsługująca odpowiedź HTTP po wysłaniu żądania rejestracji respondenta.
     *
     * @param statusCode status kod odpowiedzi HTTP.
     */
    private void handleResponse(HttpStatusCode statusCode) {
        if (statusCode.is2xxSuccessful()) {
            Notification.show("Respondent utworzony poprawnie");
        } else if (statusCode.is4xxClientError()) {
            Notification.show("Respondent o takim adresie e-mail istnieje");
        } else {
            Notification.show("Podczas rejestracji wystapił nieoczekiwany błąd");
        }
    }

    /**
     * Metoda wysyłająca żądanie rejestracji respondenta do serwera.
     *
     * @param personName     pole tekstowe z imieniem respondenta.
     * @param personSurname  pole tekstowe z nazwiskiem respondenta.
     * @param personMail     pole tekstowe z adresem e-mail respondenta.
     * @param personPassword pole hasłowe respondenta.
     * @return status kod odpowiedzi HTTP.
     */
    private HttpStatusCode postPerson(TextField personName, TextField personSurname, TextField personMail, PasswordField personPassword) {
        Respondent respondent = new Respondent(personName, personSurname, personMail, personPassword);
        String url = "http://localhost:8080/respondent/register";
        HttpEntity<Respondent> entity = new HttpEntity<>(respondent, prepareHttpHeaders());
        try {
            return restTemplate.postForEntity(url, entity, Respondent.class).getStatusCode();
        } catch (HttpClientErrorException e) {
            return HttpStatusCode.valueOf(401);
        } catch (Exception e) {
            return HttpStatusCode.valueOf(500);
        }
    }

    /**
     * Metoda sprawdzająca, czy wszystkie pola formularza są niepuste.
     *
     * @param personName     pole tekstowe z imieniem respondenta.
     * @param personSurname  pole tekstowe z nazwiskiem respondenta.
     * @param personMail     pole tekstowe z adresem e-mail respondenta.
     * @param personPassword pole hasłowe respondenta.
     * @return true, jeśli wszystkie pola formularza są niepuste, false w przeciwnym razie.
     */
    private boolean fieldsAreNotEmpty(TextField personName, TextField personSurname, TextField personMail, PasswordField personPassword) {
        if (!personName.isEmpty() && !personSurname.isEmpty() && !personMail.isEmpty() && !personPassword.isEmpty()) {
            return true;
        } else {
            Notification.show("Wszystkie pola są wymagane");
            clearFormFields(personName, personSurname, personMail, personPassword);
            return false;
        }
    }
    /**
     * Metoda czyszcząca zawartość pól formularza.
     *
     * @param personName     pole tekstowe z imieniem respondenta.
     * @param personSurname  pole tekstowe z nazwiskiem respondenta.
     * @param personMail     pole tekstowe z adresem e-mail respondenta.
     * @param personPassword pole hasłowe respondenta.
     */
    private void clearFormFields(TextField personName, TextField personSurname, TextField personMail, PasswordField personPassword) {
        personName.clear();
        personSurname.clear();
        personMail.clear();
        personPassword.clear();
    }
}

package pl.przemyslawrewis.klient.utils;

import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;

/**
 * Klasa narzędziowa zawierająca przydatne metody do tworzenia widoków.
 */
public class ViewUtils {

    /**
     * Metoda przygotowująca niestandardowe ustawienia dla polskiego formularza logowania.
     *
     * @return Obiekt LoginI18n z niestandardowymi ustawieniami.
     */
    public static LoginI18n prepareCustomPolishLogin() {
        LoginI18n i18n = LoginI18n.createDefault();
        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Zaloguj się");
        i18nForm.setUsername("Email");
        i18nForm.setPassword("Hasło");
        i18nForm.setSubmit("Zaloguj się");
        i18nForm.setForgotPassword("");
        i18n.setForm(i18nForm);
        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Błąd");
        i18nErrorMessage.setMessage("Niepoprawne dane logowania");
        i18n.setErrorMessage(i18nErrorMessage);
        LoginOverlay loginOverlay = new LoginOverlay();
        loginOverlay.setI18n(i18n);
        return i18n;
    }

    /**
     * Tworzy pole tekstowe formularza.
     *
     * @param label Etykieta pola tekstowego.
     * @return Obiekt TextField.
     */
    public static TextField createFormTextField(String label) {
        TextField newTextField = new TextField(label);
        newTextField.setRequiredIndicatorVisible(true);
        return newTextField;
    }

    /**
     * Tworzy pole hasła formularza.
     *
     * @param label Etykieta pola hasła.
     * @return Obiekt PasswordField.
     */
    public static PasswordField createFormPasswordField(String label) {
        PasswordField passwordField = new PasswordField(label);
        passwordField.setRequiredIndicatorVisible(true);
        return passwordField;
    }

    /**
     * Czyści wartości pól tekstowych formularza.
     *
     * @param textFields Tablica pól tekstowych do wyczyszczenia.
     */
    public static void clearFormTextFields(TextField... textFields) {
        for (TextField textField : textFields) {
            textField.clear();
        }
    }

    /**
     * Sprawdza, czy wszystkie pola tekstowe formularza są niepuste.
     *
     * @param textFields Tablica pól tekstowych do sprawdzenia.
     * @return Wartość logiczna informująca, czy wszystkie pola tekstowe są niepuste.
     */
    public static boolean allFormTextFieldsAreNotEmpty(TextField... textFields) {
        for (TextField textField : textFields) {
            if (textField.isEmpty())
                return false;
        }
        return true;
    }

    /**
     * Przygotowuje nagłówki HTTP dla żądań.
     *
     * @return Obiekt HttpHeaders z ustawionymi nagłówkami.
     */
    public static HttpHeaders prepareHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
}

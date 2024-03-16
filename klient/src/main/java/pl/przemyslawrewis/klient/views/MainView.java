package pl.przemyslawrewis.klient.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Główny widok aplikacji.
 */
@Route(value = "/")
@PageTitle("Sondaż")
@PreserveOnRefresh
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Service
public class MainView extends AppLayout {

    /**
     * Konstruktor klasy MainView.
     * Inicjalizuje wygląd nagłówka i menu bocznego.
     */
    public MainView() {
        this.getElement().setAttribute("theme", Lumo.DARK);
        createHeader();
        createDrawer();
    }

    /**
     * Metoda tworząca nagłówek widoku.
     * Zawiera logo oraz przycisk do otwierania/ukrywania menu bocznego.
     */
    private void createHeader() {
        H1 logo = new H1("Badanie poparcia dla partii politycznych");
        logo.addClassNames("text-l", "m-m");
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");
        addToNavbar(header);

    }

    /**
     * Metoda tworząca menu boczne.
     * Zawiera linki do stron głównej, rejestracji respondenta, panelu respondenta oraz panelu administratora.
     */
    private void createDrawer() {
        RouterLink homePage = new RouterLink("Strona główna", MainView.class);
        RouterLink register = new RouterLink("Rejstracja respondenta", RegisterView.class);
        RouterLink userLogin = new RouterLink("Panel respondenta", RespondentMainView.class);
        RouterLink adminLogin = new RouterLink("Panel administratora", AdminMainView.class);
        addToDrawer(new VerticalLayout(homePage, register, userLogin, adminLogin));
    }

}
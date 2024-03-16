package pl.przemyslawrewis.klient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


/**
 * Klasa główna aplikacji klienta.
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class KlientApplication {

    /**
     * Metoda główna, uruchamiająca aplikację klienta.
     *
     * @param args Argumenty wiersza poleceń.
     */
    public static void main(String[] args) {
        SpringApplication.run(KlientApplication.class, args);
    }
}


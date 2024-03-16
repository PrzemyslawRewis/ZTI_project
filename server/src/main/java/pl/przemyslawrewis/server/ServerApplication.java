package pl.przemyslawrewis.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Główna klasa aplikacji serwerowej.
 */
@SpringBootApplication
public class ServerApplication {
    /**
     * Metoda uruchamiająca aplikację serwerową.
     *
     * @param args argumenty wiersza poleceń
     */
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}

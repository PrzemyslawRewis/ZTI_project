package pl.przemyslawrewis.server.model;

import lombok.*;

/**
 * Reprezentuje dane logowania użytkownika.
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Login {
    private String email;
    private String password;
}

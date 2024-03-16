package pl.przemyslawrewis.server.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;

/**
 * Reprezentuje wyniki badań poparcia w systemie.
 */
@Entity
@Table(name = "wyniki_badan")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class SurveyResult {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id_wyniku")
    private Integer id;

    @Column(name = "czas", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp czas;

    @ManyToOne
    @JoinColumn(name = "id_respondenta", referencedColumnName = "id_respondenta")
    private Respondent respondent;

    @ManyToOne
    @JoinColumn(name = "id_partii", referencedColumnName = "id_partii")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Batch partia;


}


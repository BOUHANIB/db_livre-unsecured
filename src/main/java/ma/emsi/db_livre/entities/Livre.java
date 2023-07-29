package ma.emsi.db_livre.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Livre")
public class Livre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long livreId;
    @NotEmpty
    private String titre;
    private String auteur;
    private String editeur;
    @Temporal(TemporalType.DATE)
    private Date dateEdition;
    private double prix;
    private String isbn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exposant_id")
    @NotNull
    private Exposant exposant;

}

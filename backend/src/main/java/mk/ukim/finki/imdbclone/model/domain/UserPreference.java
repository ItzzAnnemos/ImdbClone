package mk.ukim.finki.imdbclone.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mk.ukim.finki.imdbclone.model.enumerations.PreferenceType;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PreferenceType preferenceType;

    @Column(nullable = false)
    private String value; // example: "Sci-Fi", "Christopher Nolan"

    @Column(nullable = false)
    private Double score = 0.0;

    public UserPreference(User user, PreferenceType preferenceType, String value, Double score) {
        this.user = user;
        this.preferenceType = preferenceType;
        this.value = value;
        this.score = score;
    }
}
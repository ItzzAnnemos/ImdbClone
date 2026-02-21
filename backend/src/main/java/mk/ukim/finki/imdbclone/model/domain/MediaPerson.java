package mk.ukim.finki.imdbclone.model.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import mk.ukim.finki.imdbclone.model.enumerations.Role;

@Entity
@Table(name = "media_persons", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "media_id", "person_id", "role" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Media is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id", nullable = false)
    @ToString.Exclude
    private Media media;

    @NotNull(message = "Person is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    @ToString.Exclude
    private Person person;

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Optional: add a specific character name for Actors
    private String characterName;
}

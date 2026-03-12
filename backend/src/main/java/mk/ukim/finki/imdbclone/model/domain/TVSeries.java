package mk.ukim.finki.imdbclone.model.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("TV_SERIES")
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TVSeries extends Media {

    private Integer numberOfSeasons;

    private String status; // e.g., "Ended", "Returning Series", "Canceled"
}

package mk.ukim.finki.imdbclone.model.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("TV_SERIES")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TVSeries extends Media {

    private String creators; // Comma separated list of creators

    private Integer numberOfSeasons;

    private String status; // e.g., "Ended", "Returning Series", "Canceled"
}

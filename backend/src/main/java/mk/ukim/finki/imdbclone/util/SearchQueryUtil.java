package mk.ukim.finki.imdbclone.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;

public final class SearchQueryUtil {

    private static final Set<String> STOP_WORDS = Set.of(
            "the", "a", "an", "of", "and", "in", "on", "at", "to"
    );

    private SearchQueryUtil() {
    }

    public static String normalize(String input) {
        if (input == null) {
            return "";
        }

        return input.toLowerCase() // "Game Of Thrones" → "game of thrones"
                .trim()
                .replaceAll("\\s+", " ");
    }

    public static List<String> tokenize(String input) {
        String normalized = normalize(input);

        if (normalized.isBlank()) {
            return List.of();
        }

        return Arrays.stream(normalized.split(" ")) // "Game of Thrones" → ["game", "of", "thrones"]
                .filter(token -> !token.isBlank())
                .filter(token -> !STOP_WORDS.contains(token))
                .collect(Collectors.toList());
    }

    public static String compact(String input) {
        return normalize(input).replace(" ", ""); // "Ga me of thr ones" → "gameofthrones"
    }
}
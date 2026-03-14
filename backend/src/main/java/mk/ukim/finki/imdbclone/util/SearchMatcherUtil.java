package mk.ukim.finki.imdbclone.util;

import java.util.List;

public final class SearchMatcherUtil {

    private SearchMatcherUtil() {
    }

    public static int levenshtein(String a, String b) {

        if (a == null) a = "";
        if (b == null) b = "";

        // Create a matrix that will store the edit distances.
        // dp[i][j] will represent the minimum number of edits needed
        // to transform the first i characters of string 'a'
        // into the first j characters of string 'b'.
        int[][] dp = new int[a.length() + 1][b.length() + 1];


        // The first column represents transforming a prefix of string 'a'
        // into an empty string ""
        // Example:
        // a = "cat"
        // We compare:
        // "c"   → ""   → we must delete 1 character
        // "ca"  → ""   → we must delete 2 characters
        // "cat" → ""   → we must delete 3 characters
        // In general:
        // converting the first i characters of 'a' into "" requires i deletions.
        // So we fill the first column with values:
        // 0, 1, 2, 3, ..., a.length()
        for (int i = 0; i <= a.length(); i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= b.length(); j++) {
            dp[0][j] = j;
        }

        // Now we fill the rest of the matrix.
        // We compare characters from both strings step by step.
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {

                // Check if the current characters are the same.
                // If they match, no change is needed (cost = 0).
                // If they differ, we must replace one character (cost = 1).
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;

                // For every pair of characters we consider three possible operations:

                // 1. Delete a character from string 'a'
                int deletion = dp[i - 1][j] + 1;

                // 2. Insert a character into string 'a'
                int insertion = dp[i][j - 1] + 1;

                // 3. Replace a character (or keep it if cost = 0)
                int replacement = dp[i - 1][j - 1] + cost;

                // Choose the operation with the smallest cost.
                dp[i][j] = Math.min(Math.min(deletion, insertion), replacement);
            }
        }

        // The final answer is in the bottom-right cell of the matrix.
        // It represents the minimum number of edits required
        // to transform the entire string 'a' into the entire string 'b'.
        return dp[a.length()][b.length()];
    }

    public static boolean containsIgnoreCase(String text, String query) {
        if (text == null || query == null) {
            return false;
        }

        return SearchQueryUtil.normalize(text)
                .contains(SearchQueryUtil.normalize(query));
    }

    public static boolean compactContains(String text, String query) {
        if (text == null || query == null) {
            return false;
        }

        return SearchQueryUtil.compact(text)
                .contains(SearchQueryUtil.compact(query));
    }

    public static boolean fuzzyMatch(String text, String query, int maxDistance) {
        if (text == null || query == null) {
            return false;
        }

        String normalizedText = SearchQueryUtil.normalize(text);
        String normalizedQuery = SearchQueryUtil.normalize(query);

        if (normalizedText.isBlank() || normalizedQuery.isBlank()) {
            return false;
        }

        if (normalizedText.equals(normalizedQuery)) {
            return true;
        }

        if (oneSwapAway(normalizedText, normalizedQuery)) {
            return true;
        }

        if (compactContains(normalizedText, normalizedQuery)) {
            return true;
        }

        if (levenshtein(normalizedText, normalizedQuery) <= maxDistance) {
            return true;
        }

        List<String> textTokens = SearchQueryUtil.tokenize(normalizedText);
        List<String> queryTokens = SearchQueryUtil.tokenize(normalizedQuery);

        if (textTokens.isEmpty() || queryTokens.isEmpty()) {
            return false;
        }

        int matchedTokens = 0;

        for (String queryToken : queryTokens) {
            boolean tokenMatched = textTokens.stream().anyMatch(textToken ->
                    textToken.contains(queryToken)
                            || queryToken.contains(textToken)
                            || oneSwapAway(textToken, queryToken)
                            || levenshtein(textToken, queryToken) <= 1
            );

            if (tokenMatched) {
                matchedTokens++;
            }
        }

        return matchedTokens >= Math.max(1, queryTokens.size() - 1); // Return true if enough words from the query match the text (we allow one word to be wrong).
    }

    public static double scoreTextMatch(String text, String query) {
        if (text == null || query == null) {
            return 0.0;
        }

        String normalizedText = SearchQueryUtil.normalize(text);
        String normalizedQuery = SearchQueryUtil.normalize(query);

        if (normalizedText.isBlank() || normalizedQuery.isBlank()) {
            return 0.0;
        }

        if (normalizedText.equals(normalizedQuery)) {
            return 100.0;
        }

        if (normalizedText.startsWith(normalizedQuery)) {
            return 90.0;
        }

        if (normalizedText.contains(normalizedQuery)) {
            return 80.0;
        }

        if (compactContains(normalizedText, normalizedQuery)) {
            return 75.0;
        }

        int distance = levenshtein(
                SearchQueryUtil.compact(normalizedText),
                SearchQueryUtil.compact(normalizedQuery)
        );

        if (distance <= 2) {
            return 70.0;
        }

        if (distance <= 4) {
            return 55.0;
        }

        List<String> textTokens = SearchQueryUtil.tokenize(normalizedText);
        List<String> queryTokens = SearchQueryUtil.tokenize(normalizedQuery);

        if (textTokens.isEmpty() || queryTokens.isEmpty()) {
            return 0.0;
        }

        int matchedTokens = 0;

        for (String queryToken : queryTokens) {
            boolean tokenMatched = textTokens.stream().anyMatch(textToken ->
                    textToken.contains(queryToken)
                            || queryToken.contains(textToken)
                            || oneSwapAway(textToken, queryToken)
                            || levenshtein(textToken, queryToken) <= 1
            );

            if (tokenMatched) {
                matchedTokens++;
            }
        }

        if (matchedTokens == 0) {
            return 0.0;
        }

        return 40.0 + (30.0 * matchedTokens / (double) queryTokens.size());
    }

    public static boolean oneSwapAway(String a, String b) {
        if (a == null || b == null) {
            return false;
        }

        String first = SearchQueryUtil.normalize(a);
        String second = SearchQueryUtil.normalize(b);

        if (first.length() != second.length()) {
            return false;
        }

        if (first.equals(second)) {
            return false;
        }

        for (int i = 0; i < first.length() - 1; i++) {
            char[] chars = first.toCharArray();

            char temp = chars[i];
            chars[i] = chars[i + 1];
            chars[i + 1] = temp;

            if (new String(chars).equals(second)) {
                return true;
            }
        }

        return false;
    }
}
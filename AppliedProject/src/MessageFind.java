import java.util.ArrayList;

public class MessageFind {
    public static ArrayList<String> analyzeAll(String cipherText, String guessMessage) {
        ArrayList<String> validMatches = new ArrayList<>();
        int cipherLen = cipherText.length();
        int guessLen = guessMessage.length();

        for (int i = 0; i <= cipherLen - guessLen; i++) {
            boolean valid = true;
            for (int j = 0; j < guessLen; j++) {
                if (cipherText.charAt(i + j) == guessMessage.charAt(j)) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                validMatches.add(cipherText.substring(i, i + guessLen));
            }
        }

        return validMatches;
    }
}
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class MessageFind {

    // Encrypt a single character with the current rotor settings
    public static char ascii_encryption(char input_letter, int[] encryption_code, Connection conn) throws SQLException {
        String gearA = "rotor_mappings_gearA";
        String gearB = "rotor_mappings_gearB";
        String gearC = "rotor_mappings_gearC";

        char mappedCharA = getMappedChar((char) encryption_code[0], conn, gearA);
        char mappedCharB = getMappedChar((char) encryption_code[1], conn, gearB);
        char mappedCharC = getMappedChar((char) encryption_code[2], conn, gearC);

        int codeA = (int) mappedCharA;
        int codeB = (int) mappedCharB;
        int codeC = (int) mappedCharC;

        int encrypted = input_letter;
        encrypted += codeA + codeB + codeC;

        while (encrypted > 122) encrypted -= 26;

        return (char) encrypted;
    }

    // Look up the mapped character from the rotor database
    public static char getMappedChar(char input, Connection conn, String gear) throws SQLException {
        String query;
        if (gear.equals("rotor_mappings_gearA")) {
            query = "SELECT output_char FROM " + gear + " WHERE input_char = ?";
        } else {
            query = "SELECT output_charB FROM " + gear + " WHERE input_charB = ?";
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, String.valueOf(Character.toUpperCase(input)));
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString(1).charAt(0) : input;
        }
    }

    // Finds all substrings of cipherText that don't match guess in the same positions
    public static ArrayList<String> analyzeAll(String cipherText, String guessMessage) {
        ArrayList<String> possibleMatches = new ArrayList<>();
        int guessLength = guessMessage.length();

        for (int i = 0; i <= cipherText.length() - guessLength; i++) {
            String sub = cipherText.substring(i, i + guessLength);

            boolean valid = true;
            for (int j = 0; j < guessLength; j++) {
                if (Character.toUpperCase(sub.charAt(j)) == Character.toUpperCase(guessMessage.charAt(j))) {
                    valid = false;
                    break;
                }
            }

            if (valid) {
                possibleMatches.add(sub);
            }
        }

        return possibleMatches;
    }
}
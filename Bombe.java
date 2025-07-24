import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class Bombe {

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:sqlite:my_databaseB.db";
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the Enigma-encrypted message:");
        String cipherText = scanner.nextLine().toLowerCase();

        // Load guess message from file
        String guessMessage = "";
        try (Scanner scanner1 = new Scanner(new File("guess_message.txt"))) {
            if (scanner1.hasNextLine()) {
                guessMessage = scanner1.nextLine().trim();
            }
        } catch (Exception e) {
            System.err.println("Failed to read guess message: " + e.getMessage());
            return;
        }

        // Get possible placements in cipherText where guess message could go
        ArrayList<String> possibleMatches = MessageFind.analyzeAll(cipherText, guessMessage);

        if (possibleMatches.isEmpty()) {
            System.out.println("No valid placements found in cipherText.");
            return;
        }

        // Connect to SQLite database
        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("Connected to database.");

            for (String subMessage : possibleMatches) {
                System.out.println("Trying placement: " + subMessage);

                for (int a = 0; a < 26; a++) {
                    for (int b = 0; b < 26; b++) {
                        for (int c = 0; c < 26; c++) {
                            int[] rotors = { a, b, c };
                            boolean match = true;

                            // Try decrypting the guessMessage and comparing to subMessage
                            for (int j = 0; j < guessMessage.length(); j++) {
                                char guessChar = guessMessage.charAt(j);

                                // Encrypt the guess character using current rotor settings
                                char encryptedChar = MessageFind.ascii_encryption(guessChar, rotors, conn);

                                // Compare to the encrypted subMessage from original message
                                if (encryptedChar != subMessage.charAt(j)) {
                                    match = false;
                                    break;
                                }

                                // Advance rotor A (like real Enigma)
                                rotors[0]++;
                                if (rotors[0] > 25) {
                                    rotors[0] = 0;
                                    rotors[1]++;
                                    if (rotors[1] > 25) {
                                        rotors[1] = 0;
                                        rotors[2]++;
                                        if (rotors[2] > 25) {
                                            rotors[2] = 0;
                                        }
                                    }
                                }
                            }

                            if (match) {
                                System.out.println("\nMATCH FOUND!");
                                System.out.println("Rotor Settings: A=" + a + ", B=" + b + ", C=" + c);
                                System.out.println("Decrypted Section: " + guessMessage);
                                return;
                            }
                        }
                    }
                }
            }

            System.out.println("No rotor settings produced a match.");

        } catch (SQLException e) {
            System.err.println("Database error:");
            e.printStackTrace();
        }
    }
}
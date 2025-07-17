import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Scanner;

public class Bombe {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:my_databaseB.db";
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the Enigma-encrypted message:");
        String cipherText = scanner.nextLine().toLowerCase();

        String guessMessage = "awaitfurtherorders";
        System.out.println("Trying guess message: " + guessMessage);

        ArrayList<String> possibleMatches = MessageFind.analyzeAll(cipherText, guessMessage);

        if (possibleMatches.isEmpty()) {
            System.out.println("No valid placements found in cipherText.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("Connected to database.");

            for (String subMessage : possibleMatches) {
                System.out.println("Trying placement: " + subMessage);

                for (int a = 0; a < 26; a++) {
                    for (int b = 0; b < 26; b++) {
                        for (int c = 0; c < 26; c++) {
                            int[] rotors = { a, b, c };
                            boolean match = true;

                            for (int j = 0; j < guessMessage.length(); j++) {
                                char encryptedChar = Main.ascii_encryption(guessMessage.charAt(j), rotors.clone(),
                                        conn);

                                if (encryptedChar != subMessage.charAt(j)) {
                                    match = false;
                                    break;
                                }

                                rotors[0]++;
                                if (rotors[0] > 25) {
                                    rotors[0] = 0;
                                    rotors[1]++;
                                    if (rotors[1] > 25) {
                                        rotors[1] = 0;
                                        rotors[2]++;
                                        if (rotors[2] > 25)
                                            rotors[2] = 0;
                                    }
                                }
                            }

                            if (match) {
                                System.out.println("MATCH FOUND!");
                                System.out.println("Rotor Settings: A=" + a + ", B=" + b + ", C=" + c);
                                System.out.println("Decrypted Section: " + guessMessage);
                                return;
                            }
                        }
                    }
                }
            }

            System.out.println("No rotor settings produced a match.");

        } catch (Exception e) {
            System.err.println("Error during Bombe execution:");
            e.printStackTrace();
        }
    }
}

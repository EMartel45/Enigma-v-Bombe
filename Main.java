import java.sql.*;

public class Main {

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

    public static char ascii_encryption(char input_letter, int[] encryption_code, Connection conn) throws SQLException {
        String gearA = "rotor_mappings_gearA";
        String gearB = "rotor_mappings_gearB";
        String gearC = "rotor_mappings_gearC";

        char mappedCharA = getMappedChar((char) encryption_code[0], conn, gearA);
        char mappedCharB = getMappedChar((char) encryption_code[1], conn, gearB);
        char mappedCharC = getMappedChar((char) encryption_code[2], conn, gearC);

        int CodeA = (int) mappedCharA;
        int CodeB = (int) mappedCharB;
        int CodeC = (int) mappedCharC;

        int encrypted = input_letter;
        encrypted += CodeA + CodeB + CodeC;

        while (encrypted > 122)
            encrypted -= 26;

        return (char) encrypted;
    }

    public static void main(String[] args) {
        String url = "jdbc:sqlite:my_databaseB.db";
        String plainText = "unitmobilized";
        char[] inputArray = plainText.toCharArray();

        int[] rotors = { 7, 19, 3 };
        char[] encryptedArray = new char[inputArray.length];

        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("Connected to DB.");
            for (int i = 0; i < inputArray.length; i++) {
                encryptedArray[i] = ascii_encryption(inputArray[i], rotors.clone(), conn);

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

            System.out.println("Encrypted result: " + String.valueOf(encryptedArray));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

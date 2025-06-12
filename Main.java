package org.example;
import java.sql.*;

public class Main {

    // Database method to get mapped character
    public static char getMappedChar(char input, Connection conn) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT output_char FROM rotor_mappings WHERE input_char = ?")) {

            stmt.setString(1, String.valueOf(Character.toUpperCase(input)));
            ResultSet rs = stmt.executeQuery();

            return rs.next() ? rs.getString(1).charAt(0) : input;
        }
    }

    // Updated encryption method with database lookup
    public static char ascii_encryption(char input_letter, int[] encryption_code, Connection conn) throws SQLException {
        // 1. Get database mapping (e.g., a→q)
        char mappedChar = getMappedChar(input_letter, conn);

        // 2. Apply numeric encryption
        int encrypted = (int) mappedChar;
        for (int i = 0; i < 3; i++) {
            encrypted += encryption_code[i];
            while (encrypted > 122) encrypted -= 26;
        }

        return (char) encrypted;
    }

    // Initialize database with default mappings
    public static void initializeMappings(Connection conn) throws SQLException {
        // Create table if not exists
        conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS rotor_mappings (" +
                        "  input_char CHAR PRIMARY KEY," +
                        "  output_char CHAR NOT NULL" +
                        ")");

        // Insert some default mappings (run once)
        String[][] GearA = {
                {"1", "15"}, {"2", "18"}, {"3", "5"}, {"4", "11"}, {"5", "9"},
                {"6", "7"}, {"7", "2"}, {"8", "4"}, {"9", "3"}, {"10", "20"},
                {"11", "4"}, {"12", "1"}, {"13", "17"}, {"14", "4"},  {"15", "11"},
                {"16", "2"}, {"17", "18"}, {"18", "44"}, {"19", "7"}, {"20", "31"},
                {"21", "7"}, {"22", "1"}, {"23", "52"}, {"24", "3"}, {"25", "2"}, {"26", "2"}
        };

        String[][] GearB = {
                {"1", "9"}, {"2", "11"}, {"3", "4"}, {"4", "7"}, {"5", "6"},
                {"6", "5"}, {"7", "2"}, {"8", "3"}, {"9", "14"}, {"10", "12"},
                {"11", "19"}, {"12", "15"}, {"13", "17"}, {"14", "13"},  {"15", "1"},
                {"16", "43"}, {"17", "22"}, {"18", "31"}, {"19", "26"}, {"20", "37"},
                {"21", "24"}, {"22", "16"}, {"23", "34"}, {"24", "42"}, {"25", "54"}, {"26", "39"}
        };

        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT OR IGNORE INTO rotor_mappings VALUES (?, ?)")) {

            for (String[] mapping : GearA) {
                stmt.setString(1, mapping[0]);
                stmt.setString(2, mapping[1]);
                stmt.executeUpdate();
            }
            for (String[] mapping : GearB) {
                stmt.setString(1, mapping[0]);
                stmt.setString(2, mapping[1]);
                stmt.executeUpdate();
            }
        }
    }

    public static void main(String[] args) {
        String url = "jdbc:sqlite:my_database.db";

        // Load JDBC driver
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found");
            e.printStackTrace();
            return;
        }

        String temp = "what is up";
        char[] tempArray = temp.toCharArray();
        int[] encryptionarray = {2, 9, 5};
        char[] encrypted = new char[tempArray.length];

        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("Connected to SQLite database!");

            // Initialize database with mappings (only need to do this once)
            initializeMappings(conn);

            // Encrypt using database mappings
            for (int i = 0; i < tempArray.length; i++) {
                if (tempArray[i] != ' ') {
                    encrypted[i] = ascii_encryption(tempArray[i], encryptionarray, conn);
                } else {
                    encrypted[i] = ' ';
                }
                encryptionarray[0] += 1;
            }

            // Debug: Print all mappings
            System.out.println("\nCurrent Mappings:");
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM rotor_mappings");
            while (rs.next()) {
                System.out.println(rs.getString("input_char") + " → " + rs.getString("output_char"));
            }

        } catch (SQLException e) {
            System.err.println("Database operation failed");
            e.printStackTrace();
        }

        String finalEncrypt = String.valueOf(encrypted);
        System.out.printf("\nThe test input was '%s' and the encrypted result is '%s'", temp, finalEncrypt);
    }
}

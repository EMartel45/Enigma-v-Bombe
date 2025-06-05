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
        String[][] defaultMappings = {
                {"A", "Q"}, {"B", "F"}, {"C", "L"}, {"D", "M"}, {"E", "P"},
                {"W", "G"}, {"H", "U"}, {"I", "V"}, {"T", "Z"}
        };

        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT OR IGNORE INTO rotor_mappings VALUES (?, ?)")) {

            for (String[] mapping : defaultMappings) {
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
package org.example;
import javax.swing.*;
import java.sql.*;
import java.util.Scanner;




public class Main {

    // Database method to get mapped character
    public static char getMappedChar(char input, Connection conn, String gear) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT output_char FROM " + gear + " WHERE input_char = ?")) {
            stmt.setString(1, String.valueOf(Character.toUpperCase(input)));
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getString(1).charAt(0) : input;
        }
    }

    // Updated encryption method with database lookup  input letter == a encryption code == position on gear
    public static char ascii_encryption(char input_letter, int[] encryption_code, Connection conn) throws SQLException {
        // 1. Get database mapping (e.g., a→q)
        String gearA = "rotor_mappings_gearA";
        char mappedCharA = getMappedChar((char) encryption_code[0], conn, gearA);
        String gearB = "rotor_mappings_gearA";
        char mappedCharB = getMappedChar((char) encryption_code[1], conn, gearB);
        String gearC = "rotor_mappings_gearA";
        char mappedCharC = getMappedChar((char) encryption_code[2], conn, gearC);
        // 2. Apply numeric encryption
        int CodeA = (int) mappedCharA;
        int CodeB = (int) mappedCharB;
        int CodeC = (int) mappedCharC;

        int encrypted = input_letter;
            encrypted += CodeA;
            encrypted += CodeB;
            encrypted += CodeC;
            while (encrypted > 122) encrypted -= 26;
            /*
            USED FOR TESTING THE LOOPING GEARS
        System.out.println(CodeA);
        System.out.println(CodeB);
        System.out.println(CodeC);
        System.out.println("---------");

             */
        return (char) encrypted;
    }

    // Initialize database with default mappings
    public static void initializeMappings(Connection conn) throws SQLException {
        // Create table if not exists
        conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS rotor_mappings_gearA (" +
                        "  input_char char PRIMARY KEY," +
                        "  output_char char NOT NULL" +
                        ")");
        conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS rotor_mappings_gearB (" +
                        "  input_charB char PRIMARY KEY," +
                        "  output_charB char NOT NULL" +
                        ")");
        conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS rotor_mappings_gearC (" +
                        "  input_charB char PRIMARY KEY," +
                        "  output_charB char NOT NULL" +
                        ")");

        // Insert some default mappings (run once)
        String[][] GearA = {
                {"1", "16"}, {"2", "18"}, {"3", "5"}, {"4", "11"}, {"5", "9"},
                {"6", "7"}, {"7", "2"}, {"8", "4"}, {"9", "3"}, {"10", "20"},
                {"11", "4"}, {"12", "1"}, {"13", "17"}, {"14", "4"},  {"15", "11"},
                {"16", "2"}, {"17", "18"}, {"18", "44"}, {"19", "7"}, {"20", "31"},
                {"21", "7"}, {"22", "1"}, {"23", "52"}, {"24", "3"}, {"25", "2"}, {"26", "2"}
        };

        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT OR IGNORE INTO rotor_mappings_gearA VALUES (?, ?)")) {

            for (String[] mapping : GearA) {
                stmt.setString(1, mapping[0]);
                stmt.setString(2, mapping[1]);
                stmt.executeUpdate();
            }
        }


        String[][] GearB = {
                {"1", "9"}, {"2", "11"}, {"3", "4"}, {"4", "7"}, {"5", "6"},
                {"6", "5"}, {"7", "2"}, {"8", "3"}, {"9", "14"}, {"10", "12"},
                {"11", "19"}, {"12", "15"}, {"13", "17"}, {"14", "13"},  {"15", "1"},
                {"16", "43"}, {"17", "22"}, {"18", "31"}, {"19", "26"}, {"20", "37"},
                {"21", "24"}, {"22", "16"}, {"23", "34"}, {"24", "42"}, {"25", "54"}, {"26", "39"}
        };

        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT OR IGNORE INTO rotor_mappings_gearB VALUES (?, ?)")) {

            for (String[] mapping : GearB) {
                stmt.setString(1, mapping[0]);
                stmt.setString(2, mapping[1]);
                stmt.executeUpdate();
            }
        }

        String[][] GearC = {
                {"1", "6"}, {"2", "1"}, {"3", "64"}, {"4", "21"}, {"5", "17"},
                {"6", "21"}, {"7", "28"}, {"8", "6"}, {"9", "9"}, {"10", "7"},
                {"11", "8"}, {"12", "4"}, {"13", "13"}, {"14", "15"},  {"15", "14"},
                {"16", "13"}, {"17", "6"}, {"18", "3"}, {"19", "26"}, {"20", "13"},
                {"21", "2"}, {"22", "18"}, {"23", "4"}, {"24", "2"}, {"25", "10"}, {"26", "20"}
        };

        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT OR IGNORE INTO rotor_mappings_gearC VALUES (?, ?)")) {

            for (String[] mapping : GearC) {
                stmt.setString(1, mapping[0]);
                stmt.setString(2, mapping[1]);
                stmt.executeUpdate();
            }
        }


    }


    public static int[] GuiFunction(){
        int[] encryptionarray = new int[] {1, 1, 1};
        JFrame frame = new JFrame("Manual Wheel + Peg Board");
        ManualWheelAndPegBoard panel = new ManualWheelAndPegBoard(encryptionarray, frame);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 750);
        frame.setVisible(true);

        // Wait for OK button to be pressed
        while (frame.isDisplayable() && !panel.isOkPressed()) {
            try {
                Thread.sleep(100); // Small delay to prevent busy waiting
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        // Get the final wheel values
        int[] finalValues = panel.getWheelValues();
        System.out.println("Final wheel values: " + finalValues[0] + ", " + finalValues[1] + ", " + finalValues[2]);
        return finalValues;
    }



    public static void main(String[] args) {
        String url = "jdbc:sqlite:my_databaseB.db";

        // Load JDBC driver
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found");
            e.printStackTrace();
            return;
        }


        //TEST SECTION
        int[] encryptionarray = GuiFunction();

        // END TEST SECTION


        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter the message to be encoded:");
        String temp = myObj.nextLine();  // Read user input

        char[] tempArray = temp.toCharArray();

      //  int positionA = 23;
      //  int positionB = 1;
      //  int positionC = 1;

        //int[] encryptionarray = {positionA, positionB, positionC};
        //new array based on length of the user input changed into an array of characters.
        char[] encrypted = new char[tempArray.length];

        try (Connection conn = DriverManager.getConnection(url)) {
           System.out.println("Connection successful");

            // Initialize database with mappings (only need to do this once)
            initializeMappings(conn);

            // Encrypt using database mappings
          //  boolean useGearA = true;
            for (int i = 0; i < tempArray.length; i++) {
                if (tempArray[i] != ' ') {

                   // String currentGear = useGearA ? "rotor_mappings_gearA" : "rotor_mappings_gearB";
                    encrypted[i] = ascii_encryption(tempArray[i], encryptionarray, conn);

                } else {
                    encrypted[i] = ' ';
                }

                encryptionarray[0] += 1;

                if (encryptionarray[0]>26){
                    encryptionarray[0]=0;
                   encryptionarray[1] += 1;
                }

                else if (encryptionarray[1]>26){
                    encryptionarray[1]=1;
                    encryptionarray[2]+=1;
                }

                 if (encryptionarray[2]>26){
                    encryptionarray[2]=1;
                }


            }


            // Debug: Print all mappings
            /*
            USE FOR TESTING GEAR MAPPINGS
            System.out.println("\nCurrent Mappings:");
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM rotor_mappings_gearA");

            while (rs.next()) {
                System.out.println(rs.getString("input_char") + " → " + rs.getString("output_char"));

            }

             */

        } catch (SQLException e) {
            System.err.println("Database operation failed");
            e.printStackTrace();
        }

        String finalEncrypt = String.valueOf(encrypted);
        System.out.printf("\nThe test input was '%s' and the encrypted result is '%s'", temp, finalEncrypt);
    }
    //TEST STUFF HERE

}



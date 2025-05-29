public class StringToCharArray {
    public static void main(String[] args) {
        String str = "Hello World";

        // Using the toCharArray() method
        char[] charArray = str.toCharArray();

        // Printing the character array
        System.out.println("Character array: ");
        for (char c : charArray) {
            System.out.print(c + " ");
        }
    }
}


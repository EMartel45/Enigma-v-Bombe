public class Main {
    public static void main(String[] args) throws Exception {
        Time time1 = new Time(); 
        StringtoCharArray convert = new StringtoCharArray(); 
        System.out.println(time1.messageGuess()); 
        String myGuessMessage = time1.messageGuess(); 
        char[] guessMessageChar =  convert.stringToArray(myGuessMessage); 

        StringtoCharArray converter = new StringtoCharArray(); 

        String input = "zxyabcdefghijklmnoqrstuvwxyzattacknow"; 
        char[] message = converter.stringToArray(input); 

        int[] result = MessageFind.findMessage(message, guessMessageChar);

        System.out.println("Valid starting indices: "); 
        for (int index : result) {
            int endIndex = index + myGuessMessage.length() + 1; // go one character past the guess
            if (endIndex <= input.length()) {
                String sub = input.substring(index, endIndex);
                System.out.println("From index " + index + " to " + (endIndex - 1) + ": " + sub);
                // You can store this in a list or array if you need to keep it later
            } else {
                System.out.println("Index " + index + " goes out of bounds for substring.");
            }
        }
    }
}

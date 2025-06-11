public class Main {
    public static void main(String[] args) throws Exception {
        Time time1 = new Time(); 
        time1.messageGuess();  

        char[] guessMessage = {'D', 'N', 'X'}; 
        char[] oldMessage = {'B', 'N', 'D', 'X', 'Y', 'Z'};

        messageFind mm = new messageFind(); 
        System.out.println(mm.findMessage(oldMessage, guessMessage));
    }
}

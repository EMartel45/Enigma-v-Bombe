public class messageFind {
    public int findMessage(char[] enigmaArray, char[] guessMessage) {
        int count = 0; 
        
        for(char letter : enigmaArray){
            for(char guess : guessMessage){
                if (letter != guess){
                    count++; 
                }
            }
        }

        return count;
    }
}

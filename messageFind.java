public class messageFind {
    public int findMessage(char[] enigmaArray, char[] guessMessage) {
        int count = 0; 
        
        for (int i = 0; i <= enigmaArray.length - guessMessage.length; i++) {
            boolean hasCommonChar = false; 

            for (int j = 0; j < guessMessage.length; j++) {
                if (enigmaArray[i + j] == guessMessage[j]) {
                    hasCommonChar = true; 
                    break; 
                }
            }
            if (hasCommonChar) {
                count++; 
            } 
        }

        return count;
    }
}

import java.util.ArrayList;
import java.util.List; 

public class MessageFind {
    public static int[] findMessage(char[] enigmaArray, char[] guessMessage) {
        List<Integer> resultList = new ArrayList<>(); 
        
        for (int i = 0; i <= enigmaArray.length - guessMessage.length; i++) {
            boolean hasCommonChar = false; 

            for (int j = 0; j < guessMessage.length; j++) {
                if (enigmaArray[i + j] == guessMessage[j]) {
                    hasCommonChar = true; 
                    break; 
                }
            }
            if (!hasCommonChar) {
                resultList.add(i); 
            } 
        }

        // Convert to int[]
        int[] result = new int[resultList.size()]; 
        for (int i = 0; i < resultList.size(); i++) {
            result[i] = resultList.get(i); 
        }

        return result;
    }
}

import java.time.LocalTime;
import java.util.Random; 

public class Time {
    public String messageGuess() {

        Random rand = new Random(); 

        LocalTime now = LocalTime.now();
        LocalTime earlyMorning = LocalTime.of(5,0);
        LocalTime midDay = LocalTime.of(8,0);
        LocalTime evening = LocalTime.of(12,0);
        LocalTime night = LocalTime.of(19,0);

        String[] morningMessages = new String[5]; 
        String[] afternoonMessages = new String[5]; 
        String[] eveningMessages = new String[5]; 
        String[] nightMessages = new String[5]; 

        int randomIntRounded = rand.nextInt(5);
        
        if(now.isAfter(earlyMorning) && now.isBefore(midDay)) {
            morningMessages[0] = "goodmorning"; 
            morningMessages[1] = "weatherconditionstable";
            morningMessages[2] = "attackatfivehours";
            morningMessages[3] = "messagerecievedunderstood";
            morningMessages[4] = "supplyinroute";

            return morningMessages[randomIntRounded]; 

        } else if(now.isAfter(midDay) && now.isBefore(evening)) {
            afternoonMessages[0] = "goodafternoon";
            afternoonMessages[1] = "supplydelayed";
            afternoonMessages[2] = "antiaircraftready";
            afternoonMessages[3] = "holdpositionuntilorder"; 
            afternoonMessages[4] = "radiomessageinterrupted";

            return afternoonMessages[randomIntRounded]; 
        } else if(now.isAfter(evening) && now.isBefore(night)) {
            eveningMessages[0] = "goodevening";
            eveningMessages[1] = "tanksmovedwest";
            eveningMessages[2] = "lostcontactwithunit";
            eveningMessages[3] = "invasionbeginstonight";
            eveningMessages[4] = "arrivalattwentytwohours";

            return eveningMessages[randomIntRounded]; 
        } else {
            nightMessages[0] = "goodnight";
            nightMessages[1] = "codechangedtomorrow";
            nightMessages[2] = "retreatapproved";
            nightMessages[3] = "fuelsufficientfortwodays";
            nightMessages[4] = "nomoreammunition";

            return nightMessages[randomIntRounded]; 
        }
    }
}

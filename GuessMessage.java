import java.time.LocalTime;

public class GuessMessage {
    public static void main(String[] args) 
    {
        LocalTime now = LocalTime.now();
        LocalTime earlyMorning = LocalTime.of(5,0);
        LocalTime midDay = LocalTime.of(8,0);
        LocalTime evening = LocalTime.of(12,0);
        LocalTime night = LocalTime.of(19,0);
        
        
        if(now.isAfter(earlyMorning) && now.isBefore(midDay)) {
            System.out.println("Good Morning!");
        } else if(now.isAfter(midDay) && now.isBefore(evening)) {
            System.out.println("Good Afternoon!");
        } else if(now.isAfter(evening) && now.isBefore(night)) {
            System.out.println("Good Evening!");
        } else {
            System.out.println("Good Night!");
        }
    }
}
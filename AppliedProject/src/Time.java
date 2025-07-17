import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;

public class Time {

    public static String messageGuess() {
        ArrayList<String> current = getCurrentTimeBlockMessages();
        Random rand = new Random();
        return current.get(rand.nextInt(current.size()));
    }

    public static ArrayList<String> getAllGuessMessages() {
        ArrayList<String> all = new ArrayList<>();
        for (String s : getMorning())
            all.add(s);
        for (String s : getAfternoon())
            all.add(s);
        for (String s : getEvening())
            all.add(s);
        for (String s : getNight())
            all.add(s);
        return all;
    }

    public static ArrayList<String> getCurrentTimeBlockMessages() {
        LocalTime now = LocalTime.now();
        LocalTime earlyMorning = LocalTime.of(5, 0);
        LocalTime midDay = LocalTime.of(8, 0);
        LocalTime evening = LocalTime.of(12, 0);
        LocalTime night = LocalTime.of(19, 0);

        if (now.isAfter(earlyMorning) && now.isBefore(midDay)) {
            return getMorning();
        } else if (now.isAfter(midDay) && now.isBefore(evening)) {
            return getAfternoon();
        } else if (now.isAfter(evening) && now.isBefore(night)) {
            return getEvening();
        } else {
            return getNight();
        }
    }

    private static ArrayList<String> getMorning() {
        return new ArrayList<String>() {
            {
                add("goodmorning");
                add("weatherconditionstable");
                add("attackatfivehours");
                add("messagerecievedunderstood");
                add("supplyinroute");
            }
        };
    }

    private static ArrayList<String> getAfternoon() {
        return new ArrayList<String>() {
            {
                add("enemyretreating");
                add("reinforcementsneeded");
                add("holdtheline");
                add("securethebridge");
                add("airstrikeimminent");
            }
        };
    }

    private static ArrayList<String> getEvening() {
        return new ArrayList<String>() {
            {
                add("meetingatdusk");
                add("retreattobase");
                add("coordinatesreceived");
                add("awaitfurtherorders");
                add("unitmobilized");
            }
        };
    }

    private static ArrayList<String> getNight() {
        return new ArrayList<String>() {
            {
                add("coverofdarkness");
                add("finalassaultplanned");
                add("enemyweaknessdetected");
                add("operationcommence");
                add("sendbackup");
            }
        };
    }
}

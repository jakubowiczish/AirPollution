package AirPollution;

import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}

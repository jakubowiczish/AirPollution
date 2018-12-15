package AirPollution;

import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }


//    Map<String, Object> map = new HashMap<String, Object>();

    //        map = (Map<String, Object>) gson.fromJson(jsonFetcher.getQualityIndex(52), map.getClass());
//
//        for (Map.Entry<String, Object> entry : map.entrySet()) {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        }

}

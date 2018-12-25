package AirPollution;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class Utils {

    private static final SimpleDateFormat usedDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    final static ArrayList<String> parameters = new ArrayList<>() {{
        add("NO2");
        add("O3");
        add("PM10");
        add("SO2");
        add("C6H6");
        add("CO");
        add("PM2.5");
    }};

    static boolean checkWhetherStationExists(ArrayList<Station> allStations, String stationName) {
        for (Station station : allStations) {
            if (station.stationName.equals(stationName)) {
                return true;
            }
        }
        return false;
    }

    static ArrayList<Station> validStations(ArrayList<String> listOfStations, ArrayList<Station> allStations) {
        ArrayList<Station> validStations = new ArrayList<>();
        if (listOfStations.size() > 0) {
            for (String stationName : listOfStations) {
                for (Station station : allStations) {
                    if (station.stationName.equals(stationName) && !validStations.contains(station)) {
                        validStations.add(station);
                    }
                }
            }
        }
        return validStations;
    }

    public static ArrayList<Station> assignValidStations(ArrayList<String> listOfStations, ArrayList<Station> allStations) {
        ArrayList<Station> validStations = null;
        if (listOfStations != null && listOfStations.size() > 0) {
            validStations = Utils.validStations(listOfStations, allStations);
        }
        return validStations;
    }

    public static ArrayList<Station> assignAllStations(ArrayList<Station> allStations, ArrayList<Station> validStations) {
        if (validStations != null && validStations.size() > 0) {
            return validStations;
        }
        return allStations;
    }


    static boolean checkWhetherParameterNameIsValid(String parameterName) {
        return parameters.contains(parameterName);
    }

    static Date multiThreadParseStringToDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (ParseException e) {
            System.out.println("The date: " + date + " could not be parsed");
            e.printStackTrace();
        }
        return null;
    }


    static String convertDateToString(Date date) {
        return usedDateFormat.format(date);
    }

    static Date parseStringToDate(String date) {
        try {
            return usedDateFormat.parse(date);
        } catch (ParseException e) {
            System.out.println("The date: " + date + " could not be parsed");
            e.printStackTrace();
        }
        return null;
    }

    static boolean checkDateInterval(Date startDate, Date endDate, Date actualDate) {
        if (actualDate != null) {
            return (actualDate.before(endDate) || actualDate.equals(endDate)) &&
                    (actualDate.after(startDate) || actualDate.equals(startDate));
        }
        return false;
    }

    static Date parseAndCheckDate(String dateString) {
        Date date = parseStringToDate(dateString);
        if (date == null) {
            throw new IllegalArgumentException("This date: " + dateString + " is not valid");
        }
        return date;
    }

    public static void startAndJoinThreads(LinkedList<Thread> threads) {
        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Interrupted thread: " + thread.getId());
                e.printStackTrace();
            }
        }
    }
}

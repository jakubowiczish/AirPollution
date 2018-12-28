package AirPollution.utils;

import AirPollution.model.Station;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.TreeMap;

public class Utils {

    private static final SimpleDateFormat usedDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public final static ArrayList<String> parameters = new ArrayList<>() {{
        add("NO2");
        add("O3");
        add("PM10");
        add("SO2");
        add("C6H6");
        add("CO");
        add("PM2.5");
    }};

    public static synchronized void addToList(TreeMap<Double, ArrayList<String>> treeMap, Double key, String value) {
        ArrayList<String> list = treeMap.get(key);

        if (list == null) {
            list = new ArrayList<>();
            list.add(value);
            treeMap.put(key, list);
        } else {
            if (!list.contains(value)) {
                list.add(value);
            }
        }
    }

    static boolean checkWhetherStationExists(ArrayList<Station> allStations, String stationName) {
        for (Station station : allStations) {
            if (station.stationName.equals(stationName)) {
                return true;
            }
        }
        return false;
    }

    static ArrayList<Station> checkValidStations(ArrayList<String> listOfStations, ArrayList<Station> allStations) {
        ArrayList<Station> validStations = new ArrayList<>();

        if (listOfStations.size() > 0) {
            for (String stationName : listOfStations) {
                boolean stationNameFound = false;
                for (Station station : allStations) {
                    if (station.stationName.equals(stationName) && !validStations.contains(station)) {
                        validStations.add(station);
                        stationNameFound = true;
                    }
                }
                if (!stationNameFound) {
                    System.out.println("There is no such station in the system as: " + stationName);
                }
            }
        }
        return validStations;
    }

    public static ArrayList<Station> assignValidStations(ArrayList<String> listOfStations, ArrayList<Station> allStations) {
        ArrayList<Station> validStations = null;
        if (listOfStations != null && listOfStations.size() > 0) {
            validStations = Utils.checkValidStations(listOfStations, allStations);
        }
        return validStations;
    }

    public static ArrayList<Station> assignAllStations(ArrayList<Station> allStations, ArrayList<Station> validStations) {
        if (validStations != null && validStations.size() > 0) {
            return validStations;
        }
        return allStations;
    }


    public static boolean checkWhetherParameterNameIsValid(String parameterName) {
        return parameters.contains(parameterName);
    }

    public static Date multiThreadParseStringToDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (ParseException e) {
            System.out.println("The date: " + date + " could not be parsed");
            e.printStackTrace();
        }
        return null;
    }


    public static String convertDateToString(Date date) {
        return usedDateFormat.format(date);
    }

    public static Date parseStringToDate(String date) {
        try {
            return usedDateFormat.parse(date);
        } catch (ParseException e) {
            System.out.println("The date: " + date + " could not be parsed");
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkDateInterval(Date beginDate, Date endDate, Date actualDate) {
        if (actualDate != null) {
            return (actualDate.before(endDate) || actualDate.equals(endDate)) &&
                    (actualDate.after(beginDate) || actualDate.equals(beginDate));
        }
        return false;
    }

    public static Date parseAndCheckDate(String dateString) {
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

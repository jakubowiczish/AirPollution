package AirPollution.utils;

import AirPollution.model.Station;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Class that contains various methods and fields that may be useful for other classes
 */
public class Utils {

    public static DecimalFormat decimalFormat = new DecimalFormat("#0.0000");
    private static final SimpleDateFormat usedDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat usedHourDateFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * List of parameters used in the system
     */
    public final static ArrayList<String> parameters = new ArrayList<>() {{
        add("NO2");
        add("O3");
        add("PM10");
        add("SO2");
        add("C6H6");
        add("CO");
        add("PM2.5");
    }};

    /**
     * Adds given key and value to given TreeMap
     *
     * @param treeMap tree map that contains Double and ArrayList<String>
     * @param key     key
     * @param value   value
     */
    public static synchronized void addToTreeWithDoubleAndString(TreeMap<Double, ArrayList<String>> treeMap, Double key, String value) {
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


    /**
     * Adds given key and value to given TreeMap
     *
     * @param treeMap tree map that contains Date and ArrayList<String>
     * @param key     key
     * @param value   value
     */
    public static synchronized void addToTreeWithDateAndString(TreeMap<Date, ArrayList<String>> treeMap, Date key, String value) {
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


    /**
     * Checks whether given station name exists in the system
     *
     * @param allStations list of all stations currently available in the system
     * @param stationName station name that is to be checked
     * @return true if stationName exists in the system, false otherwise
     */
    public static boolean checkWhetherStationExists(ArrayList<Station> allStations, String stationName) {
        for (Station station : allStations) {
            if (station.getStationName().equals(stationName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns list of valid stations out of given list of station names
     *
     * @param listOfStations given names of stations
     * @param allStations    list of all stations currently available in the system
     * @return ArrayList of stations that contains all stations with valid names that occur in the system
     */
    public static ArrayList<Station> checkValidStations(ArrayList<String> listOfStations, ArrayList<Station> allStations) {
        ArrayList<Station> validStations = new ArrayList<>();

        if (listOfStations.size() > 0) {
            for (String stationName : listOfStations) {
                boolean stationNameFound = false;
                for (Station station : allStations) {
                    if (station.getStationName().equals(stationName) && !validStations.contains(station)) {
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

    /**
     * Returns list of valid stations if length of the given list of stations is bigger than 0
     *
     * @param listOfStations given names of stations
     * @param allStations    list of all stations currently available in the system
     * @return
     */
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


    /**
     * Checks whether given parameter name is valid
     *
     * @param parameterName name of parameter which we want to check
     * @return true if given name of parameter is valid, false otherwise
     */
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


    /**
     * Converts given Date to String format
     *
     * @param date date that is to be converted
     * @return String in format "yyyy-MM-dd HH:mm:ss"
     */
    public static String convertDateToString(Date date) {
        return usedDateFormat.format(date);
    }

    /**
     * Converts given String in format "yyyy-MM-dd HH:mm:ss" to Date
     *
     * @param date String in format "yyyy-MM-dd HH:mm:ss" that will be converted
     * @return Date variable parsed from given String
     */
    public static Date parseStringToDate(String date) {
        try {
            return usedDateFormat.parse(date);
        } catch (ParseException e) {
            System.out.println("The date: " + date + " could not be parsed");
        }
        return null;
    }

    /**
     * Converts given String in format "HH:mm:ss" to Date
     *
     * @param date String in format "HH:mm:ss" that will be converted
     * @return Date variable parsed from given String
     */
    public static Date parseStringToHour(String date) {
        try {
            return usedHourDateFormat.parse(date);
        } catch (ParseException e) {
            System.out.println("The date: " + date + " could not be parsed");
        }
        return null;
    }


    /**
     * Checks whether actualDate is between beginDate and endDate
     *
     * @param beginDate  begin date of period of time
     * @param endDate    end date of period of time
     * @param actualDate date that is to be checked know whether it is between beginDate and endDate
     * @return true if actualDate is between beginDate and endDate, false otherwise
     */
    public static boolean checkDateInterval(Date beginDate, Date endDate, Date actualDate) {
        if (actualDate != null) {
            return (actualDate.before(endDate) || actualDate.equals(endDate)) &&
                    (actualDate.after(beginDate) || actualDate.equals(beginDate));
        }
        return false;
    }

    /**
     * Checks whether actualDate is after sinceWhenDate
     *
     * @param sinceWhenDate date from which actualDate will be checked
     * @param actualDate date that is to be checked
     * @return true if actualDate is after sinceWhenDate, false otherwise
     */
    public static boolean checkSinceWhenDate(Date sinceWhenDate, Date actualDate) {
        if (actualDate != null) {
            return actualDate.after(sinceWhenDate) || actualDate.equals(sinceWhenDate);
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

    /**
     * Starts every thread in given list of threads and joins them
     *
     * @param threads list of threads that will start and join later
     */
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

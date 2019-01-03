package AirPollution.optionHandler;

import AirPollution.storage.Storage;
import AirPollution.utils.Utils;
import AirPollution.model.*;
import com.google.common.util.concurrent.AtomicDouble;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Class that is responsible with handling commandline arguments about parameters
 */
public class ParameterOptionHandler {
    private Storage storageReceiver;

    public ParameterOptionHandler(Storage storageReceiver) {
        this.storageReceiver = storageReceiver;
    }

    /**
     * Provides user with information about the value of given parameter
     * for specified, given names of the stations and given date in format yyyy-MM-dd HH:mm:ss
     *
     * @param date           we convey this date when we want to check value of given parameter for this specific date,
     *                       in format yyyy-MM-dd HH:mm:ss
     * @param listOfStations list of stations for which we want to check parameter values
     * @param parameterName  name of the parameter which value we want to get to know
     * @return String that has in itself value of given parameter for given stations and date
     */
    public String valueOfGivenParameterForGivenStationsAndDate(String date, ArrayList<String> listOfStations, String parameterName) {
        if (!Utils.checkWhetherParameterNameIsValid(parameterName)) {
            System.out.println("Invalid parameter name: " + parameterName);
            return null;
        }

        ArrayList<Station> allStations = storageReceiver.getAllStations();

        StringBuilder stringBuilder = new StringBuilder();

        ArrayList<Station> validStations = Utils.assignValidStations(listOfStations, allStations);
        allStations = Utils.assignAllStations(allStations, validStations);

        stringBuilder.append("For Parameter: ").append(parameterName).append("\nDate: ").append(date).append("\n");

        ConcurrentSkipListMap<Double, String> valuesOfParameterForGivenStationsAndDate = new ConcurrentSkipListMap<>();

        for (Station station : allStations) {
            if (station == null) continue;
            int stationID = Station.returnIdOfGivenStation(allStations, station.getStationName());

            CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(stationID);
            if (sensors == null) continue;

            boolean foundParameter = false;

            for (Sensor sensor : sensors) {
                foundParameter = true;
                SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.getId());
                if (sensorData == null) continue;
                if (sensorData.getKey().equals(parameterName)) {
                    boolean validDate = false;
                    for (SensorData.Value value : sensorData.getValues()) {
                        if (value == null) continue;
                        if (value.date.equals(date)) {
                            validDate = true;
                            if (value.value == null) {
                                valuesOfParameterForGivenStationsAndDate.
                                        put(-1.0, " NULL pollution value of parameter: " +
                                                parameterName + " for station: \"" + station.getStationName() + "\"\n");
                            } else {
                                valuesOfParameterForGivenStationsAndDate.
                                        put(value.value, " - pollution value of parameter: " +
                                                parameterName + " for station: \"" + station.getStationName() + "\"\n");
                            }
                        }
                    }
                    if (!validDate) {
                        System.out.println("There is no such date as \"" + date + "\"  in the system for station: " + station.getStationName());
                    }
                }
            }
            if (!foundParameter) {
                System.out.println("There is no such parameter as \"" + parameterName + "\" in the system for station: " + station.getStationName());
            }
        }
        for (Map.Entry<Double, String> entry : valuesOfParameterForGivenStationsAndDate.entrySet()) {
            stringBuilder.append(Utils.decimalFormat.format(entry.getKey())).append(entry.getValue());
        }

        return stringBuilder.toString();
    }

    /**
     * Provides user with information about the value of all parameters that occur in the system
     * for specified, given names of the stations and given date in format yyyy-MM-dd HH:mm:ss
     *
     * @param date           we convey this date when we want to check value of all parameters in the system for this specific date,
     *                       in format yyyy-MM-dd HH:mm:ss
     * @param listOfStations list of stations for which we want to check parameter values
     * @return String that has in itself values of all parameters in the system for given stations and date
     */
    public String valueOfAllParametersForGivenStationsAndDate(String date, ArrayList<String> listOfStations) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("VALUES OF ALL PARAMETERS: ").append("\n");

        for (String parameter : Utils.parameters) {
            String value = valueOfGivenParameterForGivenStationsAndDate(date, listOfStations, parameter);
            if (value.length() < 50)
                continue; // means that we did not add any pollution values, just communicate about which parameter is being checked
            stringBuilder.append(value);
        }
        return stringBuilder.toString();
    }

    /**
     * Returns String that contains information about which parameter was
     * the most fluctuating one since given date for given list of stations
     * If no stations are given, list of stations becomes list of all stations available in the system
     *
     * @param sinceWhenString date since when we want to check which parameter was the most fluctuating one in the system,
     *                        in format yyyy-MM-dd HH:mm:ss
     * @param listOfStations  list of stations for which we want to check the most fluctuating parameter
     *                        if list is empty, method is executed for all available stations
     * @return Information about which parameter was the most fluctuating one, with the difference between maximum and minimum value of pollution
     */
    public String mostFluctuatingParameter(String sinceWhenString, ArrayList<String> listOfStations) {
        Date sinceWhenDate = Utils.parseStringToDate(sinceWhenString);
        if (sinceWhenDate == null) {
            System.out.println("This date is not valid: " + sinceWhenString);
            return null;
        }

        ArrayList<Station> allStations = storageReceiver.getAllStations();
        ArrayList<Station> validStations = Utils.assignValidStations(listOfStations, allStations);
        allStations = Utils.assignAllStations(allStations, validStations);

        String resultParameter = null;

        double resultMinValue = 10000;
        double resultMaxValue = -1;
        double maxDifference = -1;

        boolean foundDate = false;

        for (Station station : allStations) {

            CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.getId());
            if (sensors == null) continue;

            for (Sensor sensor : sensors) {
                SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.getId());
                if (sensorData == null) continue;
                if (sensorData.getValues().length == 0) continue;

                for (SensorData.Value value : sensorData.getValues()) {
                    if (value.value == null || !value.date.contains("-")) continue;

                    Date actualDate = Utils.multiThreadParseStringToDate(value.date);
                    if (actualDate == null) continue;

                    if (Utils.checkSinceWhenDate(sinceWhenDate, actualDate)) {
                        foundDate = true;
                        if (value.value < resultMinValue && value.value > 0) {
                            resultMinValue = value.value;
                        }
                        if (value.value > resultMaxValue) {
                            resultMaxValue = value.value;

                        }
                    }
                }
                double difference = resultMaxValue - resultMinValue;

                if (maxDifference < difference) {
                    maxDifference = difference;
                    resultParameter = sensorData.getKey();
                }
            }
        }

        if (!foundDate) {
            System.out.println("Date: " + sinceWhenString + " was not found in the system");
            return null;
        }

        StringBuilder resultString = new StringBuilder();
        if (validStations != null && validStations.size() > 0) {
            StringBuilder stationNames = new StringBuilder();
            for (Station station : validStations) {
                stationNames.append(station.getStationName()).append("\n");
            }
            resultString.
                    append("Most fluctuating parameter since \"").append(sinceWhenString).
                    append("\" for stations:\n").append(stationNames).append("is ");
        } else {
            resultString.
                    append("Most fluctuating parameter since \"").
                    append(sinceWhenString).append("\" for all stations is ");
        }

        return resultString + resultParameter +
                ",\nthe difference between maximum and minimum pollution for this parameter amounts to: " + Utils.decimalFormat.format(maxDifference) + ",\n" +
                "with maximum value of: " + Utils.decimalFormat.format(resultMaxValue) + " and minimum value of: " + Utils.decimalFormat.format(resultMinValue);
    }


    /**
     * Provides user with information about the name of the parameter
     * which has the lowest value of pollution at specific time - time that is given in an argument
     *
     * @param date we convey this date when we want to check which parameter has lowest value at this specific time,
     *             in format yyyy-MM-dd HH:mm:ss
     * @return name of the parameter that has lowest value at specific date given in an argument
     */
    public String parametersWithLowestAndHighestValuesAtSpecificTime(String date) {
        Date specificDate = Utils.parseAndCheckDate(date);

        ArrayList<Station> allStations = storageReceiver.getAllStations();

        String lowestParameterName = null, highestParameterName = null;
        String lowestStationName = null, highestStationName = null;

        double resultMinValue = 10000;
        double resultMaxValue = -1;

        for (Station station : allStations) {

            CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.getId());
            if (sensors == null) continue;

            for (Sensor sensor : sensors) {
                SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.getId());

                if (sensorData.getValues().length == 0) continue;

                for (SensorData.Value value : sensorData.getValues()) {
                    if (value.value == null) continue;

                    if (!value.date.contains("-")) continue;
                    Date actualDate = Utils.multiThreadParseStringToDate(value.date);
                    if (actualDate == null) continue;

                    if (actualDate.equals(specificDate)) {
                        if (value.value < resultMinValue && value.value > 0) {
                            resultMinValue = value.value;
                            lowestParameterName = sensorData.getKey();
                            lowestStationName = station.getStationName();
                        }

                        if (value.value > resultMaxValue) {
                            resultMaxValue = value.value;
                            highestParameterName = sensorData.getKey();
                            highestStationName = station.getStationName();
                        }
                    }
                }

            }
        }

        String lowestValueString = "Parameter with lowest value on \"" + date + "\" is " + lowestParameterName +
                " and its value is: " + Utils.decimalFormat.format(resultMinValue) + ", it occurs for station: " + lowestStationName;
        String highestValueString = "\nParameter with highest value on \"" + date + "\" is " + highestParameterName +
                " and its value is: " + Utils.decimalFormat.format(resultMaxValue) + ", it occurs for station: " + highestStationName;

        return lowestValueString + highestValueString;
    }


    /**
     * Returns String that contains N stations, sorted by pollution value in ascending order,
     * where N is number given in an argument
     *
     * @param listOfStations list of stations for which we want to have sorted stations
     * @param date           we convey this date when we want to have the list of sorted stations,
     *                       *             in format yyyy-MM-dd HH:mm:ss
     * @param parameterName  name of the parameter for which we want to have desired information
     * @param N              number of stations that we want them to be printed
     * @return String containing sorted stations in ascending order
     */
    public String sortedStations(ArrayList<String> listOfStations, String date, String parameterName, int N) {
        Date realDate = Utils.parseAndCheckDate(date);

        if (!Utils.checkWhetherParameterNameIsValid(parameterName)) {
            System.out.println("Parameter name: " + parameterName + " is not valid");
            return null;
        }

        ArrayList<Station> allStations = storageReceiver.getAllStations();
        ArrayList<Station> validStations = Utils.assignValidStations(listOfStations, allStations);
        allStations = Utils.assignAllStations(allStations, validStations);

        TreeMap<Double, ArrayList<String>> parameterDataAtSpecificTime = new TreeMap<>();

        for (Station station : allStations) {
            CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.getId());
            if (sensors == null) continue;

            for (Sensor sensor : sensors) {
                SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.getId());
                if (sensorData == null) continue;
                if (!sensorData.getKey().equals(parameterName)) continue;
                for (SensorData.Value value : sensorData.getValues()) {
                    if (!value.date.contains("-")) continue;
                    Date actualDate = Utils.multiThreadParseStringToDate(value.date);
                    if (actualDate == null) continue;
                    if (actualDate.equals(realDate)) {
                        if (value.value == null) {
                            System.out.println("Key for station " + station.getStationName() + ", sensor: " +
                                    sensor.getId() + " and date \"" + date + "\" is null");

                            Utils.addToTreeWithDoubleAndString(parameterDataAtSpecificTime, -1.0, "STATION NAME: " + station.getStationName());

                        } else {
                            Utils.addToTreeWithDoubleAndString(parameterDataAtSpecificTime, value.value, "STATION NAME: " + station.getStationName());
                        }
                    }

                }
            }

        }

        StringBuilder result = new StringBuilder();
        result.append("Top ").append(N).
                append(" stations with highest pollution of parameter \"").
                append(parameterName).append("\" on \"").append(date).append("\"\n");

        for (Map.Entry<Double, ArrayList<String>> entry : parameterDataAtSpecificTime.entrySet()) {
            N++;
            if (parameterDataAtSpecificTime.entrySet().size() - N < 0) {
                result.append(entry.getValue()).append(" Value of parameter ").
                        append(parameterName).append(" is equal to ").append(Utils.decimalFormat.format(entry.getKey())).append("\n");
            }
        }
        return result.toString();
    }


    /**
     * Returns String that contains information about extreme values of parameter
     * (maximum and minimum values).
     * This information consists of names of the stations where such extreme values occur together with
     * date when they occur and pollution values for this specific parameter
     *
     * @param parameterName name of the parameter that we want to have extreme values' information about
     * @return Information about extreme values for given parameter
     */
    public String parameterExtremeValues(String parameterName) {
        final Container<Date> minDate = new Container<>();
        final Container<Date> maxDate = new Container<>();
        final Container<Station> minStation = new Container<>();
        final Container<Station> maxStation = new Container<>();

        ArrayList<Station> allStations = storageReceiver.getAllStations();
        LinkedList<Thread> threads = new LinkedList<>();
        final AtomicDouble maxValue = new AtomicDouble(0);
        final AtomicDouble minValue = new AtomicDouble(10000);

        if (!Utils.checkWhetherParameterNameIsValid(parameterName)) {
            System.out.println("There is no such parameter as \"" + parameterName + "\" in system");
            return null;
        }

        for (Station station : allStations) {
            Thread thread = new Thread(() -> {
                CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.getId());
                if (sensors != null) {

                    for (Sensor sensor : sensors) {
                        SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.getId());
                        if (sensorData == null) continue;
                        if (sensorData.getValues().length == 0) continue;
                        if (sensorData.getKey().equals(parameterName)) {
                            for (SensorData.Value value : sensorData.getValues()) {
                                if (value.date.contains("-") && value.value != null) {
                                    Date actualDate = Utils.multiThreadParseStringToDate(value.date);
                                    if (value.value < minValue.get() && value.value > 0) {
                                        minValue.set(value.value);
                                        synchronized (minDate) {
                                            minDate.set(actualDate);
                                        }
                                        synchronized (minStation) {
                                            minStation.set(station);
                                        }
                                    }
                                    if (value.value > maxValue.get()) {
                                        maxValue.set(value.value);
                                        synchronized (maxDate) {
                                            maxDate.set(actualDate);
                                        }
                                        synchronized (maxStation) {
                                            maxStation.set(station);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            });
            threads.add(thread);
        }

        Utils.startAndJoinThreads(threads);

        return "Minimum value of parameter \"" + parameterName + "\" occurs on " +
                Utils.convertDateToString(minDate.getValue()) +
                " for station: \"" + minStation.getValue().getStationName() + "\" and its value is: " + Utils.decimalFormat.format(minValue.get()) +
                "\nMaximum value of parameter \"" + parameterName + "\" occurs on " +
                Utils.convertDateToString(maxDate.getValue()) +
                " for station: \"" + maxStation.getValue().getStationName() + "\" and its value is: " + Utils.decimalFormat.format(maxValue.get());
    }


    /**
     * Returns list of all parameters that occur in the system at the very moment
     *
     * @return list of all parameters present in the system
     */
    public ArrayList<String> parameterNames() {
        ArrayList<String> parameters = new ArrayList<>();
        ArrayList<Station> allStations = storageReceiver.getAllStations();
        for (Station station : allStations) {
            CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.getId());
            if (sensors == null) continue;
            for (Sensor sensor : sensors) {
                SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.getId());
                if (sensorData == null) continue;
                if (!parameters.contains(sensorData.getKey())) {
                    parameters.add(sensorData.getKey());
                }
            }
        }
        return parameters;
    }
}

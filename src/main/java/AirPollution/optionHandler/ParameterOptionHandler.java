package AirPollution.optionHandler;

import AirPollution.storage.Storage;
import AirPollution.utils.Utils;
import AirPollution.model.*;
import com.google.common.util.concurrent.AtomicDouble;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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

    DecimalFormat decimalFormat = new DecimalFormat("#0.000000");

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
            int stationID = Station.returnIdOfGivenStation(allStations, station.stationName);

            CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(stationID);

            boolean foundParameter = false;

            for (Sensor sensor : sensors) {
                if (!sensor.param.paramFormula.equals(parameterName)) continue;
                foundParameter = true;
                SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.id);
                if (sensorData == null) continue;
                if (sensorData.key.equals(parameterName)) {
                    boolean validDate = false;
                    for (SensorData.Value value : sensorData.values) {
                        if (value == null) continue;
                        if (value.date.equals(date)) {
                            validDate = true;
                            if (value.value == null) {
                                valuesOfParameterForGivenStationsAndDate.
                                        put(-1.0, " NULL pollution value of parameter: " +
                                                parameterName + " for station: \"" + station.stationName + "\"\n");
                            } else {
                                valuesOfParameterForGivenStationsAndDate.
                                        put(value.value, " - pollution value of parameter: " +
                                                parameterName + " for station: \"" + station.stationName + "\"\n");
                            }

                        }

                    }
                    if (!validDate) {
                        System.out.println("There is no such date as \"" + date + "\"  in the system for station: " + station.stationName);
                    }
                }

            }
            if (!foundParameter) {
                System.out.println("There is no such parameter as \"" + parameterName + "\" in the system for station: " + station.stationName);
            }
        }
        for (Map.Entry<Double, String> entry : valuesOfParameterForGivenStationsAndDate.entrySet()) {
            stringBuilder.append(entry.getKey()).append(entry.getValue());
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
     *
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
                CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.id);
                for (Sensor sensor : sensors) {
                    SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.id);
                    if (sensorData == null) continue;
                    if (sensorData.values.length == 0) continue;
                    if (sensorData.key.equals(parameterName)) {
                        for (SensorData.Value value : sensorData.values) {
                            if (value.date.contains("-") && value.value != null) {
                                Date actualDate = Utils.multiThreadParseStringToDate(value.date);
                                if (value.value < minValue.get()) {
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

            });
            threads.add(thread);

        }

        Utils.startAndJoinThreads(threads);

        return "Minimum value of parameter \"" + parameterName + "\" occurs on " +
                Utils.convertDateToString(minDate.getValue()) +
                " for station: \"" + minStation.getValue().stationName + "\" and its value is: " + minValue.get() +
                "\nMaximum value of parameter \"" + parameterName + "\" occurs on " +
                Utils.convertDateToString(maxDate.getValue()) +
                " for station: \"" + maxStation.getValue().stationName + "\" and its value is: " + maxValue.get();
    }

    /**
     * Provides user with information about the name of the parameter
     * which has the lowest value of pollution at specific time - time that is given in an argument
     *
     * @param date we convey this date when we want to check which parameter has lowest value at this specific time,
     *             in format yyyy-MM-dd HH:mm:ss
     * @return name of the parameter that has lowest value at specific date given in an argument
     */
    public String parameterWithLowestValueAtSpecificTime(String date) {
        Date specificDate = Utils.parseStringToDate(date);
        if (specificDate == null) {
            throw new IllegalArgumentException("Given date is not valid");
        }

        ArrayList<Station> allStations = storageReceiver.getAllStations();
        ConcurrentHashMap<String, Double> fluctuations = new ConcurrentHashMap<>();

        LinkedList<Thread> threads = new LinkedList<>();
        for (Station station : allStations) {
            Thread thread = new Thread(() -> {
                CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.id);

                for (Sensor sensor : sensors) {
                    SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.id);
                    double minValue = 10000;

                    if (sensorData.values.length == 0) continue;
                    for (SensorData.Value value : sensorData.values) {
                        if (value.value != null) {
                            if (value.date.contains("-")) {
                                Date actualDate = Utils.multiThreadParseStringToDate(value.date);
                                if (actualDate != null) {
                                    if (actualDate.after(specificDate) || actualDate.equals(specificDate)) {
                                        if (value.value < minValue && value.value > 0) {
                                            minValue = value.value;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (fluctuations.get(sensorData.key) != null) {
                        if (fluctuations.get(sensorData.key) > minValue)
                            fluctuations.put(sensorData.key, minValue);
                    } else {
                        fluctuations.put(sensorData.key, minValue);
                    }
                }
            });
            threads.add(thread);
        }

        Utils.startAndJoinThreads(threads);

        return "Parameter with lowest value on \"" + date + "\" is " +
                Collections.min(fluctuations.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey()
                + " and its value is: " +
                Collections.min(fluctuations.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getValue();


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
            CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.id);
            for (Sensor sensor : sensors) {
                SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.id);
                if (sensorData == null) continue;
                if (!parameters.contains(sensorData.key)) {
                    parameters.add(sensorData.key);
                }
            }
        }
        return parameters;
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
            throw new IllegalArgumentException("This date is not valid: " + sinceWhenString);
        }


        ArrayList<Station> allStations = storageReceiver.getAllStations();
        ArrayList<Station> validStations = Utils.assignValidStations(listOfStations, allStations);
        allStations = Utils.assignAllStations(allStations, validStations);

        ConcurrentHashMap<String, StationFluctuation> fluctuations = new ConcurrentHashMap<>();

        LinkedList<Thread> threads = new LinkedList<>();

        for (Station station : allStations) {

            Thread thread = new Thread(() -> {
                CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.id);
                for (Sensor sensor : sensors) {
                    SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.id);
                    if (sensorData == null) continue;
                    if (sensorData.values.length == 0) continue;

                    double maxValue = 0;
                    double minValue = 1000;


                    for (SensorData.Value value : sensorData.values) {
                        if (value.value != null && value.date.contains("-")) {
                            Date actualDate = Utils.multiThreadParseStringToDate(value.date);
                            if (actualDate == null) continue;
                            if (actualDate.after(sinceWhenDate) || actualDate.equals(sinceWhenDate)) {
                                if (value.value < minValue) {
                                    minValue = value.value;
                                }
                                if (value.value > maxValue) {
                                    maxValue = value.value;
                                }
                            }

                        }
                    }
                    Double difference = maxValue - minValue;

                    if (fluctuations.get(sensorData.key) != null) {
                        if (fluctuations.get(sensorData.key).getDifference() < difference)
                            fluctuations.put(sensorData.key, new StationFluctuation(station, difference));
                    } else {
                        fluctuations.put(sensorData.key, new StationFluctuation(station, difference));
                    }
                }

            });
            threads.add(thread);

        }
        Utils.startAndJoinThreads(threads);

        StringBuilder resultString = new StringBuilder();
        if (validStations != null && validStations.size() > 0) {
            StringBuilder stationNames = new StringBuilder();
            for (Station station : validStations) {
                stationNames.append(station.stationName).append("\n");
            }
            resultString.
                    append("Most fluctuating parameter since \"").append(sinceWhenString).
                    append("\" for stations:\n").append(stationNames).append("is ");
        } else {
            resultString.
                    append("Most fluctuating parameter since \"").
                    append(sinceWhenString).append("\" for all stations is ");
        }
        return resultString +
                Collections.max(fluctuations.entrySet(), Comparator.comparingDouble(o -> o.getValue().getDifference())).getKey() +
                ", the difference between maximum and minimum pollution for this parameter amounts to: " +
                Collections.max(fluctuations.entrySet(),
                        Comparator.comparingDouble(o -> o.getValue().getDifference())).getValue();

    }

    /**
     *
     *
     * @param listOfStations
     * @param date
     * @param parameterName
     * @return
     */
    public String sortedSensors(ArrayList<String> listOfStations, String date, String parameterName) {
        Date realDate = Utils.parseAndCheckDate(date);

        if (!Utils.checkWhetherParameterNameIsValid(parameterName)) {
            System.out.println("Parameter name: " + parameterName + " is not valid");
            return null;
        }

        ArrayList<Station> allStations = storageReceiver.getAllStations();
        ArrayList<Station> validStations = Utils.assignValidStations(listOfStations, allStations);
        allStations = Utils.assignAllStations(allStations, validStations);

        LinkedList<Thread> threads = new LinkedList<>();
        ConcurrentSkipListMap<Double, String> parameterDataAtSpecificTime = new ConcurrentSkipListMap<>();

        for (Station station : allStations) {
            CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.id);

            Thread thread = new Thread(() -> {
                for (Sensor sensor : sensors) {
                    SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.id);
                    if (sensorData == null) continue;
                    if (!sensorData.key.equals(parameterName)) continue;
                    for (SensorData.Value value : sensorData.values) {
                        if (value.date.contains("-")) {
                            Date actualDate = Utils.multiThreadParseStringToDate(value.date);
                            if (actualDate == null) continue;
                            if (actualDate.equals(realDate)) {
                                if (value.value == null) {
                                    System.out.println(
                                            "Key for station " + station.stationName + ", sensor: " +
                                                    sensor.id + " and date " + realDate + " is null"
                                    );
                                    parameterDataAtSpecificTime.
                                            put(-1.0, "STATION NAME: " + station.stationName + "\nSensor id: " + sensor.id + "\n");
                                } else {
                                    parameterDataAtSpecificTime.
                                            put(value.value, "STATION NAME: " + station.stationName + "\nSensor id: " + sensor.id + "\n");
                                }

                            }
                        }
                    }
                }
            });
            threads.add(thread);
        }

        Utils.startAndJoinThreads(threads);

        StringBuilder result = new StringBuilder();
        for (Map.Entry<Double, String> entry : parameterDataAtSpecificTime.entrySet()) {
            result.append(entry.getValue()).append(" Value of parameter: ").
                    append(parameterName).append(" is equal to: ").append(entry.getKey()).append("\n");
        }
        return result.toString();
    }
}

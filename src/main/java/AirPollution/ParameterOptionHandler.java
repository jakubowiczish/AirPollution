package AirPollution;

import com.google.common.util.concurrent.AtomicDouble;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParameterOptionHandler {
    private Storage storageReceiver;

    public ParameterOptionHandler(Storage storageReceiver) {
        this.storageReceiver = storageReceiver;
    }

    DecimalFormat decimalFormat = new DecimalFormat("#0.000000");


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


    public double valueOfGivenParameterForGivenStationAndDate(String date, String stationName, String parameterName) {
        ArrayList<Station> allStations = storageReceiver.getAllStations();

        double currentValue = -1;

        if (stationName != null) {
            int stationID = Station.returnIdOfGivenStation(allStations, stationName);
            CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(stationID);

            boolean foundParameter = false;

            for (Sensor sensor : sensors) {
                if (sensor.param.paramFormula.equals(parameterName)) {
                    foundParameter = true;
                    SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.id);
                    if (sensorData == null) continue;
                    if (sensorData.key.equals(parameterName)) {
                        boolean validDate = false;
                        for (SensorData.Value value : sensorData.values) {
                            if (value != null) {
                                if (value.date.equals(date)) {
                                    validDate = true;
                                    currentValue = value.value;
                                }
                            }
                        }
                        if (!validDate) {
                            System.out.println("There is no such date as \"" + date + "\"  in the system");
                        }
                    }
                }
            }
            if (!foundParameter) {
                System.out.println("There is no such parameter as \" " + parameterName + "\" in system for station: " + stationName);
            }
        }
        return currentValue;
    }


    public String valueOfAllParametersForGivenStationAndDate(String date, String stationName) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String parameter : Utils.parameters) {
            double value = valueOfGivenParameterForGivenStationAndDate(date, stationName, parameter);
            if (value < 0) continue;
            stringBuilder.
                    append("Parameter name ").
                    append(parameter).
                    append(" and its value on ").
                    append(date).
                    append(" is ").append(value).
                    append("\n");
        }
        return stringBuilder.toString();
    }


    ArrayList<String> parameterNames() {
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


    public String mostFluctuatingParameter(String sinceWhenString, ArrayList<String> listOfStations) {
        Date sinceWhenDate = Utils.parseStringToDate(sinceWhenString);
        if (sinceWhenDate == null) {
            throw new IllegalArgumentException("This date is not valid: " + sinceWhenString);
        }
        ArrayList<Station> validStations = null;

        ArrayList<Station> allStations = storageReceiver.getAllStations();

        if (listOfStations != null && listOfStations.size() > 0) {
            validStations = Utils.validStations(listOfStations, allStations);
        }
        if (validStations != null && validStations.size() > 0) {
            allStations = validStations;
        }

        ConcurrentHashMap<String, StationFluctuation> fluctuations = new ConcurrentHashMap<>();

        LinkedList<Thread> threads = new LinkedList<>();

        for (Station station : allStations) {

            Thread thread = new Thread(() -> {
                CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.id);

                for (Sensor sensor : sensors) {
                    SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.id);
                    double maxValue = 0;
                    double minValue = 1000;

                    if (sensorData.values.length == 0) continue;

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


    public String sortedSensors(ArrayList<String> listOfStations, String argumentDate, String parameterName) {
        Date date = Utils.parseAndCheckDate(argumentDate);
//        if (date == null) {
//            throw new IllegalArgumentException("Given date: " + argumentDate + " is not valid");
//        }

        if (!Utils.checkWhetherParameterNameIsValid(parameterName)) {
            System.out.println("Parameter name: " + parameterName + " is not valid");
            return null;
        }

        ArrayList<Station> allStations = storageReceiver.getAllStations();
        ArrayList<Station> validStations = null;

        if (listOfStations != null && listOfStations.size() > 0) {
            validStations = Utils.validStations(listOfStations, allStations);
        }
        if (validStations != null && validStations.size() > 0) {
            allStations = validStations;
        }

        TreeMap<Double, String> parameterDataAtSpecificTime = new TreeMap<>();

        for (Station station : allStations) {
            CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.id);

            for (Sensor sensor : sensors) {
                SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.id);
                if (!sensorData.key.equals(parameterName)) continue;
                for (SensorData.Value value : sensorData.values) {
                    if (value.date.contains("-")) {
                        Date actualDate = Utils.multiThreadParseStringToDate(value.date);
                        if (actualDate == null) continue;
                        if (actualDate.equals(date)) {
                            parameterDataAtSpecificTime.put(value.value, "Station name: " + station.stationName + "\nSensor id: " + sensor.id);
                        }
                    }
                }
            }
        }

        StringBuilder result = new StringBuilder();
        for (Map.Entry<Double, String> entry : parameterDataAtSpecificTime.entrySet()) {
            result.append(entry.getValue()).append(" value of parameter: ").append(parameterName).append(" is ").append(entry.getKey()).append("\n");
        }
        return result.toString();
    }
}

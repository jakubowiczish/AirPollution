package AirPollution;

import com.google.common.util.concurrent.AtomicDouble;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class OptionsHandler {

    private static final SimpleDateFormat usedDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private <T> T[] getNoNullArray(Class<T> tClass, T[] array) {
        int validElementsCounter = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                validElementsCounter++;
            }
        }

        T[] newArray = (T[]) Array.newInstance(tClass, validElementsCounter);
        int j = 0;

        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                newArray[j++] = array[i];
            }
        }
        return newArray;
    }


    private Station[] getAllStations() {
        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();
        Station[] allStations = new Station[0];
        try {
            allStations = factory.createStations(jsonFetcher.getAllStations());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Station[] validStations = getNoNullArray(Station.class, allStations);
//        System.out.println(validStations.length + " stations found\n");
        return validStations;
    }


    private Sensor[] getAllSensorsForSpecificStation(int stationID, String stationName) {
        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();
        Sensor[] allSensors = new Sensor[0];

        try {
            allSensors = factory.createSensors(jsonFetcher.getSensors(stationID));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Sensor[] validSensors = getNoNullArray(Sensor.class, allSensors);

//        System.out.println(validSensors.length + " sensors found for station: \"" + stationName + "\"\n");
        return validSensors;
    }

    private SensorData getSensorDataForSpecificSensor(int sensorID) {
        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();
        SensorData sensorData = null;
        try {
            sensorData = factory.createSensorData(jsonFetcher.getSensorData(sensorID));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sensorData;
    }

    private AirIndex getAirIndexOfSpecificStation(int stationID) {
        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();
        AirIndex airIndex = null;
        try {
            airIndex = factory.createAirIndex(jsonFetcher.getQualityIndex(stationID));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return airIndex;
    }

    public String airIndexForStation(String stationName) {
        Station[] allStations = getAllStations();
        if (stationName != null) {
            int stationID = Station.returnIdOfGivenStation(allStations, stationName);
            AirIndex airIndex = getAirIndexOfSpecificStation(stationID);
            if (airIndex != null) {
                return airIndex.toString();
            }
        }
        return null;
    }

    public double currentParameterValue(String date, String stationName, String parameterName) {
        Station[] allStations = getAllStations();

        double currentValue = -1;

        if (stationName != null) {
            int stationID = Station.returnIdOfGivenStation(allStations, stationName);
            Sensor[] sensors = getAllSensorsForSpecificStation(stationID, stationName);

            boolean foundParameter = false;

            for (Sensor sensor : sensors) {

                if (sensor.param.paramFormula.equals(parameterName)) {
                    foundParameter = true;
                    SensorData sensorData = getSensorDataForSpecificSensor(sensor.id);
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
                            throw new IllegalArgumentException("There is no such date as " + date + " in system");
                        }
                    }
                }

            }
            if (!foundParameter) {
                throw new IllegalArgumentException("There is no such parameter as " + parameterName + " in system");
            }
        }
        return currentValue;
    }

    private Date multiThreadParseStringToDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (ParseException e) {
            System.out.println("The date: " + date + " could not be parsed");
            e.printStackTrace();
        }
        return null;
    }


    private Date parseStringToDate(String date) {
        try {
            return usedDateFormat.parse(date);
        } catch (ParseException e) {
            System.out.println("The date: " + date + " could not be parsed");
            e.printStackTrace();
        }
        return null;
    }

//    public double averagePollutionValue(String startDate, String endDate, String parameterName) {
//        Date realStartDate = parseStringToDate(startDate);
//        Date realEndDate = parseStringToDate(endDate);
//        if (realStartDate == null || realEndDate == null) {
//            throw new IllegalArgumentException("These dates are not valid");
//        }
//        return countAveragePollution(realStartDate, realEndDate, parameterName);
//    }

    public double multiThreadAveragePollutionValue(String startDate, String endDate, String parameterName) {
        Date realStartDate = parseStringToDate(startDate);
        Date realEndDate = parseStringToDate(endDate);
        if (realStartDate == null || realEndDate == null) {
            throw new IllegalArgumentException("These dates are not valid");
        }
        return multiThreadCountAveragePollution(realStartDate, realEndDate, parameterName);
    }

    public double averagePollutionValueForSpecificStation(String startDate, String endDate, String parameterName, String stationName) {
        double sumOfValues = 0;
        int valuesCounter = 0;
        double averageValue = 0;

        Date realStartDate = parseStringToDate(startDate);
        Date realEndDate = parseStringToDate(endDate);

        if (realStartDate == null || realEndDate == null) {
            throw new IllegalArgumentException("These dates are not valid");
        }

        Sensor[] sensors = null;

        Station[] allStations = getAllStations();
        for (Station station : allStations) {
            if (station.stationName.equals(stationName)) {
                sensors = getAllSensorsForSpecificStation(station.id, stationName);
            }
        }
        if (parameterName != null && sensors != null) {
            for (Sensor sensor : sensors) {
                SensorData sensorData = getSensorDataForSpecificSensor(sensor.id);
                if (sensorData.key.equals(parameterName)) {
                    for (SensorData.Value value : sensorData.values) {
                        if (value.date.contains("-")) {
                            Date actualDate = parseStringToDate(value.date);
                            // if date is between given period of time
                            if (actualDate != null) {
                                if ((actualDate.before(realEndDate) || actualDate.equals(realEndDate)) &&
                                        (actualDate.after(realStartDate) || actualDate.equals(realStartDate))) {

                                    if (value.value != null) {
                                        valuesCounter++;
                                        sumOfValues += value.value;
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        averageValue = sumOfValues / valuesCounter;
        return averageValue;
    }

//    private double countAveragePollution(Date realStartDate, Date realEndDate, String parameterName) {
//        double sumOfValues = 0;
//        int valuesCounter = 0;
//        double averageValue = 0;
//        Station[] allStations = getAllStations();
//
//        if (parameterName != null) {
//            for (Station station : allStations) {
//
//                Sensor[] sensors = getAllSensorsForSpecificStation(station.id, station.stationName);
//                for (Sensor sensor : sensors) {
//
//                    SensorData sensorData = getSensorDataForSpecificSensor(sensor.id);
//                    if (sensorData != null) {
//                        if (sensorData.key.equals(parameterName)) {
//                            for (SensorData.Value value : sensorData.values) {
//                                if (value.date.contains("-")) {
//                                    Date actualDate = parseStringToDate(value.date);
//                                    // if date is between given period of time
//                                    if (actualDate != null) {
//                                        if ((actualDate.before(realEndDate) || actualDate.equals(realEndDate)) &&
//                                                (actualDate.after(realStartDate) || actualDate.equals(realStartDate))) {
//                                            if (value.value != null) {
//                                                valuesCounter++;
//                                                sumOfValues += value.value;
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                }
//
//            }
//        }
//        averageValue = sumOfValues / valuesCounter;
//        return averageValue;
//    }

    private double multiThreadCountAveragePollution(Date realStartDate, Date realEndDate, String parameterName) {
        final AtomicInteger valuesCounter = new AtomicInteger();
        final AtomicDouble sumOfValues = new AtomicDouble();
        final AtomicInteger numberOfThreads = new AtomicInteger();

        Station[] allStations = getAllStations();

        if (parameterName != null) {
            for (Station station : allStations) {
                numberOfThreads.incrementAndGet();
                new Thread(() -> {
                    Sensor[] sensors = getAllSensorsForSpecificStation(station.id, station.stationName);
                    for (Sensor sensor : sensors) {
                        SensorData sensorData = getSensorDataForSpecificSensor(sensor.id);
                        if (sensorData != null) {
                            if (sensorData.key.equals(parameterName)) {
                                for (SensorData.Value value : sensorData.values) {
                                    if (value.date.contains("-")) {
                                        Date actualDate = multiThreadParseStringToDate(value.date);
                                        // if date is between given period of time
                                        if (actualDate != null) {
                                            if ((actualDate.before(realEndDate) || actualDate.equals(realEndDate)) &&
                                                    (actualDate.after(realStartDate) || actualDate.equals(realStartDate))) {
                                                if (value.value != null) {
                                                    valuesCounter.incrementAndGet();
                                                    sumOfValues.addAndGet(value.value);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                    numberOfThreads.decrementAndGet();
                }).start();

            }
        }

        while (numberOfThreads.get() != 0) {

        }
        return sumOfValues.get() / valuesCounter.get();
    }

    public String multiThreadMostFluctuatingParameter(String sinceWhenString) {
        final AtomicInteger numberOfThreads = new AtomicInteger();

        Date sinceWhenDate = parseStringToDate(sinceWhenString);
        if (sinceWhenDate == null) {
            throw new IllegalArgumentException("This date is not valid");
        }
        Station[] allStations = getAllStations();
        ConcurrentHashMap<String, StationFluctuation> fluctuations = new ConcurrentHashMap<>();

        for (Station station : allStations) {

            numberOfThreads.incrementAndGet();
            new Thread(() -> {
                Sensor[] sensors = getAllSensorsForSpecificStation(station.id, station.stationName);

                for (Sensor sensor : sensors) {
                    SensorData sensorData = getSensorDataForSpecificSensor(sensor.id);
                    double maxValue = 0;
                    double minValue = 1000;

                    if (sensorData.values.length == 0) continue;

                    for (SensorData.Value value : sensorData.values) {
                        if (value.value != null) {

                            if (value.date.contains("-")) {
                                Date actualDate = multiThreadParseStringToDate(value.date);
                                if (actualDate != null) {
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
                numberOfThreads.decrementAndGet();
            }).start();

        }
        while (numberOfThreads.get() != 0) {

        }
        System.out.println(Collections.max(fluctuations.entrySet(), Comparator.comparingDouble(o -> o.getValue().getDifference())).getValue());
        return "Most fluctuating parameter since " + sinceWhenString + " is "
                + Collections.max(fluctuations.entrySet(), Comparator.comparingDouble(o -> o.getValue().getDifference())).getKey();

    }
//
//    public String mostFluctuatingParameter(String sinceWhenString) {
//
//        Date sinceWhenDate = parseStringToDate(sinceWhenString);
//        if (sinceWhenDate == null) {
//            throw new IllegalArgumentException("This date is not valid");
//        }
//
//        Station[] allStations = getAllStations();
//        HashMap<String, StationFluctuation> fluctuations = new HashMap<>();
//
//        double maxValue;
//        double minValue;
//
//        for (Station station : allStations) {
//
//            Sensor[] sensors = getAllSensorsForSpecificStation(station.id, station.stationName);
//            for (Sensor sensor : sensors) {
//                SensorData sensorData = getSensorDataForSpecificSensor(sensor.id);
//                maxValue = 0;
//                minValue = 1000;
//
//                if (sensorData.values.length == 0) continue;
//
//                for (SensorData.Value value : sensorData.values) {
//                    if (value.value != null) {
//
//                        if (value.date.contains("-")) {
//                            Date actualDate = parseStringToDate(value.date);
//                            if (actualDate != null) {
//                                if (actualDate.after(sinceWhenDate) || actualDate.equals(sinceWhenDate)) {
//                                    if (value.value < minValue) {
//                                        minValue = value.value;
//                                    }
//                                    if (value.value > maxValue) {
//                                        maxValue = value.value;
//                                    }
//                                }
//                            }
//
//                        }
//                    }
//                }
//                Double difference = maxValue - minValue;
//
//                if (fluctuations.get(sensorData.key) != null) {
//                    if (fluctuations.get(sensorData.key).getDifference() < difference)
//                        fluctuations.put(sensorData.key, new StationFluctuation(station, difference));
//                } else {
//                    fluctuations.put(sensorData.key, new StationFluctuation(station, difference));
//                }
//            }
//
//        }
//
//        System.out.println(Collections.max(fluctuations.entrySet(), Comparator.comparingDouble(o -> o.getValue().getDifference())).getValue());
//        return "Parameter name: " + Collections.max(fluctuations.entrySet(), Comparator.comparingDouble(o -> o.getValue().getDifference())).getKey();
//    }

    public String multiThreadParameterWithLowestValueAtSpecificTime(String date) {
        final AtomicInteger numberOfThreads = new AtomicInteger();

        Date specificDate = parseStringToDate(date);
        if (specificDate == null) {
            throw new IllegalArgumentException("This specificDate is not valid");
        }
        Station[] allStations = getAllStations();
        ConcurrentHashMap<String, Double> fluctuations = new ConcurrentHashMap<>();

        for (Station station : allStations) {

            numberOfThreads.incrementAndGet();
            new Thread(() -> {
                Sensor[] sensors = getAllSensorsForSpecificStation(station.id, station.stationName);

                for (Sensor sensor : sensors) {
                    SensorData sensorData = getSensorDataForSpecificSensor(sensor.id);
                    double minValue = 10000;

                    if (sensorData.values.length == 0) continue;

                    for (SensorData.Value value : sensorData.values) {
                        if (value.value != null) {

                            if (value.date.contains("-")) {
                                Date actualDate = multiThreadParseStringToDate(value.date);
                                if (actualDate != null) {
                                    if (actualDate.after(specificDate) || actualDate.equals(specificDate)) {
                                        if (value.value < minValue) {
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
                numberOfThreads.decrementAndGet();
            }).start();

        }
        while (numberOfThreads.get() != 0) {

        }
        System.out.println("The lowest parameter value is " +
                Collections.min(fluctuations.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getValue());
        return "Parameter with lowest value on " + date + " is " +
                Collections.min(fluctuations.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();

    }

//    public String parameterWithLowestValueAtSpecificTime(String date) {
//        Date specificDate = parseStringToDate(date);
//        if (specificDate == null) {
//            throw new IllegalArgumentException("This date is not valid");
//        }
//
//        Station[] allStations = getAllStations();
//        for (Station station : allStations) {
//
//            Sensor[] sensors = getAllSensorsForSpecificStation(station.id, station.stationName);
//            for (Sensor sensor : sensors) {
//                SensorData sensorData = getSensorDataForSpecificSensor(sensor.id);
//
//                double maxValue = 0;
//                double minValue = 1000;
//
//                if (sensorData.values.length == 0) continue;
//                for (SensorData.Value value : sensorData.values) {
//                    if (value.value != null) {
//
//                    }
//                }
//
//            }
//
//        }
//
//
//    }
}


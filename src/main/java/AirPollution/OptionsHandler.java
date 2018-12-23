package AirPollution;

import com.google.common.util.concurrent.AtomicDouble;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;


public class OptionsHandler {

    OptionsHandler(Storage storageReceiver) {
        this.storageReceiver = storageReceiver;
    }

    private Storage storageReceiver;


    public double averagePollutionValueOfGivenParameterForSpecificStation(String startDate, String endDate, String parameterName, String stationName) {
        double sumOfValues = 0;
        int valuesCounter = 0;
        double averageValue = 0;

        Date realStartDate = Utils.parseAndCheckDate(startDate);
        Date realEndDate = Utils.parseAndCheckDate(endDate);

        ArrayList<Station> allStations = storageReceiver.getAllStations();
        CopyOnWriteArrayList<Sensor> sensors = null;

        for (Station station : allStations) {
            if (station.stationName.equals(stationName)) {
                sensors = storageReceiver.getAllSensorsForSpecificStation(station.id);
            }
        }
        if (parameterName != null && sensors != null) {
            for (Sensor sensor : sensors) {
                SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.id);
                if (sensorData.key.equals(parameterName)) {
                    for (SensorData.Value value : sensorData.values) {
                        if (value.date.contains("-")) {
                            Date actualDate = Utils.parseStringToDate(value.date);
                            // if date is between given period of time
                            if (Utils.checkDateInterval(realStartDate, realEndDate, actualDate)) {
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


        averageValue = sumOfValues / valuesCounter;
        return averageValue;
    }

    public double averagePollutionValueOfGivenParameterForAllStations(String startDate, String endDate, String parameterName) {
        Date realStartDate = Utils.parseStringToDate(startDate);
        Date realEndDate = Utils.parseStringToDate(endDate);
        if (realStartDate == null || realEndDate == null) {
            throw new IllegalArgumentException("These dates are not valid");
        }
        return countAveragePollutionValueOfGivenParameterForAllStations(realStartDate, realEndDate, parameterName);
    }

    private double countAveragePollutionValueOfGivenParameterForAllStations(Date realStartDate, Date realEndDate, String parameterName) {
        final AtomicInteger valuesCounter = new AtomicInteger();
        final AtomicDouble sumOfValues = new AtomicDouble();

        ArrayList<Station> allStations = storageReceiver.getAllStations();
        LinkedList<Thread> threads = new LinkedList<>();

        if (parameterName == null) {
            throw new IllegalArgumentException("There was no parameter given in argument, try again");
        }

        for (Station station : allStations) {
            Thread thread = new Thread(() -> {
                CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.id);
                for (Sensor sensor : sensors) {
                    SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.id);
                    if (sensorData == null) continue;
                    if (sensorData.key.equals(parameterName)) {
                        for (SensorData.Value value : sensorData.values) {
                            if (value.date.contains("-")) {
                                Date actualDate = Utils.multiThreadParseStringToDate(value.date);
                                // if date is between given period of time
                                if (actualDate != null) {
                                    if (Utils.checkDateInterval(realStartDate, realEndDate, actualDate)) {
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
            });
            threads.add(thread);
        }

        Utils.startAndJoinThreads(threads);

//        System.out.println("Average pollution of parameter: " + parameterName + " for all stations");
        return sumOfValues.get() / valuesCounter.get();
    }

    public String mostFluctuatingParameter(String sinceWhenString) {
        Date sinceWhenDate = Utils.parseStringToDate(sinceWhenString);
        if (sinceWhenDate == null) {
            throw new IllegalArgumentException("This date is not valid");
        }
        ArrayList<Station> allStations = storageReceiver.getAllStations();
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
                        if (value.value != null) {
                            if (value.date.contains("-")) {
                                Date actualDate = Utils.multiThreadParseStringToDate(value.date);
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

            });
            threads.add(thread);

        }
        Utils.startAndJoinThreads(threads);

        System.out.println(Collections.max(fluctuations.entrySet(), Comparator.comparingDouble(o -> o.getValue().getDifference())).getValue());
        System.out.print("Most fluctuating parameter since " + sinceWhenString + " is ");
        return Collections.max(fluctuations.entrySet(), Comparator.comparingDouble(o -> o.getValue().getDifference())).getKey();
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

        System.out.println("The lowest parameter value is " +
                Collections.min(fluctuations.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getValue());
        return "Parameter with lowest value on " + date + " is " +
                Collections.min(fluctuations.entrySet(), Comparator.comparingDouble(Map.Entry::getValue)).getKey();

    }

    boolean checkWhetherParameterNameExists(String parameterName) {
        ArrayList<Station> allStations = storageReceiver.getAllStations();
        for (Station station : allStations) {
            CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.id);
            for (Sensor sensor : sensors) {
                SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.id);
                if (sensorData.key.equals(parameterName)) {
                    return true;
                }

            }
        }
        return false;
    }


    public String parameterExtremeValues(String parameterName) {
        final Container<Date> minDate = new Container<>();
        final Container<Date> maxDate = new Container<>();
        final Container<Station> minStation = new Container<>();
        final Container<Station> maxStation = new Container<>();

        ArrayList<Station> allStations = storageReceiver.getAllStations();
        LinkedList<Thread> threads = new LinkedList<>();
        final AtomicDouble maxValue = new AtomicDouble(0);
        final AtomicDouble minValue = new AtomicDouble(10000);

        if (!checkWhetherParameterNameExists(parameterName)) {
            System.out.println("There is no such parameter as " + parameterName + " in system");
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
//        if (minValue.get() == 10000) {
//            System.out.println("Minimum value of given parameter was not found");
//
//        }
//        if (maxValue.get() == 0) {
//            System.out.println("Maximum value of given parameter was not found");
//        }

        Utils.startAndJoinThreads(threads);

        return "Minimum value of parameter " + parameterName + " occurs on " +
                Utils.convertDateToString(minDate.getValue()) +
                " for station: " + minStation.getValue().stationName + " and its value is: " + minValue.get() +
                "\nMaximum value of parameter " + parameterName + " occurs on " +
                Utils.convertDateToString(maxDate.getValue()) +
                " for station: " + maxStation.getValue().stationName + " and its value is: " + maxValue.get();
    }


}


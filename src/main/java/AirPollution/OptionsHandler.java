package AirPollution;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class OptionsHandler {

    private static final SimpleDateFormat usedDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String airIndexForStation(String stationName) {
        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();
        Station[] allStations = null;
        if (stationName != null) {
            try {
                allStations = factory.createStations(jsonFetcher.getAllStations());
                int stationID = Station.returnIdOfGivenStation(allStations, stationName);
                AirIndex airIndex = factory.createAirIndex(jsonFetcher.getQualityIndex(stationID));
                if (airIndex != null) {
                    return airIndex.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public double currentParameterValue(String date, String stationName, String parameterName) {
        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();

        Station[] allStations = null;

        double currentValue = -1;

        if (stationName != null) {
            try {
                allStations = factory.createStations(jsonFetcher.getAllStations());
                int stationID = Station.returnIdOfGivenStation(allStations, stationName);

                Sensor[] sensors = factory.createSensors(jsonFetcher.getSensors(stationID));
                boolean foundParameter = false;

                for (Sensor sensor : sensors) {
                    if (sensor != null) {
                        if (sensor.param.paramFormula.equals(parameterName)) {
                            foundParameter = true;
                            SensorData sensorData = factory.createSensorData(jsonFetcher.getSensorData(sensor.id));

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
                }
                if (!foundParameter) {
                    throw new IllegalArgumentException("There is no such parameter as " + parameterName + " in system");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return currentValue;
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

    public double averagePollutionValue(String startDate, String endDate, String parameterName) {
        Date realStartDate = parseStringToDate(startDate);
        Date realEndDate = parseStringToDate(endDate);
        if (realStartDate == null || realEndDate == null) {
            throw new IllegalArgumentException("These dates are not valid");
        }
        return countAveragePollution(realStartDate, realEndDate, parameterName);
    }

    private double countAveragePollution(Date realStartDate, Date realEndDate, String parameterName) {
        double sumOfValues = 0;
        int valuesCounter = 0;
        double averageValue = 0;

        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();
        Station[] allStations;

        if (parameterName != null) {
            try {
                allStations = factory.createStations(jsonFetcher.getAllStations());

                for (Station station : allStations) {
                    if (station != null) {
                        Sensor[] sensors = factory.createSensors(jsonFetcher.getSensors(station.id));
                        for (Sensor sensor : sensors) {
                            if (sensor != null) {
                                SensorData sensorData = factory.
                                        createSensorData(jsonFetcher.getSensorData(sensor.id));
                                if (sensorData != null) {
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
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        averageValue = sumOfValues / valuesCounter;
        return averageValue;
    }


    public String mostFluctuatingParameter(String sinceWhenString) {

        Date sinceWhenDate = parseStringToDate(sinceWhenString);
        if (sinceWhenDate == null) {
            throw new IllegalArgumentException("This date is not valid");
        }

        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();

        Station[] allStations;
        HashMap<String, StationFluctuation> fluctuations = new HashMap<>();
        try {
            allStations = factory.createStations(jsonFetcher.getAllStations());

            double maxValue;
            double minValue;

            for (Station station : allStations) {
                if (station != null) {
                    Sensor[] sensors = factory.createSensors(jsonFetcher.getSensors(station.id));
                    for (Sensor sensor : sensors) {
                        SensorData sensorData = factory.createSensorData(jsonFetcher.getSensorData(sensor.id));
                        maxValue = 0;
                        minValue = 1000;

                        if (sensorData.values.length == 0) continue;

                        for (SensorData.Value value : sensorData.values) {
                            if (value.value != null) {

                                if (value.date.contains("-")) {
                                    Date actualDate = parseStringToDate(value.date);
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
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(Collections.max(fluctuations.entrySet(), Comparator.comparingDouble(o -> o.getValue().getDifference())).getValue());
        return "Parameter name: " + Collections.max(fluctuations.entrySet(), Comparator.comparingDouble(o -> o.getValue().getDifference())).getKey();
    }

//    public double averagePollutionValueForSpecificStation(String startDate, String endDate, String parameterName, String stationName) {
//        double sumOfValues = 0;
//        int valuesCounter = 0;
//
//        double averageValue = 0;
//
//        Date realStartDate = parseStringToDate(startDate);
//        Date realEndDate = parseStringToDate(endDate);
//
//        if (realStartDate == null || realEndDate == null) {
//            throw new IllegalArgumentException("These dates are not valid");
//        }
//
//        Factory factory = new Factory();
//        JsonFetcher jsonFetcher = new JsonFetcher();
//
//        Sensor[] sensors = null;
//        try {
//            Station[] allStations = factory.createStations(jsonFetcher.getAllStations());
//            for (Station station : allStations) {
//                if (station.stationName.equals(stationName)) {
//                    sensors = factory.createSensors(jsonFetcher.getSensors(station.id));
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (parameterName != null && sensors != null) {
//            try {
//                for (Sensor sensor : sensors) {
//                    if (sensor != null) {
//                        SensorData sensorData = factory.createSensorData(jsonFetcher.getSensorData(sensor.id));
//                        if (sensorData.key.equals(parameterName)) {
//                            for (SensorData.Value value : sensorData.values) {
//                                try {
//                                    if (value.date.contains("-")) {
//                                        Date actualDate = usedDateFormat.parse(value.date);
//                                        // if date is between given period of time
//                                        if ((actualDate.before(realEndDate) || actualDate.equals(realEndDate)) &&
//                                                (actualDate.after(realStartDate) || actualDate.equals(realStartDate))) {
//
//                                            if (value.value != null) {
//                                                valuesCounter++;
//                                                sumOfValues += value.value;
//                                            }
//                                        }
//                                    }
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        averageValue = sumOfValues / valuesCounter;
//        return averageValue;
//    }

}


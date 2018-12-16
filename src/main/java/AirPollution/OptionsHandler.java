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
                    if (sensor.param.paramFormula.equals(parameterName)) {
                        foundParameter = true;
                        SensorData sensorData = factory.createSensorData(jsonFetcher.getSensorData(sensor.id));

                        System.out.println(sensor);
                        System.out.println(sensorData);
                        if (sensorData.key.equals(parameterName)) {
                            boolean validDate = false;
                            for (SensorData.Value value : sensorData.values) {
                                if (value.date.equals(date)) {
                                    validDate = true;
                                    currentValue = value.value;
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

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return currentValue;
    }

    public double averagePollutionValue(String startDate, String endDate, String parameterName) {
        double sumOfValues = 0;
        int valuesCounter = 0;

        double averageValue = 0;

        SimpleDateFormat usedDateFormat = OptionsHandler.usedDateFormat;
        Date realStartDate = null;
        Date realEndDate = null;
        try {
            realStartDate = usedDateFormat.parse(startDate);
            realEndDate = usedDateFormat.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (realStartDate == null || realEndDate == null) {
            throw new IllegalArgumentException("These dates are not valid");
        }

        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();
        Station[] allStations = null;
        if (parameterName != null) {
            try {
                allStations = factory.createStations(jsonFetcher.getAllStations());

                for (Station station : allStations) {
                    int id = station.id;

                    Sensor[] sensors = factory.createSensors(jsonFetcher.getSensors(id));
                    for (Sensor sensor : sensors) {
                        if (sensor != null) {
                            SensorData sensorData = factory.createSensorData(jsonFetcher.getSensorData(sensor.stationId));
                            if (sensorData.key.equals(parameterName)) {
                                valuesCounter++;
                                for (SensorData.Value value : sensorData.values) {
                                    try {
                                        if (value.date.contains("-")) {
                                            Date actualDate = usedDateFormat.parse(value.date);
                                            // if date is between given period of time
                                            if ((actualDate.before(realEndDate) || actualDate.equals(realEndDate)) && (actualDate.after(realStartDate) || actualDate.equals(realStartDate))) {
                                                sumOfValues += value.value;
                                            }
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
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


//    public double averagePollutionValueForSpecificStation(String startDate, String endDate, String parameterName, String stationName) {
//        double sumOfValues = 0;
//        int valuesCounter = 0;
//
//        double averageValue = 0;
//
//        SimpleDateFormat usedDateFormat = OptionsHandler.usedDateFormat;
//        Date realStartDate = null;
//        Date realEndDate = null;
//        try {
//            realStartDate = usedDateFormat.parse(startDate);
//            realEndDate = usedDateFormat.parse(endDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        if (realStartDate == null || realEndDate == null) {
//            throw new IllegalArgumentException("These dates are not valid");
//        }
//
//        Factory factory = new Factory();
//        JsonFetcher jsonFetcher = new JsonFetcher();
//
//        int id = -1;
//        try {
//            Station[] allStations = factory.createStations(jsonFetcher.getAllStations());
//            for (Station station : allStations) {
//                if (station.stationName.equals(stationName)) {
//                    id = station.id;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if (parameterName != null) {
//            try {
//                SensorData sensorData = factory.createSensorData(jsonFetcher.getSensorData(id));
//                if (sensorData.key.equals(parameterName)) {
//                    valuesCounter++;
//                    for (SensorData.Value value : sensorData.values) {
//                        try {
//                            if (value.date.contains("-")) {
//                                Date actualDate = usedDateFormat.parse(value.date);
//                                // if date is between given period of time
//                                if ((actualDate.before(realEndDate) || actualDate.equals(realEndDate)) && (actualDate.after(realStartDate) || actualDate.equals(realStartDate))) {
//                                    sumOfValues += value.value;
//                                }
//                            }
//                        } catch (ParseException e) {
//                            e.printStackTrace();
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


    public String mostFluctuatingParameter(String sinceWhenString) {

        SimpleDateFormat usedDateFormat = OptionsHandler.usedDateFormat;
        Date sinceWhenDate = null;

        try {
            sinceWhenDate = usedDateFormat.parse(sinceWhenString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (sinceWhenDate == null) {
            throw new IllegalArgumentException("This date is not valid");
        }

        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();

        Station[] allStations = null;
        HashMap<String, StationFluctuation> fluctuations = new HashMap<>();
        try {
            allStations = factory.createStations(jsonFetcher.getAllStations());

            double maxValue;
            double minValue;

            for (Station station : allStations) {
                if (station != null) {
                    int id = station.id;
                    try {
                        SensorData sensorData = factory.createSensorData(jsonFetcher.getSensorData(id));
                        maxValue = 0;
                        minValue = 1000;

                        if (sensorData.values.length == 0) continue;

                        for (SensorData.Value value : sensorData.values) {
                            if (value.value != null) {
                                try {
                                    if (value.date.contains("-")) {
                                        Date actualDate = usedDateFormat.parse(value.date);
                                        if (actualDate.after(sinceWhenDate) || actualDate.equals(sinceWhenDate)) {
                                            if (value.value < minValue) {
                                                minValue = value.value;
                                            }
                                            if (value.value > maxValue) {
                                                maxValue = value.value;
                                            }
                                        }

                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
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
                    } catch (IOException e) {
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(Collections.max(fluctuations.entrySet(), Comparator.comparingDouble(o -> o.getValue().getDifference())).getValue());
        return "Parameter name: " + Collections.max(fluctuations.entrySet(), Comparator.comparingDouble(o -> o.getValue().getDifference())).getKey();
    }
}


package AirPollution;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class ValueOfParameterOptionHandler {
    private Storage storageReceiver;

    public ValueOfParameterOptionHandler(Storage storageReceiver) {
        this.storageReceiver = storageReceiver;
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
                            System.out.println("There is no such date as " + date + " in the system");
                        }
                    }
                }
            }
            if (!foundParameter) {
                System.out.println("There is no such parameter as " + parameterName + " in system for station: " + stationName);
            }
        }
        return currentValue;
    }


    public String valueOfAllParametersForGivenStationAndDate(String date, String stationName) {
        ArrayList<String> parameters = new ArrayList<>() {{
            add("NO2");
            add("O3");
            add("PM10");
            add("SO2");
            add("C6H6");
            add("CO");
            add("PM2.5");
        }};

        StringBuilder stringBuilder = new StringBuilder();
        for (String parameter : parameters) {
            double value = valueOfGivenParameterForGivenStationAndDate(date, stationName, parameter);
            if (value  < 0) continue;
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
}

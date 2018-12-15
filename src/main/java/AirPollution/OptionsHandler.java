package AirPollution;

import java.io.IOException;

public class OptionsHandler {

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
                SensorData sensorData = factory.createSensorData(jsonFetcher.getSensor(stationID));

                if (sensorData.key.equals(parameterName)) {
                    boolean validDate = false;
                    for (SensorData.Value value : sensorData.values) {
                        if (value.date.equals(date)) {
                            validDate = true;
                            currentValue = value.value;
                        }
                    }
                    if (!validDate) {
                        throw new IOException("There is no such date as " + date + " in system");
                    }
                } else {
                    throw new IOException("There is no such parameter as " + parameterName + " in system");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return currentValue;
    }
}

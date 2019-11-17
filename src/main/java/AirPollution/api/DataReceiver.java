package AirPollution.api;

import AirPollution.model.AirIndex;
import AirPollution.model.Sensor;
import AirPollution.model.SensorData;
import AirPollution.model.Station;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toCollection;

/**
 * Class used to collect desirable data from the Internet
 */
public class DataReceiver {

    /**
     * Returns sensor data for given sensor id
     *
     * @param sensorID       of sensor which data we want to collect
     * @param attemptCounter parameter used for recursive callings of the method, leads to system exit if too many attempts occurred
     * @return sensor data for given sensor id
     * @see SensorData
     */
    public SensorData getSensorDataForSpecificSensor(int sensorID, int attemptCounter) {
        if (attemptCounter > 10) {
            System.out.println("UNABLE TO FETCH SENSOR DATA FOR SENSOR: " + sensorID);
            System.exit(1);
        }

        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();
        SensorData sensorData;

        try {
            sensorData = factory.createSensorData(jsonFetcher.getSensorData(sensorID));
        } catch (IOException e) {
            System.out.println("There is no SensorData for this sensor: " + sensorID);
            return getSensorDataForSpecificSensor(sensorID, attemptCounter + 1);
        }
        return sensorData;
    }

    /**
     * Returns air index for given station id
     *
     * @param stationID      of station which air index we want to collect
     * @param attemptCounter parameter used for recursive callings of the method, leads to system exit if too many attempts occurred
     * @return air index for given station id
     * @see AirIndex
     */
    public AirIndex getAirIndexOfSpecificStation(int stationID, int attemptCounter) {
        if (attemptCounter > 10) {
            System.out.println("UNABLE TO FETCH AIR INDEX FOR STATION: " + stationID);
            System.exit(1);
        }

        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();
        AirIndex airIndex;

        try {
            airIndex = factory.createAirIndex(jsonFetcher.getQualityIndex(stationID));
        } catch (IOException e) {
            System.out.println("There is no AirIndex for this station: " + stationID);
            return getAirIndexOfSpecificStation(stationID, attemptCounter + 1);
        }

        return airIndex;
    }

    /**
     * Returns list of all sensors for given station id
     *
     * @param stationID      of station which sensors we want to collect
     * @param attemptCounter parameter used for recursive callings of the method, leads to system exit if too many attempts occurred
     * @return List of all sensors for given station id
     * @see Sensor
     */
    public CopyOnWriteArrayList<Sensor> getAllSensorsForSpecificStation(int stationID, int attemptCounter) {
        if (attemptCounter > 10) {
            System.out.println("UNABLE TO FETCH ALL SENSORS FOR STATION: " + stationID);
            System.exit(1);
        }

        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();
        Sensor[] allSensors;

        try {
            allSensors = factory.createSensors(jsonFetcher.getSensors(stationID));
        } catch (IOException e) {
            System.out.println("System was unable to fetch all sensors for station with stationID: " + stationID);
            return getAllSensorsForSpecificStation(stationID, attemptCounter + 1);
        }

        CopyOnWriteArrayList<Sensor> validSensors = Arrays.stream(allSensors)
                .filter(Objects::nonNull)
                .collect(toCollection(CopyOnWriteArrayList::new));

        if (validSensors.size() == 0) {
            System.out.println("None of the sensors were found for station: " + stationID);
            return null;
        }

        return validSensors;
    }


    /**
     * Returns list of all stations available in the system
     *
     * @param attemptCounter parameter used for recursive callings of the method, leads to system exit if too many attempts occurred
     * @return List of all stations available in the system
     * @see Station
     */
    public List<Station> getAllStations(int attemptCounter) {
        if (attemptCounter > 10) {
            System.out.println("UNABLE TO FETCH ALL STATIONS");
            System.exit(1);
        }

        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();

        Station[] allStations;
        try {
            allStations = factory.createStations(jsonFetcher.getAllStations());
        } catch (IOException e) {
            System.out.println("System was unable to fetch all stations");
            return getAllStations(attemptCounter + 1);
        }

        List<Station> validStations = Arrays.stream(allStations)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (validStations.size() == 0) {
            System.out.println("None of stations were found, something probably went wrong");
            return null;
        }

        return validStations;
    }
}

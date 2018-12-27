package AirPollution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataReceiver {

    public SensorData getSensorDataForSpecificSensor(int sensorID) {
        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();
        SensorData sensorData;
        try {
            sensorData = factory.createSensorData(jsonFetcher.getSensorData(sensorID));
        } catch (IOException e) {
            System.out.println("There is no SensorData for this sensor: " + sensorID);
            return null;
        }
        return sensorData;
    }

    public AirIndex getAirIndexOfSpecificStation(int stationID) {
        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();
        AirIndex airIndex;
        try {
            airIndex = factory.createAirIndex(jsonFetcher.getQualityIndex(stationID));
        } catch (IOException e) {
            System.out.println("There is no AirIndex for this station: " + stationID);
            return null;
        }
        return airIndex;
    }

    public CopyOnWriteArrayList<Sensor> getAllSensorsForSpecificStation(int stationID) {
        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();
        Sensor[] allSensors = new Sensor[0];

        try {
            allSensors = factory.createSensors(jsonFetcher.getSensors(stationID));
        } catch (IOException e) {
            System.out.println("System was unable to fetch all sensors for station with stationID: " + stationID);
        }
        CopyOnWriteArrayList<Sensor> validSensors = new CopyOnWriteArrayList<>();
        for (Sensor sensor : allSensors) {
            if (sensor != null) {
                validSensors.add(sensor);
            }
        }
        if (validSensors.size() == 0) {
            System.out.println("None of the sensors were found for station: " + stationID);
            return null;
        }
        return validSensors;
    }


    public ArrayList<Station> getAllStations() {
        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();
        Station[] allStations = new Station[0];
        try {
            allStations = factory.createStations(jsonFetcher.getAllStations());
        } catch (IOException e) {
            System.out.println("System was unable to fetch all stations");
        }
        ArrayList<Station> validStations = new ArrayList<>();
        for(Station station : allStations) {
            if (station != null) {
                validStations.add(station);
            }
        }
        if (validStations.size() == 0) {
            System.out.println("None of stations were found, something probably went wrong");
            return null;
        }
        return validStations;
    }
}

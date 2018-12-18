package AirPollution;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Storage {

    private ConcurrentHashMap<Integer, AirIndex> airIndexMemory = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Station> stationMemory = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, CopyOnWriteArrayList<Sensor>> sensorMemory = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, SensorData> sensorDataMemory = new ConcurrentHashMap<>();
    DataReceiver dataReceiver = new DataReceiver();

    public ArrayList<Station> getAllStations() {
        if (stationMemory.size() == 0) {
            ArrayList<Station> allStations = dataReceiver.getAllStations();

            if (allStations == null) {
                System.out.println("There is no stations available at the moment");
            }

            if (allStations != null) {
                for (Station station : allStations) {
                    stationMemory.put(station.stationName, station);
                }
            }
        }

        return new ArrayList<>(stationMemory.values());
    }

    public CopyOnWriteArrayList<Sensor> getAllSensorsForSpecificStation(int stationID) {
        if (sensorMemory.containsKey(stationID)) {
            return sensorMemory.get(stationID);
        }

        CopyOnWriteArrayList<Sensor> allSensors = dataReceiver.getAllSensorsForSpecificStation(stationID);

        sensorMemory.put(stationID, allSensors);
        return getAllSensorsForSpecificStation(stationID);
    }

    public SensorData getSensorDataForSpecificSensor(int sensorID) {
        if (sensorDataMemory.containsKey(sensorID)) {
            return sensorDataMemory.get(sensorID);
        }

        SensorData sensorData = dataReceiver.getSensorDataForSpecificSensor(sensorID);
        sensorDataMemory.put(sensorID, sensorData);
        return getSensorDataForSpecificSensor(sensorID);
    }

    public AirIndex getAirIndexOfSpecificStation(int stationID) {
        if(airIndexMemory.containsKey(stationID)) {
            return airIndexMemory.get(stationID);
        }

        AirIndex airIndex = dataReceiver.getAirIndexOfSpecificStation(stationID);
        airIndexMemory.put(stationID, airIndex);
        return getAirIndexOfSpecificStation(stationID);
    }


}

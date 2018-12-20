package AirPollution;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Storage {
    public Date lastLoadDate;

    private ConcurrentHashMap<Integer, AirIndex> airIndexMemory = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Station> stationMemory = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, CopyOnWriteArrayList<Sensor>> sensorMemory = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, SensorData> sensorDataMemory = new ConcurrentHashMap<>();

    private transient DataReceiver dataReceiver = new DataReceiver();

    public void loadAllData() {
        ArrayList<Station> stations = getAllStations();

        LinkedList<Thread> threads = new LinkedList<>();

        for (Station station : stations) {
            Thread thread = new Thread(() -> {
                CopyOnWriteArrayList<Sensor> sensors = getAllSensorsForSpecificStation(station.id);

                for (Sensor sensor : sensors) {
                    getSensorDataForSpecificSensor(sensor.id);
                }

                getAirIndexOfSpecificStation(station.id);
            });

            threads.add(thread);
        }

        System.out.println("Starting " + threads.size() + " threads");
        Utils.startAndJoinThreads(threads);
        System.out.println("Loading all of the data is now finished");
        lastLoadDate = new Date();
    }

    public ArrayList<Station> getAllStations() {
        if (stationMemory.size() == 0) {
            ArrayList<Station> allStations = dataReceiver.getAllStations();

            if (allStations == null) {
                System.out.println("There are no stations available at the moment");
                return null;
            }

            for (Station station : allStations) {
                stationMemory.put(station.stationName, station);
            }

        }

        return new ArrayList<>(stationMemory.values());
    }

    public CopyOnWriteArrayList<Sensor> getAllSensorsForSpecificStation(int stationID) {
        if (sensorMemory.containsKey(stationID)) {
            return sensorMemory.get(stationID);
        }

        CopyOnWriteArrayList<Sensor> allSensors = dataReceiver.getAllSensorsForSpecificStation(stationID);
        if (allSensors == null) {
            System.out.println("Sensors for " + stationID + " are not available at the moment or do not exist");
            return null;
        }
        sensorMemory.put(stationID, allSensors);
        return getAllSensorsForSpecificStation(stationID);
    }

    public SensorData getSensorDataForSpecificSensor(int sensorID) {
        if (sensorDataMemory.containsKey(sensorID)) {
            return sensorDataMemory.get(sensorID);
        }

        SensorData sensorData = dataReceiver.getSensorDataForSpecificSensor(sensorID);

        if (sensorData == null) {
            System.out.println("SensorData for sensor: " + sensorID + " is null");
            return null;
        }

        sensorDataMemory.put(sensorID, sensorData);
        return getSensorDataForSpecificSensor(sensorID);
    }

    public AirIndex getAirIndexOfSpecificStation(int stationID) {
        if (airIndexMemory.containsKey(stationID)) {
            return airIndexMemory.get(stationID);
        }

        AirIndex airIndex = dataReceiver.getAirIndexOfSpecificStation(stationID);

        if (airIndex == null) {
            return null;
        }

        airIndexMemory.put(stationID, airIndex);
        return getAirIndexOfSpecificStation(stationID);
    }


}

package AirPollution.storage;

import AirPollution.api.DataReceiver;
import AirPollution.model.AirIndex;
import AirPollution.model.Sensor;
import AirPollution.model.SensorData;
import AirPollution.model.Station;
import AirPollution.utils.Utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Class that is responsible for storing fetched data
 */
public class Storage {
    public Date lastLoadDate;

    private Map<Integer, AirIndex> airIndexMemory = new ConcurrentHashMap<>();
    private Map<String, Station> stationMemory = new ConcurrentHashMap<>();
    private Map<Integer, CopyOnWriteArrayList<Sensor>> sensorMemory = new ConcurrentHashMap<>();
    private Map<Integer, SensorData> sensorDataMemory = new ConcurrentHashMap<>();

    private transient DataReceiver dataReceiver;

    public void setDataReceiver(DataReceiver dataReceiver) {
        this.dataReceiver = dataReceiver;
    }

    /**
     * Loads all available data
     */
    public void loadAllData() {
        List<Station> stations = getAllStations();

        LinkedList<Thread> threads = stations.stream()
                .map(station -> new Thread(() -> {
                    CopyOnWriteArrayList<Sensor> sensors
                            = getAllSensorsForSpecificStation(station.getId());

                    sensors.stream()
                            .mapToInt(Sensor::getId)
                            .forEach(this::getSensorDataForSpecificSensor);

                    getAirIndexOfSpecificStation(station.getId());
                }))
                .collect(Collectors.toCollection(LinkedList::new));

        System.out.println("Starting " + threads.size() + " threads");
        Utils.getInstance().startAndJoinThreads(threads);
        System.out.println("Loading all of the data is now finished");
        lastLoadDate = new Date();
    }

    /**
     * Returns list that contains all stations currently available in the system
     *
     * @return ArrayList with all stations currently available in the system
     */
    public List<Station> getAllStations() {
        if (stationMemory.size() == 0) {
            List<Station> allStations = dataReceiver.getAllStations(0);

            if (allStations == null) {
                System.out.println("There are no stations available at the moment");
                return null;
            }

            allStations.forEach(station -> stationMemory.put(station.getStationName(), station));
        }

        return new ArrayList<>(stationMemory.values());
    }

    /**
     * Returns list of sensors currently available for given station id
     *
     * @param stationID id of station for which sensors will be fetched
     * @return CopyOnWriteArrayList with all sensors fetched for given station id
     */
    public CopyOnWriteArrayList<Sensor> getAllSensorsForSpecificStation(int stationID) {
        if (sensorMemory.containsKey(stationID)) {
            return sensorMemory.get(stationID);
        }

        CopyOnWriteArrayList<Sensor> allSensors = dataReceiver.getAllSensorsForSpecificStation(stationID, 0);
        if (allSensors == null) {
            System.out.println("Sensors for " + stationID + " are not available at the moment or do not exist");
            return null;
        }

        sensorMemory.put(stationID, allSensors);
        return getAllSensorsForSpecificStation(stationID);
    }

    /**
     * Returns SensorData for specific sensor id
     *
     * @param sensorID id of sensor for which SensorData will be fetched
     * @return SensorData for given sensor id
     */
    public SensorData getSensorDataForSpecificSensor(int sensorID) {
        if (sensorDataMemory.containsKey(sensorID)) {
            return sensorDataMemory.get(sensorID);
        }

        SensorData sensorData = dataReceiver.getSensorDataForSpecificSensor(sensorID, 0);

        if (sensorData == null) {
            System.out.println("SensorData for sensor: " + sensorID + " is null");
            return null;
        }

        sensorDataMemory.put(sensorID, sensorData);
        return getSensorDataForSpecificSensor(sensorID);
    }

    /**
     * Returns AirIndex for specific station id
     *
     * @param stationID id of station for which you want to fetch
     * @return AirIndex for given station id
     */
    public AirIndex getAirIndexOfSpecificStation(int stationID) {
        if (airIndexMemory.containsKey(stationID)) {
            return airIndexMemory.get(stationID);
        }

        AirIndex airIndex = dataReceiver.getAirIndexOfSpecificStation(stationID, 0);

        if (airIndex == null) {
            return null;
        }

        airIndexMemory.put(stationID, airIndex);
        return getAirIndexOfSpecificStation(stationID);
    }
}

package AirPollution;

import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    private DataReceiver dataReceiver;

    private ConcurrentHashMap<String, AirIndex> airIndexMemory = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Station> stationMemory = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Station[]> sensorMemory = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, SensorData> sensorDataMemory = new ConcurrentHashMap<>();

}

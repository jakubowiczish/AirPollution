package AirPollution.storageTest;

import AirPollution.api.DataReceiver;
import AirPollution.model.AirIndex;
import AirPollution.model.Sensor;
import AirPollution.model.SensorData;
import AirPollution.model.Station;
import AirPollution.storage.Storage;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class StorageTest {
    private static final String EXAMPLE_STATION_NAME = "exampleStationName";

    //@Test
    public void getAllStationsTest() {
        DataReceiver dataReceiver = mock(DataReceiver.class);

        Storage storage = new Storage();
        storage.setDataReceiver(dataReceiver);

        Station station = new Station();
        station.setStationName(EXAMPLE_STATION_NAME);

        ArrayList<Station> stations = new ArrayList<>();
        stations.add(station);

        when(dataReceiver.getAllStations(anyInt())).thenReturn(stations);

        assertEquals(1, storage.getAllStations().size());
        assertEquals(1, storage.getAllStations().size());
        assertEquals(1, storage.getAllStations().size());
        assertEquals(1, storage.getAllStations().size());

        verify(dataReceiver, times(1)).getAllStations(anyInt());
    }

    //@Test
    public void getAllSensorsTest() {
        DataReceiver dataReceiver = mock(DataReceiver.class);
        Storage storage = new Storage();
        storage.setDataReceiver(dataReceiver);

        Sensor sensor = new Sensor();
        sensor.setId(5);

        CopyOnWriteArrayList<Sensor> sensors = new CopyOnWriteArrayList<>();
        sensors.add(sensor);

        when(dataReceiver.getAllSensorsForSpecificStation(anyInt(), anyInt())).thenReturn(sensors);

        assertEquals(1, storage.getAllSensorsForSpecificStation(5).size());
        assertEquals(1, storage.getAllSensorsForSpecificStation(5).size());

        verify(dataReceiver, times(1)).getAllSensorsForSpecificStation(anyInt(), anyInt());
    }

    //@Test
    public void getAirIndexTest() {
        DataReceiver dataReceiver = mock(DataReceiver.class);
        Storage storage = new Storage();
        storage.setDataReceiver(dataReceiver);

        AirIndex airIndex = new AirIndex();
        airIndex.setStCalcDate("2019-01-01 19:00:00");

        when(dataReceiver.getAirIndexOfSpecificStation(anyInt(), anyInt())).thenReturn(airIndex);

        assertEquals(airIndex, storage.getAirIndexOfSpecificStation(5));
        assertEquals(airIndex, storage.getAirIndexOfSpecificStation(5));
        assertEquals(airIndex, storage.getAirIndexOfSpecificStation(5));

        verify(dataReceiver, times(1)).getAirIndexOfSpecificStation(anyInt(), anyInt());
    }

    //@Test
    public void getSensorDataTest() {
        DataReceiver dataReceiver = mock(DataReceiver.class);
        Storage storage = new Storage();
        storage.setDataReceiver(dataReceiver);

        SensorData sensorData = new SensorData();
        sensorData.setKey("CO");

        when(dataReceiver.getSensorDataForSpecificSensor(anyInt(), anyInt())).thenReturn(sensorData);

        assertEquals(sensorData, storage.getSensorDataForSpecificSensor(2));
        assertEquals(sensorData, storage.getSensorDataForSpecificSensor(2));
        assertEquals(sensorData, storage.getSensorDataForSpecificSensor(2));

        verify(dataReceiver, times(1)).getSensorDataForSpecificSensor(anyInt(), anyInt());
    }
}

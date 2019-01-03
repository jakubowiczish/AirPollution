package AirPollution.optionHandlerTest;

import AirPollution.model.Sensor;
import AirPollution.model.SensorData;
import AirPollution.model.Station;
import AirPollution.optionHandler.ParameterOptionHandler;
import AirPollution.storage.Storage;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParameterOptionHandlerTest {
    private static final String EXAMPLE_STATION_NAME = "exampleStationName";

    @Test
    public void valueOfGivenParameterForGivenStationsAndDateTest() {
        Station station = new Station(1, EXAMPLE_STATION_NAME);

        ArrayList<String> listOfStations = new ArrayList<>();
        listOfStations.add(station.getStationName());

        ArrayList<Station> stations = new ArrayList<>();
        stations.add(station);

        Sensor sensor = new Sensor(10);
        CopyOnWriteArrayList<Sensor> sensors = new CopyOnWriteArrayList<>();
        sensors.add(sensor);

        SensorData sensorData = new SensorData();
        sensorData.setKey("SO2");
        sensorData.setValues(new SensorData.Value[]{
                new SensorData.Value("2019-01-01 16:00:00", 89.50),
                new SensorData.Value("2019-01-01 17:00:00", 56.70),
                new SensorData.Value("2019-01-01 18:00:00", 47.90),
                new SensorData.Value("2019-01-01 19:00:00", 32.00),
                new SensorData.Value("2019-01-02 16:00:00", 50.50),
                new SensorData.Value("2019-01-02 17:00:00", 34.20),
                new SensorData.Value("2019-01-02 18:00:00", 21.60),
                new SensorData.Value("2019-01-02 19:00:00", 19.00),
                new SensorData.Value("2019-01-03 16:00:00", 29.50),
                new SensorData.Value("2019-01-03 17:00:00", 27.50),
                new SensorData.Value("2019-01-03 18:00:00", 11.50),
                new SensorData.Value("2019-01-03 19:00:00", 9.50),
        });

        Storage storageReceiver = mock(Storage.class);
        when(storageReceiver.getAllStations()).thenReturn(stations);
        when(storageReceiver.getAllSensorsForSpecificStation(station.getId())).thenReturn(sensors);
        when(storageReceiver.getSensorDataForSpecificSensor(sensor.getId())).thenReturn(sensorData);

        ParameterOptionHandler parameterOptionHandler = new ParameterOptionHandler(storageReceiver);

        String parameterName = "SO2";
        String date = "2019-01-01 17:00:00";
        String actualResult = parameterOptionHandler.
                valueOfGivenParameterForGivenStationsAndDate(date, listOfStations, parameterName);

        String expectedResult = "For Parameter: SO2\n" +
                "Date: 2019-01-01 17:00:00\n" +
                "56.7000 - pollution value of parameter: SO2 for station: \"exampleStationName\"\n";

        assertEquals(expectedResult, actualResult);

        String date_2 = "2019-01-01 16:00:00";
        String actualResult_2 = parameterOptionHandler.
                valueOfGivenParameterForGivenStationsAndDate(date_2, listOfStations, parameterName);

        assertNotEquals(expectedResult, actualResult_2);

        String parameterName_2 = "OX";
        String actualResult_3 = parameterOptionHandler.
                valueOfGivenParameterForGivenStationsAndDate(date, listOfStations, parameterName_2);

        assertNull(actualResult_3);
    }

}

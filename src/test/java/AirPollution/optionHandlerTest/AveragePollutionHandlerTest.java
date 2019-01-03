package AirPollution.optionHandlerTest;

import AirPollution.model.Sensor;
import AirPollution.model.SensorData;
import AirPollution.model.Station;
import AirPollution.optionHandler.AveragePollutionHandler;
import AirPollution.storage.Storage;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AveragePollutionHandlerTest {
    private static final String EXAMPLE_STATION_NAME = "exampleStationName";

    @Test
    public void averagePollutionValueOfGivenParameterForGivenStationsTest() {

        Station station = new Station(1, EXAMPLE_STATION_NAME);

        Sensor sensor = new Sensor(10);

        SensorData sensorData = new SensorData();
        sensorData.setKey("PM10");
        sensorData.setValues(new SensorData.Value[]{
                new SensorData.Value("2019-01-01 21:20:26", 12.50),
                new SensorData.Value("2019-01-02 21:20:26", 13.50),
        });

        String beginDate = "2019-01-01 21:00:00";
        String beginDate_2 = "2019-01-01 22:00:00";
        String endDate = "2019-01-02 22:00:00";
        String parameterName = "PM10";

        ArrayList<String> listOfStations = new ArrayList<>();
        listOfStations.add(station.getStationName());

        ArrayList<Station> stations = new ArrayList<>();
        stations.add(station);

        CopyOnWriteArrayList<Sensor> sensors = new CopyOnWriteArrayList<>();
        sensors.add(sensor);

        Storage storageReceiver = mock(Storage.class);

        when(storageReceiver.getAllStations()).thenReturn(stations);
        when(storageReceiver.getAllSensorsForSpecificStation(station.getId())).thenReturn(sensors);
        when(storageReceiver.getSensorDataForSpecificSensor(sensor.getId())).thenReturn(sensorData);

        AveragePollutionHandler averagePollutionHandler = new AveragePollutionHandler(storageReceiver);

        String actualResult = averagePollutionHandler.
                averagePollutionValueOfGivenParameterForGivenStations(beginDate, endDate, parameterName, listOfStations);

        String expectedResult = "Average pollution value of parameter: PM10\n" +
                "from: 2019-01-01 21:00:00 to: 2019-01-02 22:00:00\n" +
                "for GIVEN stations:\n" +
                "exampleStationName\n" +
                "is equal to: 13.0000";

        assertEquals(expectedResult, actualResult);

        String actualResult_2 = averagePollutionHandler.
                averagePollutionValueOfGivenParameterForGivenStations(beginDate_2, endDate, parameterName, listOfStations);

        assertNotEquals(expectedResult, actualResult_2);
    }
}

package AirPollution.optionHandlerTest;

import AirPollution.model.Sensor;
import AirPollution.model.SensorData;
import AirPollution.model.Station;
import AirPollution.optionHandler.BarGraphHandler;
import AirPollution.storage.Storage;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BarGraphHandlerTest {
    private static final String EXAMPLE_STATION_NAME = "exampleStationName";

    @Test
    public void barGraphForGivenParameterStationsAndPeriodOfTimeTest() {
        Station station = new Station(1, EXAMPLE_STATION_NAME);

        ArrayList<String> listOfStations = new ArrayList<>();
        listOfStations.add(station.getStationName());
        ArrayList<Station> stations = new ArrayList<>();
        stations.add(station);

        Sensor sensor = new Sensor(10);
        CopyOnWriteArrayList<Sensor> sensors = new CopyOnWriteArrayList<>();
        sensors.add(sensor);

        SensorData sensorData = new SensorData();
        sensorData.setKey("O3");
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

        String beginHour = "15:00:00";
        String beginHour_2 = "17:00:00";
        String endHour = "20:00:00";
        String parameterName = "O3";

        LocalDate localDate = LocalDate.of(2019, 1, 3);

        Storage storageReceiver = mock(Storage.class);

        when(storageReceiver.getAllStations()).thenReturn(stations);
        when(storageReceiver.getAllSensorsForSpecificStation(station.getId())).thenReturn(sensors);
        when(storageReceiver.getSensorDataForSpecificSensor(sensor.getId())).thenReturn(sensorData);

        BarGraphHandler barGraphHandler = new BarGraphHandler(storageReceiver);

        String actualResult = barGraphHandler.
                barGraphForGivenParameterStationsAndPeriodOfTime(beginHour, endHour, parameterName, listOfStations, localDate);

        String expectedResult =
                "16:00:00 DAY BEFORE YESTERDAY (exampleStationName) ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 89.5000\n" +
                        " 16:00:00 YESTERDAY            (exampleStationName) ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 50.5000\n" +
                        " 16:00:00 TODAY                (exampleStationName) ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 29.5000\n" +
                        "17:00:00 DAY BEFORE YESTERDAY (exampleStationName) ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 56.7000\n" +
                        " 17:00:00 YESTERDAY            (exampleStationName) ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 34.2000\n" +
                        " 17:00:00 TODAY                (exampleStationName) ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 27.5000\n" +
                        "18:00:00 DAY BEFORE YESTERDAY (exampleStationName) ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 47.9000\n" +
                        " 18:00:00 YESTERDAY            (exampleStationName) ■■■■■■■■■■■■■■■■■■■■■■■■■■ 21.6000\n" +
                        " 18:00:00 TODAY                (exampleStationName) ■■■■■■■■■■■■■■ 11.5000\n" +
                        "19:00:00 DAY BEFORE YESTERDAY (exampleStationName) ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 32.0000\n" +
                        " 19:00:00 YESTERDAY            (exampleStationName) ■■■■■■■■■■■■■■■■■■■■■■■ 19.0000\n" +
                        " 19:00:00 TODAY                (exampleStationName) ■■■■■■■■■■■ 9.5000\n";

        assertEquals(expectedResult, actualResult);

        String actualResult_2 = barGraphHandler.
                barGraphForGivenParameterStationsAndPeriodOfTime(beginHour_2, endHour, parameterName, listOfStations, localDate);

        assertNotEquals(expectedResult, actualResult_2);
    }
}

package AirPollution.optionHandlerTest;

import AirPollution.model.AirIndex;
import AirPollution.model.Sensor;
import AirPollution.model.SensorData;
import AirPollution.model.Station;
import AirPollution.optionHandler.AirIndexOptionHandler;
import AirPollution.optionHandler.AveragePollutionHandler;
import AirPollution.storage.Storage;
import org.junit.Test;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StorageTest {
    private static final String EXAMPLE_STATION_NAME = "exampleStationName";
    private static final String EXAMPLE_STATION_NAME_2 = "exampleStationName_2";

    @Test
    public void allIndicesOfGivenStationsTest() {

        Station station = new Station(5, EXAMPLE_STATION_NAME);

        AirIndex airIndex = new AirIndex();
        airIndex.setId(5);
        airIndex.setStCalcDate("2019-01-01 21:20:26");
        airIndex.setStSourceDataDate("2019-01-01 20:00:00");

        Storage storageReceiver = mock(Storage.class);

        when(storageReceiver.getAirIndexOfSpecificStation(station.getId())).thenReturn(airIndex);

        AirIndex resultIndex = storageReceiver.getAirIndexOfSpecificStation(station.getId());
        String actualResult = resultIndex.toString();
        String expectedResult =
                "{\n" +
                        "   id:                 5\n" +
                        "   stIndexLevel:       null\n" +
                        "   stCalcDate:         2019-01-01 21:20:26\n" +
                        "   stSourceDataDate:   2019-01-01 20:00:00\n" +
                        "   so2IndexLevel:      null\n" +
                        "   so2CalcDate:        null\n" +
                        "   so2SourceDataDate:  null\n" +
                        "   no2IndexLevel:      null\n" +
                        "   no2CalcDate:        null\n" +
                        "   no2SourceDataDate:  null\n" +
                        "   coIndexLevel:       null\n" +
                        "   coCalcDate:         null\n" +
                        "   coSourceDataDate:   null\n" +
                        "   pm10IndexLevel:     null\n" +
                        "   pm10CalcDate:       null\n" +
                        "   pm10SourceDataDate: null\n" +
                        "   pm25IndexLevel:     null\n" +
                        "   pm25CalcDate:       null\n" +
                        "   pm25SourceDataDate: null\n" +
                        "   o3IndexLevel:       null\n" +
                        "   o3CalcDate:         null\n" +
                        "   o3SourceDataDate:   null\n" +
                        "   c6h6IndexLevel:     null\n" +
                        "   c6h6CalcDate:       null\n" +
                        "   c6h6SourceDataDate: null\n" +
                        "}";


        assertEquals(expectedResult, actualResult);
    }

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

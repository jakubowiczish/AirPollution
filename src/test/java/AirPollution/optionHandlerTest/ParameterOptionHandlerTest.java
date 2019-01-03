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
    private static final String EXAMPLE_STATION_NAME_2 = "exampleStationName_2";

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

        String expectedResult = "For Date: 2019-01-01 17:00:00\n" +
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

        String date_3 = "2019-01-04 16:00:00";
        String actualResult_4 = parameterOptionHandler.
                valueOfGivenParameterForGivenStationsAndDate(date_3, listOfStations, parameterName);

        assertNull(actualResult_4);
    }

    @Test
    public void mostFluctuatingParameterTest() {
        Station station = new Station(1, EXAMPLE_STATION_NAME);

        ArrayList<String> listOfStations = new ArrayList<>();
        listOfStations.add(station.getStationName());

        ArrayList<Station> stations = new ArrayList<>();
        stations.add(station);

        Sensor sensor = new Sensor(10);
        CopyOnWriteArrayList<Sensor> sensors = new CopyOnWriteArrayList<>();
        sensors.add(sensor);

        SensorData sensorData = new SensorData();
        sensorData.setKey("PM10");
        sensorData.setValues(new SensorData.Value[]{
                new SensorData.Value("2019-01-01 16:00:00", 189.50),
                new SensorData.Value("2019-01-02 17:00:00", 34.20),
                new SensorData.Value("2019-01-03 19:00:00", 9.50),
        });

        Sensor sensor_2 = new Sensor(11);
        sensors.add(sensor_2);

        SensorData sensorData_2 = new SensorData();
        sensorData_2.setKey("CO");
        sensorData_2.setValues(new SensorData.Value[]{
                new SensorData.Value("2019-01-01 16:00:00", 6.42),
                new SensorData.Value("2019-01-02 17:00:00", 97.65),
                new SensorData.Value("2019-01-03 19:00:00", 18.64),
        });

        Storage storageReceiver = mock(Storage.class);
        when(storageReceiver.getAllStations()).thenReturn(stations);
        when(storageReceiver.getAllSensorsForSpecificStation(station.getId())).thenReturn(sensors);
        when(storageReceiver.getSensorDataForSpecificSensor(sensor.getId())).thenReturn(sensorData);
        when(storageReceiver.getSensorDataForSpecificSensor(sensor_2.getId())).thenReturn(sensorData_2);

        ParameterOptionHandler parameterOptionHandler = new ParameterOptionHandler(storageReceiver);

        String sinceWhen = "2019-01-01 15:00:00";
        String actualResult = parameterOptionHandler.mostFluctuatingParameter(sinceWhen, listOfStations);

        String expectedResult = "Most fluctuating parameter since \"2019-01-01 15:00:00\" for stations:\n" +
                "exampleStationName\n" +
                "is CO,\n" +
                "the difference between maximum and minimum pollution for this parameter amounts to: 183.0800,\n" +
                "with maximum value of: 189.5000 and minimum value of: 6.4200";

        assertNotNull(expectedResult);
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);


        String sinceWhen_2 = "2019-01-01 17:00:00";
        String actualResult_2 = parameterOptionHandler.mostFluctuatingParameter(sinceWhen_2, listOfStations);

        String expectedResult_2 = "Most fluctuating parameter since \"2019-01-01 17:00:00\" for stations:\n" +
                "exampleStationName\n" +
                "is CO,\n" +
                "the difference between maximum and minimum pollution for this parameter amounts to: 88.1500,\n" +
                "with maximum value of: 97.6500 and minimum value of: 9.5000";

        assertNotNull(expectedResult_2);
        assertNotNull(actualResult_2);
        assertEquals(expectedResult_2, actualResult_2);

        String sinceWhen_3 = "2019-01-04 20:00:00";
        String actualResult_3 = parameterOptionHandler.mostFluctuatingParameter(sinceWhen_3, listOfStations);
        assertNull(actualResult_3);
    }

    @Test
    public void parametersWithLowestAndHighestValuesAtSpecificTime() {

        Station station = new Station(1, EXAMPLE_STATION_NAME);
        Station station_2 = new Station(2, EXAMPLE_STATION_NAME_2);

        ArrayList<Station> stations = new ArrayList<>();
        stations.add(station);
        stations.add(station_2);

        Sensor sensor = new Sensor(11);
        CopyOnWriteArrayList<Sensor> sensors = new CopyOnWriteArrayList<>();
        sensors.add(sensor);

        SensorData sensorData = new SensorData();
        sensorData.setKey("PM10");
        sensorData.setValues(new SensorData.Value[]{
                new SensorData.Value("2019-01-01 16:00:00", 189.50),
                new SensorData.Value("2019-01-01 16:00:00", 34.20),
                new SensorData.Value("2019-01-01 16:00:00", 9.50),
                new SensorData.Value("2019-01-01 18:00:00", 1.30),
                new SensorData.Value("2019-01-01 18:00:00", 95.30),
        });

        Sensor sensor_2 = new Sensor(12);
        CopyOnWriteArrayList<Sensor> sensors_2 = new CopyOnWriteArrayList<>();
        sensors_2.add(sensor_2);

        SensorData sensorData_2 = new SensorData();
        sensorData_2.setKey("CO");
        sensorData_2.setValues(new SensorData.Value[]{
                new SensorData.Value("2019-01-01 16:00:00", 6.42),
                new SensorData.Value("2019-01-01 16:00:00", 97.65),
                new SensorData.Value("2019-01-01 16:00:00", 18.64),
                new SensorData.Value("2019-01-01 16:00:00", 75.59),
                new SensorData.Value("2019-01-01 18:00:00", 9.74),
                new SensorData.Value("2019-01-01 18:00:00", 101.34),
        });

        Storage storageReceiver = mock(Storage.class);
        when(storageReceiver.getAllStations()).thenReturn(stations);

        when(storageReceiver.getAllSensorsForSpecificStation(station.getId())).thenReturn(sensors);
        when(storageReceiver.getSensorDataForSpecificSensor(sensor.getId())).thenReturn(sensorData);

        when(storageReceiver.getAllSensorsForSpecificStation(station_2.getId())).thenReturn(sensors_2);
        when(storageReceiver.getSensorDataForSpecificSensor(sensor_2.getId())).thenReturn(sensorData_2);

        ParameterOptionHandler parameterOptionHandler = new ParameterOptionHandler(storageReceiver);
        String date = "2019-01-01 16:00:00";

        String actualResult = parameterOptionHandler.
                parametersWithLowestAndHighestValuesAtSpecificTime(date);

        String expectedResult =
                "Parameter with lowest value on \"2019-01-01 16:00:00\" " +
                "is CO and its value is: 6.4200, " +
                "it occurs for station: exampleStationName_2\n" +
                "Parameter with highest value on \"2019-01-01 16:00:00\" " +
                "is PM10 and its value is: 189.5000, " +
                "it occurs for station: exampleStationName";

        assertEquals(expectedResult, actualResult);


        String date_2 = "2019-01-01 18:00:00";

        String actualResult_2 = parameterOptionHandler.
                parametersWithLowestAndHighestValuesAtSpecificTime(date_2);

        String expectedResult_2 =
                "Parameter with lowest value on \"2019-01-01 18:00:00\" " +
                        "is PM10 and its value is: 1.3000, " +
                        "it occurs for station: exampleStationName\n" +
                        "Parameter with highest value on \"2019-01-01 18:00:00\" " +
                        "is CO and its value is: 101.3400, " +
                        "it occurs for station: exampleStationName_2";

        assertEquals(expectedResult_2, actualResult_2);

        String date_3 = "2019-01-01 14:00:00";

        String actualResult_3 = parameterOptionHandler.parametersWithLowestAndHighestValuesAtSpecificTime(date_3);
        assertNull(actualResult_3);
    }



}

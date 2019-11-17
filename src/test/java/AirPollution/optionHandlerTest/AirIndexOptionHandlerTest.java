package AirPollution.optionHandlerTest;

import AirPollution.model.AirIndex;
import AirPollution.model.Station;
import AirPollution.optionHandler.AirIndexOptionHandler;
import AirPollution.storage.Storage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AirIndexOptionHandlerTest {
    private static final String EXAMPLE_STATION_NAME = "exampleStationName";

    //@Test
    public void allIndicesOfGivenStationsTest() {

        Station station = new Station(5, EXAMPLE_STATION_NAME);
        List<String> listOfStations = new ArrayList<>();
        listOfStations.add(station.getStationName());

        ArrayList<Station> stations = new ArrayList<>();
        stations.add(station);

        Storage storageReceiver = mock(Storage.class);
        when(storageReceiver.getAllStations()).thenReturn(stations);

        AirIndex airIndex = new AirIndex();
        airIndex.setId(5);
        airIndex.setStCalcDate("2019-01-01 21:20:26");
        airIndex.setStSourceDataDate("2019-01-01 20:00:00");
        when(storageReceiver.getAirIndexOfSpecificStation(station.getId())).thenReturn(airIndex);

        AirIndexOptionHandler airIndexOptionHandler = new AirIndexOptionHandler(storageReceiver);

        String actualResult = airIndexOptionHandler.airIndicesOfGivenStations(listOfStations);
        String expectedResult =
                "AIR INDEX FOR STATION: \"exampleStationName\"\n" +
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
                        "}\n";

        assertEquals(expectedResult, actualResult);
    }

}

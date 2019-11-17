package AirPollution.utilsTest;

import AirPollution.model.Station;
import AirPollution.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UtilsTest {
    private static final String EXAMPLE_STATION_NAME = "exampleStationName";
    private static final String EXAMPLE_STATION_NAME_2 = "exampleStationName_2";

    //@Test
    public void checkWhetherStationExistsTest() {
        // given
        Station station = mock(Station.class);
        when(station.getStationName()).thenReturn(EXAMPLE_STATION_NAME);
        List<Station> stations = singletonList(station);

        // when
        boolean result = Utils.getInstance().checkWhetherStationExists(stations, EXAMPLE_STATION_NAME);

        // then
        assertTrue(result);
    }

    //@Test
    public void checkValidStationsTest() {
        Station station1 = mock(Station.class);
        Station station2 = mock(Station.class);

        when(station1.getStationName()).thenReturn(EXAMPLE_STATION_NAME);
        when(station2.getStationName()).thenReturn(EXAMPLE_STATION_NAME_2);

        List<String> listOfStations = singletonList(EXAMPLE_STATION_NAME);
        List<Station> allStations = new ArrayList<>(Arrays.asList(station1, station2));

        List<Station> expectedStations = Utils.getInstance().checkValidStations(listOfStations, allStations);
        List<Station> actualStations = singletonList(station1);

        assertEquals(expectedStations, actualStations);
    }


}

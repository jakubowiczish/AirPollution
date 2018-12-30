package AirPollution.utilsTest;

import AirPollution.model.Station;
import AirPollution.utils.Utils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UtilsTest {
    private static final String EXAMPLE_STATION_NAME = "exampleStationName";
    private static final String EXAMPLE_STATION_NAME_2 = "exampleStationName_2";

    @Test
    public void checkWhetherStationExistsTest() {
        // given
        Station station = mock(Station.class);
        when(station.getStationName()).thenReturn(EXAMPLE_STATION_NAME);
        ArrayList<Station> stations = new ArrayList<>(List.of(station));

        // when
        boolean result = Utils.checkWhetherStationExists(stations, EXAMPLE_STATION_NAME);

        // then
        assertTrue(result);
    }

    @Test
    public void checkValidStationsTest() {
        Station station1 = mock(Station.class);
        Station station2 = mock(Station.class);

        when(station1.getStationName()).thenReturn(EXAMPLE_STATION_NAME);
        when(station2.getStationName()).thenReturn(EXAMPLE_STATION_NAME_2);

        ArrayList<String> listOfStations = new ArrayList<>(List.of(EXAMPLE_STATION_NAME));
        ArrayList<Station> allStations = new ArrayList<>(List.of(station1, station2));

        ArrayList<Station> expectedStations = Utils.checkValidStations(listOfStations, allStations);
        ArrayList<Station> actualStations = new ArrayList<>(List.of(station1));

        assertEquals(expectedStations, actualStations);
    }



}

package AirPollution.optionHandler;

import AirPollution.model.AirIndex;
import AirPollution.model.Station;
import AirPollution.storage.Storage;
import AirPollution.utils.Utils;

import java.util.List;

/**
 * Class that is used to provide information about air index
 */
public class AirIndexOptionHandler {
    private Storage storageReceiver;

    public AirIndexOptionHandler(Storage storageReceiver) {
        this.storageReceiver = storageReceiver;
    }

    /**
     * Returns String containing content of all air indices for given list of stations
     *
     * @param listOfStations list of names of stations which air indices we want to be shown
     * @return String representation of air indices for given stations
     */
    public String airIndicesOfGivenStations(List<String> listOfStations) {
        List<Station> allStations = storageReceiver.getAllStations();

        List<Station> validStations = Utils.getInstance().assignValidStations(listOfStations, allStations);
        allStations = Utils.getInstance().assignAllStations(allStations, validStations);
        StringBuilder stringBuilder = new StringBuilder();

        if (allStations == null) return null;
        for (Station station : allStations) {
            if (station == null) continue;
            int stationID = Station.returnIdOfGivenStation(allStations, station.getStationName());
            AirIndex airIndex = storageReceiver.getAirIndexOfSpecificStation(stationID);
            if (airIndex == null) continue;
            stringBuilder.append("AIR INDEX FOR STATION: \"").
                    append(station.getStationName()).append("\"\n").append(airIndex.toString()).append("\n");
        }

        return stringBuilder.toString();
    }

}

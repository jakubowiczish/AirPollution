package AirPollution.optionHandler;

import AirPollution.storage.Storage;
import AirPollution.utils.Utils;
import AirPollution.model.AirIndex;
import AirPollution.model.Station;

import java.util.ArrayList;

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
    public String airIndicesOfGivenStations(ArrayList<String> listOfStations) {
        ArrayList<Station> allStations = storageReceiver.getAllStations();

        ArrayList<Station> validStations = Utils.assignValidStations(listOfStations, allStations);
        allStations = Utils.assignAllStations(allStations, validStations);
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

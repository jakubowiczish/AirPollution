package AirPollution.optionHandler;

import AirPollution.storage.Storage;
import AirPollution.utils.Utils;
import AirPollution.model.AirIndex;
import AirPollution.model.Station;

import java.util.ArrayList;

public class AirIndexOptionHandler {
    private Storage storageReceiver;

    public AirIndexOptionHandler(Storage storageReceiver) {
        this.storageReceiver = storageReceiver;
    }

    public String airIndicesOfGivenStations(ArrayList<String> listOfStations) {
        ArrayList<Station> allStations = storageReceiver.getAllStations();

        ArrayList<Station> validStations = Utils.assignValidStations(listOfStations, allStations);
        allStations = Utils.assignAllStations(allStations, validStations);
        StringBuilder stringBuilder = new StringBuilder();

        if (allStations == null) return null;
        for (Station station : allStations) {
            if (station == null) continue;
            int stationID = Station.returnIdOfGivenStation(allStations, station.stationName);
            AirIndex airIndex = storageReceiver.getAirIndexOfSpecificStation(stationID);
            if (airIndex == null) continue;
            stringBuilder.append("AIR INDEX FOR STATION: \"").append(station.stationName).append("\"\n").append(airIndex.toString()).append("\n");
        }
        return stringBuilder.toString();
    }

}

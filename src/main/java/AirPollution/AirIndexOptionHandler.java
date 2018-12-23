package AirPollution;

import java.util.ArrayList;

public class AirIndexOptionHandler {
    private Storage storageReceiver;

    public AirIndexOptionHandler(Storage storageReceiver) {
        this.storageReceiver = storageReceiver;
    }

    public String printerOfAirIndexForGivenStation(String stationName) {
        ArrayList<Station> allStations = storageReceiver.getAllStations();

        StringBuilder stringBuilder = new StringBuilder();
        boolean foundStation = Utils.checkWhetherStationExists(allStations, stationName);

        if (!foundStation) {
            System.out.println("There is no such station as " + stationName + " in the system");
        }
        if (foundStation) {
            int stationID = Station.returnIdOfGivenStation(allStations, stationName);
            AirIndex airIndex = storageReceiver.getAirIndexOfSpecificStation(stationID);
            stringBuilder.append("Air Index for station: ").append(stationName).append("\n").append(airIndex.toString());

        }
        return stringBuilder.toString();
    }
}

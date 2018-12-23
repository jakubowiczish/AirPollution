package AirPollution;

import java.util.ArrayList;

public class PrintApiInformationOptionHandler {
    private Storage storageReceiver;

    public PrintApiInformationOptionHandler(Storage storageReceiver) {
        this.storageReceiver = storageReceiver;
    }

    public void printNamesOfAllStations() {
        storageReceiver.getAllStations()
                .forEach(station -> System.out.println(station.stationName));
    }

    public void printAllStationsWithTheirSensors() {
        storageReceiver.getAllStations()
                .forEach(station -> {
                    System.out.println("\nSTATION NAME: " + station.stationName + "\nSensors for this station:");
                    storageReceiver.getAllSensorsForSpecificStation(station.id)
                            .forEach(sensor -> System.out.println(sensor.toString()));
                });

    }

    public void printNamesOfAllStationsContainingGivenString(String stationAddressFragment) {
        boolean found = false;
        ArrayList<Station> allStations = storageReceiver.getAllStations();
        for (Station station : allStations) {
            if (station.stationName.contains(stationAddressFragment)) {
                System.out.println(station.stationName);
                found = true;
            }
        }
        if (!found) {
            System.out.println("There is no station that contains \"" + stationAddressFragment + "\" in her name");
        }
    }

}

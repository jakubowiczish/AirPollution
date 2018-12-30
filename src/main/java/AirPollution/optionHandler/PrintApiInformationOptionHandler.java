package AirPollution.optionHandler;

import AirPollution.storage.Storage;
import AirPollution.model.Station;

import java.util.ArrayList;

public class PrintApiInformationOptionHandler {
    private Storage storageReceiver;

    public PrintApiInformationOptionHandler(Storage storageReceiver) {
        this.storageReceiver = storageReceiver;
    }

    public void printNamesOfAllStations() {
        storageReceiver.getAllStations()
                .forEach(station -> System.out.println(station.getStationName()));
    }

    public void printAllStationsWithTheirSensors() {
        storageReceiver.getAllStations()
                .forEach(station -> {
                    System.out.println("\nSTATION NAME: " + station.getStationName() + "\nSensors for this station:");
                    storageReceiver.getAllSensorsForSpecificStation(station.getId())
                            .forEach(sensor -> System.out.println(sensor.toString()));
                });

    }

    public void printNamesOfAllStationsContainingGivenString(String stationAddressFragment) {
        boolean found = false;
        ArrayList<Station> allStations = storageReceiver.getAllStations();
        for (Station station : allStations) {
            if (station.getStationName().contains(stationAddressFragment)) {
                System.out.println(station.getStationName());
                found = true;
            }
        }
        if (!found) {
            System.out.println("There is no station that contains \"" + stationAddressFragment + "\" in her name");
        }
    }

}

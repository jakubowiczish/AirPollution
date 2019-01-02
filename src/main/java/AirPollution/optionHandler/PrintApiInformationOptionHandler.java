package AirPollution.optionHandler;

import AirPollution.model.Sensor;
import AirPollution.storage.Storage;
import AirPollution.model.Station;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Class that is used to print some information provided by api
 */
public class PrintApiInformationOptionHandler {
    private Storage storageReceiver;

    public PrintApiInformationOptionHandler(Storage storageReceiver) {
        this.storageReceiver = storageReceiver;
    }

    /**
     * Prints names of all stations available in the system
     */
    public void printNamesOfAllStations() {
        storageReceiver.getAllStations()
                .forEach(station -> System.out.println(station.getStationName()));
    }

    /**
     * Prints names of all stations and all sensors for every station
     */
    public void printAllStationsWithTheirSensors() {
        storageReceiver.getAllStations()
                .forEach(new Consumer<Station>() {
                    @Override
                    public void accept(Station station) {
                        if (station != null) {
                            System.out.println("\nSTATION NAME: " + station.getStationName() + "\nSensors for this station:");
                            CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.getId());
                            if (sensors != null) {
                                sensors.forEach(new Consumer<Sensor>() {
                                    @Override
                                    public void accept(Sensor sensor) {
                                        if (sensor != null) {
                                            System.out.println(sensor.toString());
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

    }

    /**
     * Prints names of all stations that contain given fragment in their names
     *
     * @param stationAddressFragment fragment of station name that you want to find in the system
     */
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

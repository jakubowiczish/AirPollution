package AirPollution;

import java.io.IOException;
import java.util.Arrays;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command
        (
                name = "Air Pollution in Poland",
                version = "Air Pollution in Poland,\nv1.0 by Jakub Płotnikowski 2018,\nall rights reserved",
                description = "Gives various information about air quality in Poland",
                mixinStandardHelpOptions = true
        )
public class App implements Runnable {

    @Option(names = {"--station"}, description = "Station name for Air Index")
    private String stationName;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Try adding -h for more information");
        }
        CommandLine.run(new App(), args);
    }

    @Override
    public void run() {
        if (stationName != null) {
            System.out.println(airIndexForStation());
        }
    }


    public String airIndexForStation() {
//      args = new String[] {"--station=Katowice, ul. Kossutha 6"};
        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();
        Station[] allStations = null;

        if (stationName != null) {
            try {
                allStations = factory.createStations(jsonFetcher.getAllStations());

                int stationID = Station.returnIdOfGivenStation(allStations, stationName);

                AirIndex airIndex = factory.createIndex(jsonFetcher.getQualityIndex(stationID));

                if (airIndex != null) {
                    return airIndex.toString();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
package AirPollution;

import java.io.IOException;

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

    @Option(names = {"--index"}, description = "Air quality index of station of given stationID")
    private int stationID;


    public static void main(String[] args) {
//        args = new String[] {"--index=52"};
        CommandLine.run(new App(), args);
    }

    @Override
    public void run() {
        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();

        JsonAirIndex airIndex = null;
        try {
            airIndex = factory.createIndex(jsonFetcher.getQualityIndex(stationID));
            if (airIndex != null) {
                System.out.println(airIndex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

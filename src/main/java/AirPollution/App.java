package AirPollution;

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

    @Option(names = {"--stationName"}, description = "Station name for Air Index")
    private String stationName;

    @Option(names = {"--date"}, description = "Date of measurement")
    private String date;

    @Option(names = {"--parameterName"}, description = "Name of the parameter")
    private String parameterName;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Try adding -h for more information");
        }
        CommandLine.run(new App(), args);
    }

    @Override
    public void run() {
        OptionsHandler optionsHandler = new OptionsHandler();

        if (stationName != null) {
            System.out.println(optionsHandler.airIndexForStation(stationName));
        }

        // --station="Tarnów, ul. Bitwy pod Studziankami", --parameterName="O3", --date="2018-12-15 21:00:00"
        if (stationName != null && date != null && parameterName != null) {
            System.out.println(optionsHandler.currentParameterValue(date, stationName, parameterName));
        }


    }
}
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

    @Option(names = {"-s", "--stationName"}, description = "Station name for Air Index")
    private String stationName;

    @Option(names = {"-d", "--date"}, description = "Date of measurement")
    private String date;

    @Option(names = {"-par", "--parameterName"}, description = "Name of the parameter")
    private String parameterName;

    @Option(names = {"-st", "--startDate"}, description = "Start date of measurement")
    private String startDate;

    @Option(names = {"-end", "--endDate"}, description = "End date of measurement")
    private String endDate;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Try adding -h for more information");
        }
        CommandLine.run(new App(), args);
    }

    @Override
    public void run() {
        OptionsHandler optionsHandler = new OptionsHandler();

//        if (stationName != null) {
//            System.out.println(optionsHandler.airIndexForStation(stationName));
//        }

        // --station="Tarnów, ul. Bitwy pod Studziankami", --parameterName="O3", --date="2018-12-15 21:00:00"
//        if (stationName != null && date != null && parameterName != null) {
//            System.out.println(optionsHandler.currentParameterValue(date, stationName, parameterName));
//        }
//        java -jar AirPollution-1.0-all.jar --startDate="2018-12-15 19:00:00" --endDate="2018-12-15 21:00:00" --parameterName="O3"
//        if (startDate != null && endDate !=java -jar AirPollution-1.0-all.jar -st "2018-12-15 19:00:00" -end "2018-12-15 21:00:00" -par "O3" -s "Tarnów, ul. Bitwy pod Studziankami" null && parameterName != null) {
//            System.out.println(optionsHandler.averagePollutionValue(startDate, endDate, parameterName));
//        }
//        java -jar AirPollution-1.0-all.jar --startDate="2018-12-15 19:00:00" --endDate="2018-12-15 21:00:00" --parameterName="O3" --stationName="Tarnów, ul. Bitwy pod Studziankami"
//        java -jar AirPollution-1.0-all.jar -st "2018-12-15 19:00:00" -end "2018-12-15 21:00:00" -par "O3" -s "Tarnów, ul. Bitwy pod Studziankami"
//        if(startDate != null && endDate != null && parameterName != null && stationName != null) {
//            System.out.println(optionsHandler.averagePollutionValueForSpecificStation(startDate, endDate, parameterName, stationName));
//        }
        System.out.println(optionsHandler.mostFluctuatingParameter("2018-12-16 07:00:00"));


    }
}
package AirPollution;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command
        (
                name = "Air Pollution in Poland",
                version = "Air Pollution in Poland,\nv1.0 by Jakub Płotnikowski 2018,\nall rights reserved",
                description = "Gives various information about air quality in Poland",
                mixinStandardHelpOptions = true,
                descriptionHeading = "%n@|bold,underline Description:|@%n%n",
                optionListHeading = "%n@|bold,underline Options:|@%n"

        )
public class App implements Runnable {

    @Option(names = {"-a", "-allStations"}, description = "Printing all available stations")
    private boolean all;

    @Option(names = {"-s", "--stationName"}, description = "Station name for Air Index")
    private String stationName;

    @Option(names = {"-d", "--date"}, description = "Date of measurement, \nin format \"yyyy-MM-dd HH:mm:ss\"")
    private String date;

    @Option(names = {"-p", "--parameterName"}, description = "Name of the parameter")
    private String parameterName;

    @Option(names = {"-b", "--beginDate"}, description = "Start date of measurement, " +
            "\nin format \"yyyy-MM-dd HH:mm:ss\"")
    private String startDate;

    @Option(names = {"-e", "--endDate"}, description = "End date of measurement, " +
            "\nin format \"yyyy-MM-dd HH:mm:ss\"")
    private String endDate;

    @Option(names = {"-w", "--sinceWhen"},
            description = "Since when we want to have information about which parameter " +
                    "has biggest difference between maximum and minimum value of pollution, " +
                    "\nin format \"yyyy-MM-dd HH:mm:ss\"")
    private String sinceWhenDate;

    @Option(names = {"-l", "--lowestWhen"}, description = "Give this date when you want to check which parameter's " +
            "pollution was lowest at given time, \nin format \"yyyy-MM-dd HH:mm:ss\"")
    private String dateForLowestParameter;


    public static void main(String[] args) {
        if (args.length == 0) {
//            Displays information about the usage of the program when no arguments are given
            CommandLine.usage(new App(), System.out);
        } else {
            CommandLine.run(new App(), args);
        }
    }


    @Override
    public void run() {
        OptionsHandler optionsHandler = new OptionsHandler();

//java -jar AirPollution-1.0-all.jar -s "Tarnów, ul. Bitwy pod Studziankami" -p "O3" -d "2018-12-15 21:00:00" -b "2018-12-16 17:00:00" -e "2018-12-16 21:00:00" -w "2018-12-16 07:00:00" -l  "2018-12-17 12:00:00"


//        if (stationName != null) {
//            System.out.println(optionsHandler.airIndexForStation(stationName));
//        }
//
//        if (stationName != null && date != null && parameterName != null) {
//            System.out.println(optionsHandler.currentParameterValue(date, stationName, parameterName));
//        }
//
//
//        if (startDate != null && endDate != null && parameterName != null) {
//            System.out.println("Average pollution of parameter: " + parameterName + " from " + startDate + " to "
//                    + endDate + ": " + optionsHandler.multiThreadAveragePollutionValue(startDate, endDate, parameterName));
//        }
//
//        if (startDate != null && endDate != null && parameterName != null && stationName != null) {
//            System.out.println("Pollution of parameter " + parameterName + " in " + stationName + " from "
//                    + startDate + " to " + endDate + ": " +
//                    optionsHandler.averagePollutionValueForSpecificStation(startDate, endDate, parameterName, stationName));
//        }
//
//
//        if (sinceWhenDate != null) {
//            System.out.println(optionsHandler.multiThreadMostFluctuatingParameter(sinceWhenDate));
//        }
//
//        if(dateForLowestParameter != null) {
//            System.out.println(optionsHandler.multiThreadParameterWithLowestValueAtSpecificTime(dateForLowestParameter));
//        }


        if (all) {
            System.out.println(optionsHandler.printerNamesOfAllStations());
        }
    }
}
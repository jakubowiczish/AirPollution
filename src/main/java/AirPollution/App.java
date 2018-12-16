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

    @Option(names = {"-sw", "--sinceWhen"},
            description = "Since when we want to have information about which parameter " +
                    "has biggest difference between maximum and minimum value of pollution")
    private String sinceWhenDate;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Try adding -h for more information");
        }
        CommandLine.run(new App(), args);
    }

    @Override
    public void run() {
        OptionsHandler optionsHandler = new OptionsHandler();

//         java -jar AirPollution-1.0-all.jar -s "Tarnów, ul. Bitwy pod Studziankami"
//        if (stationName != null) {
//            System.out.println(optionsHandler.airIndexForStation(stationName));
//        }

//         java -jar AirPollution-1.0-all.jar -s "Tarnów, ul. Bitwy pod Studziankami" -par "O3" -d "2018-12-15 21:00:00"
//        if (stationName != null && date != null && parameterName != null) {
//            System.out.println(optionsHandler.currentParameterValue(date, stationName, parameterName));
//        }


//        java -jar AirPollution-1.0-all.jar -st "2018-12-16 17:00:00" -end "2018-12-16 18:00:00" -par "O3"
        if (startDate != null && endDate != null && parameterName != null) {
            System.out.println("Average pollution of parameter: " + parameterName + " " + optionsHandler.averagePollutionValue(startDate, endDate, parameterName));
        }

//        "Żywiec, ul. Kopernika  83 a"
//        java -jar AirPollution-1.0-all.jar --startDate="2018-12-16 17:00:00" --endDate="2018-12-16 18:00:00"--parameterName="O3" --stationName="Tarnów, ul. Bitwy pod Studziankami"
//        java -jar AirPollution-1.0-all.jar -st "2018-12-16 17:00:00" -end "2018-12-16 21:00:00" -par "O3" -s "Tarnów, ul. Bitwy pod Studziankami"
        if (startDate != null && endDate != null && parameterName != null && stationName != null) {
            System.out.println("Pollution of parameter " + parameterName + " in " + stationName + " " + optionsHandler.averagePollutionValueForSpecificStation(startDate, endDate, parameterName, stationName));
        }


////        java -jar AirPollution-1.0-all.jar -sw "2018-12-16 07:00:00"
//        if (sinceWhenDate != null) {
//            System.out.println(optionsHandler.mostFluctuatingParameter(sinceWhenDate));
//        }
    }
}
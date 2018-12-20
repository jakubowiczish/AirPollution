package AirPollution;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.text.DecimalFormat;
import java.util.Date;

@Command
        (
                name = "Air Pollution in Poland",
                version = "Air Pollution in Poland,\nv1.0 by Jakub Płotnikowski\n2018,\nAll rights reserved",
                description = "Commandline app that provides you with various information about air quality in Poland",
                mixinStandardHelpOptions = true,
                descriptionHeading = "%n@|bold,underline Description:|@%n%n",
                optionListHeading = "%n@|bold,underline Options:|@%n"

        )
public class App implements Runnable {

    @Option(names = {"-a", "-allStations"}, description = "Printing all available stations")
    private boolean all;

    @Option(names = {"-q", "-allStationsWithSensors"},
            description = "Printing all available stations along with their sensors")
    private boolean allWithSensors;

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
            "pollution was lowest at given time\nassuming that this value is higher than 0, \nin format \"yyyy-MM-dd HH:mm:ss\"")
    private String dateForLowestParameter;

    @Option(names = {"-f", "--fragmentOfAddress"}, description = "Give fragment of address for instance " +
            "street name of city name that you want to find station in")
    private String addressFragment;


    public static void main(String[] args) {
        if (args.length == 0) {
//            Displays information about the usage of the program when no arguments are given
            CommandLine.usage(new App(), System.out);
        } else {
            CommandLine.run(new App(), args);
        }
    }

// zrobiony punkt 1,2,3,4,5
    @Override
    public void run() {
        DecimalFormat decimalFormat = new DecimalFormat("#0.000000");

        PhysicalStorage physicalStorage = new PhysicalStorage();
        Storage storage = physicalStorage.loadStorageFromFile();
        Date currentDate = new Date();

        if (storage == null || (currentDate.getTime() - storage.lastLoadDate.getTime() > 20 * 60 * 1000)) {
            storage = new Storage();
            storage.loadAllData();

            physicalStorage.saveStorageToFile(storage);
        }

        OptionsHandler optionsHandler = new OptionsHandler(storage);
//java -jar AirPollution-1.0-all.jar -s "Tarnów, ul. Bitwy pod Studziankami" -p "O3" -d "2018-12-18 21:00:00" -b "2018-12-18 17:00:00" -e "2018-12-18 21:00:00" -w "2018-12-16 07:00:00" -l  "2018-12-17 12:00:00"


        if (stationName != null) {
            System.out.println(optionsHandler.printerOfAirIndexForGivenStation(stationName));
        }
//java -jar AirPollution-1.0-all.jar -s "Tarnów, ul. Bitwy pod Studziankami" -p "O3" -d "2018-12-18 15:00:00"
        if (stationName != null && date != null && parameterName != null) {
            System.out.println("Parameter: " + parameterName + " and its pollution value on " + date + ": " +
                    optionsHandler.currentValueOfGivenParameterInGivenStation(date, stationName, parameterName));
        }

//java -jar AirPollution-1.0-all.jar -s "Tarnów, ul. Bitwy pod Studziankami" -b "2018-12-18 15:00:00" -e "2018-12-18 16:00:00" -p "O3"
//        if (startDate != null && endDate != null && parameterName != null) {
//            System.out.println("Average pollution of parameter: " + parameterName + " from " + startDate + " to "
//                    + endDate + " is " + optionsHandler.averagePollutionValueOfGivenParameterForAllStations(startDate, endDate, parameterName));
//        }

//java -jar AirPollution-1.0-all.jar -s "Tarnów, ul. Bitwy pod Studziankami" -p "O3" -d "2018-12-20 21:00:00" -b "2018-12-18 17:00:00" -e "2018-12-18 21:00:00"


//java -jar AirPollution-1.0-all.jar -s "Tarnów, ul. Bitwy pod Studziankami" -p "O3" -b "2018-12-19 17:00:00" -e "2018-12-19 21:00:00"
        if (startDate != null && endDate != null && parameterName != null && stationName != null) {
            System.out.println("Pollution of parameter " + parameterName + " in " + stationName + " from "
                    + startDate + " to " + endDate + ": " +
                    decimalFormat.format(optionsHandler.
                            averagePollutionValueOfGivenParameterForSpecificStation(startDate, endDate, parameterName, stationName)));
        }

        if(startDate != null && endDate != null && parameterName != null) {
            System.out.println("Pollution of parameter " + parameterName + " for all stations from "
                    + startDate + " to " + endDate + ": " +
                    decimalFormat.format(optionsHandler.
                            averagePollutionValueOfGivenParameterForAllStations(startDate, endDate, parameterName)));
        }

//
        if (sinceWhenDate != null) {
            System.out.println(optionsHandler.mostFluctuatingParameter(sinceWhenDate));
        }

        if(dateForLowestParameter != null) {
            System.out.println(optionsHandler.parameterWithLowestValueAtSpecificTime(dateForLowestParameter));
        }


        if (addressFragment != null) {
            optionsHandler.printNamesOfAllStationsContainingGivenString(addressFragment);
        }

        if (all) {
            optionsHandler.printNamesOfAllStations();
        }

        if(allWithSensors){
            optionsHandler.printAllStationsWithTheirSensors();
        }
    }
}
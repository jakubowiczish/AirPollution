package AirPollution;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.text.DecimalFormat;
import java.util.ArrayList;
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

    @Option(names = {"-s", "--stationName"}, description = "Station name")
    private String stationName;

    @Option(names = {"-d", "--date"}, description = "Date of measurement, \nin format \"yyyy-MM-dd HH:mm:ss\"")
    private String date;

    @Option(names = {"-p", "--parameterName"}, description = "Name of the parameter")
    private String parameterName;

    @Option(names = {"-b", "--beginDate"}, description = "Start date of measurement, " +
            "\nin format \"yyyy-MM-dd HH:mm:ss\"")
    private String beginDate;

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

    @Option(names = {"-r"}, description = "All parameters available in the system at this very moment")
    private boolean allParameters;

    @Option(names = {"-S"}, description = "List of stations", split = ",")
    private ArrayList<String> listOfStations;

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
        DecimalFormat decimalFormat = new DecimalFormat("#0.000000");

        PhysicalStorage physicalStorage = new PhysicalStorage();
        Storage storage = physicalStorage.loadStorageFromFile();
        Date currentDate = new Date();

        if (storage == null || (currentDate.getTime() - storage.lastLoadDate.getTime() > 20 * 60 * 1000)) {
            storage = new Storage();
            storage.loadAllData();

            physicalStorage.saveStorageToFile(storage);
        }

        AirIndexOptionHandler airIndexOptionHandler = new AirIndexOptionHandler(storage);
        PrintApiInformationOptionHandler printApiInformationOptionHandler = new PrintApiInformationOptionHandler(storage);
        ParameterOptionHandler parameterOptionHandler = new ParameterOptionHandler(storage);
        AveragePollutionHandler averagePollutionHandler = new AveragePollutionHandler(storage);

//java -jar AirPollution-1.0-all.jar -s "Tarnów, ul. Bitwy pod Studziankami" -p "O3" -d "2018-12-18 21:00:00" -b "2018-12-18 17:00:00" -e "2018-12-18 21:00:00" -w "2018-12-16 07:00:00" -l  "2018-12-17 12:00:00"
//
//        if (listOfStations != null) {
//            System.out.println(airIndexOptionHandler.airIndicesOfGivenStations(listOfStations));
//        }
/*
//java -jar AirPollution-1.0-all.jar -s "Tarnów, ul. Bitwy pod Studziankami" -p "O3" -d "2018-12-22 16:00:00"
        if (stationName != null && date != null && parameterName != null) {
            System.out.println("Parameter: " + parameterName + " and its pollution value on " + date + ": " +
                    parameterOptionHandler.valueOfGivenParameterForGivenStationsAndDate(date, stationName, parameterName));
            System.out.println(parameterOptionHandler.valueOfAllParametersForGivenStationsAndDate(date, stationName));
        }

//java -jar AirPollution-1.0-all.jar -s "Tarnów, ul. Bitwy pod Studziankami" -p "O3" -d "2018-12-20 21:00:00" -b "2018-12-18 17:00:00" -e "2018-12-18 21:00:00"

//java -jar AirPollution-1.0-all.jar -s "Tarnów, ul. Bitwy pod Studziankami" -p "O3" -b "2018-12-19 17:00:00" -e "2018-12-19 21:00:00"


        */

//        if (date != null && parameterName != null) {
//            System.out.println(parameterOptionHandler.valueOfGivenParameterForGivenStationsAndDate(date, listOfStations, parameterName));
//        }
//        if (date != null) {
//            System.out.println(parameterOptionHandler.valueOfAllParametersForGivenStationsAndDate(date, listOfStations));
//        }


//        if (sinceWhenDate != null) {
//            System.out.println(parameterOptionHandler.mostFluctuatingParameter(sinceWhenDate, listOfStations));
//        }

//        if (date != null && parameterName != null) {
//            System.out.println(parameterOptionHandler.sortedSensors(listOfStations, date, parameterName));
//        }
/*
        if (dateForLowestParameter != null) {
            System.out.println(parameterOptionHandler.parameterWithLowestValueAtSpecificTime(dateForLowestParameter));
        }


        if (parameterName != null) {
            System.out.println(parameterOptionHandler.parameterExtremeValues(parameterName));
        }

*/
        if (beginDate != null && endDate != null && parameterName != null) {
            System.out.println(averagePollutionHandler.
                    averagePollutionValueOfGivenParameterForGivenStations(beginDate, endDate, parameterName, listOfStations));
        }

        if (all) {
            printApiInformationOptionHandler.printNamesOfAllStations();
        }

        if (addressFragment != null) {
            printApiInformationOptionHandler.printNamesOfAllStationsContainingGivenString(addressFragment);
        }

        if (allWithSensors) {
            printApiInformationOptionHandler.printAllStationsWithTheirSensors();
        }

        if (allParameters) {
            System.out.println(parameterOptionHandler.parameterNames());
        }

    }

}

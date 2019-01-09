package AirPollution;

import AirPollution.api.DataReceiver;
import AirPollution.optionHandler.*;
import AirPollution.storage.PhysicalStorage;
import AirPollution.storage.Storage;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

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

    @Option(names = {"-1"}, description = "give this option if you want to receive AIR INDEX")
    private boolean airIndex_1;

    @Option(names = {"-2"}, description = "give this option if you want to receive value of given parameter for the given date")
    private boolean valueOfParameter_2;

//    @Option(names = {"-2a"}, description = "give this option if you want to receive something for all parameters")
//    private boolean forAllParameters;

    @Option(names = {"-3"}, description = "give this option if you want to receive " +
            "average pollution value of given parameters for given period of time and given stations")
    private boolean averagePollution_3;

    @Option(names = {"-4"}, description = "give this option if you want to receive " +
            "name of the parameter which values were the most fluctuating among all stations")
    private boolean mostFluctuatingParameter_4;

    @Option(names = {"-5"}, description = "give this option if you want to receive" +
            "information about which parameter had lowest and highest pollution at given time")
    private boolean lowestAndHighestValue_5;

    @Option(names = {"-6"}, description = "give this option when you want to " +
            "have printed sorted sensors with highest value of given parameter")
    private boolean sortedStations_6;

    @Option(names = {"-7"}, description = "give this option if you want to receive when and where" +
            "occurred maximum and minimum pollution values of given parameter")
    private boolean extremeValues_7;

    @Option(names = {"-8"}, description = "give this option if you want to have graph_8 printed")
    private boolean graph_8;

    @Option(names = {"-N"}, description = "number of sensors for sorted sensors")
    private int N;

    @Option(names = {"-F"}, description = "if you do not want to fetch data, use this option, " +
            "program will use previously stored data, DATA MAY NOT BE UP-TO-DATE")
    private boolean noDataFetching;

    @Option(names = {"-a", "-allStations"}, description = "Printing all available stations")
    private boolean all;

    @Option(names = {"-q", "-allStationsWithSensors"},
            description = "Printing all available stations along with their sensors")
    private boolean allWithSensors;

    @Option(names = {"-d", "--date"}, description = "give this date when there are no specific requirements, " +
            "\nin format \"yyyy-MM-dd HH:mm:ss\"")
    private String date;

    @Option(names = {"-p", "--parameterName"}, description = "Name of the parameter")
    private String parameterName;

    @Option(names = {"-b", "--beginDate"}, description = "Start date of measurement, " +
            "\nin format \"yyyy-MM-dd HH:mm:ss\"")
    private String beginDate;

    @Option(names = {"-e", "--endDate"}, description = "End date of measurement, " +
            "\nin format \"yyyy-MM-dd HH:mm:ss\"")
    private String endDate;

    @Option(names = {"-B", "--beginHour"}, description = "Start hour for graph")
    private String beginHour;

    @Option(names = {"-E", "--endHour"}, description = "End hour for graph")
    private String endHour;

//    @Option(names = {"-w", "--sinceWhen"},
//            description = "Since when we want to have information about which parameter " +
//                    "has biggest difference between maximum and minimum value of pollution, " +
//                    "\nin format \"yyyy-MM-dd HH:mm:ss\"")
//    private String sinceWhenDate;
//
//    @Option(names = {"-l", "--lowestWhen"}, description = "Give this date when you want to check which parameter's " +
//            "pollution was lowest at given time\nassuming that this value is higher than 0, \nin format \"yyyy-MM-dd HH:mm:ss\"")
//    private String dateForLowestParameter;

    @Option(names = {"-f", "--fragmentOfAddress"}, description = "Give fragment of address for instance " +
            "street name of city name that you want to find station in")
    private String addressFragment;

    @Option(names = {"-r"}, description = "All parameters available in the system at this very moment")
    private boolean allParameters;

    @Option(names = {"-S"}, description = "List of stations", split = ";")
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
        PhysicalStorage physicalStorage = new PhysicalStorage();
        Storage storage = physicalStorage.loadStorageFromFile();

        if (!noDataFetching) {
            if (storage == null || (System.currentTimeMillis() - storage.lastLoadDate.getTime() > 3600 * 1000)) {

                storage = new Storage();
                storage.setDataReceiver(new DataReceiver());
                storage.loadAllData();

                physicalStorage.saveStorageToFile(storage);
            }
        } else {
            if(storage == null) {
                storage = new Storage();
            }
        }

        storage.setDataReceiver(new DataReceiver());

        AirIndexOptionHandler airIndexOptionHandler = new AirIndexOptionHandler(storage);
        PrintApiInformationOptionHandler printApiInformationOptionHandler = new PrintApiInformationOptionHandler(storage);
        ParameterOptionHandler parameterOptionHandler = new ParameterOptionHandler(storage);
        AveragePollutionHandler averagePollutionHandler = new AveragePollutionHandler(storage);
        BarGraphHandler barGraphHandler = new BarGraphHandler(storage);

//java -jar AirPollution-1.0-all.jar -s "Tarnów, ul. Bitwy pod Studziankami" -p "O3" -d "2018-12-18 21:00:00" -b "2018-12-18 17:00:00" -e "2018-12-18 21:00:00" -w "2018-12-16 07:00:00" -l  "2018-12-17 12:00:00"
//
        if (airIndex_1) {
            System.out.println(airIndexOptionHandler.airIndicesOfGivenStations(listOfStations));
        }

        if (valueOfParameter_2 && date != null && parameterName != null) {
            System.out.println(parameterOptionHandler.valueOfGivenParameterForGivenStationsAndDate(date, listOfStations, parameterName));
        }

        if (averagePollution_3 && beginDate != null && endDate != null && parameterName != null) {
            System.out.println(averagePollutionHandler.averagePollutionValueOfGivenParameterForGivenStations(beginDate, endDate, parameterName, listOfStations));
        }

        if (mostFluctuatingParameter_4 && date != null) {
            System.out.println(parameterOptionHandler.mostFluctuatingParameter(date, listOfStations));
        }

        if (lowestAndHighestValue_5 && date != null) {
            System.out.println(parameterOptionHandler.parametersWithLowestAndHighestValuesAtSpecificTime(date));
        }

        if (sortedStations_6 && date != null && parameterName != null) {
            System.out.println(parameterOptionHandler.sortedStations(listOfStations, date, parameterName, N));
        }

        if (sortedStations_6 && date != null) {
            System.out.println(parameterOptionHandler.stationsAboveStandardPollutionValue(listOfStations, date, N));

        }

        if (extremeValues_7 && parameterName != null) {
            System.out.println(parameterOptionHandler.parameterExtremeValues(parameterName));
        }

        if (graph_8 && beginHour != null && endHour != null) {
            System.out.println(barGraphHandler.barGraphForGivenParameterStationsAndPeriodOfTime(beginHour, endHour, parameterName, listOfStations, LocalDate.now()));
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

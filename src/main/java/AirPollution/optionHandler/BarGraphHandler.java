package AirPollution.optionHandler;

import AirPollution.model.Sensor;
import AirPollution.model.Station;
import AirPollution.storage.Storage;
import AirPollution.utils.Utils;
import com.google.common.base.Strings;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Provides method that can be used to print graph which presents information about pollution, its date and place
 */
public class BarGraphHandler {

    private Storage storageReceiver;

    public BarGraphHandler(Storage storageReceiver) {
        this.storageReceiver = storageReceiver;
    }


    /**
     * Returns a graph that contains information about pollution,
     * dates of measurements and names of the stations,
     * everything for desired period of time.
     * For arguments: beginDate: "05:00:00", endDate: "05:00:00", parameter CO and station names:
     * "Szczecin_Piłsudskiego;Radom-Tochtermana" there is an exemplary graph:
     * 05:00:00 TODAY                (Szczecin_Piłsudskiego)                        ■■■ 2.7803
     * 05:00:00 YESTERDAY            (Szczecin_Piłsudskiego)                        ■■■■■■■■■ 7.8078
     * 05:00:00 DAY BEFORE YESTERDAY (Szczecin_Piłsudskiego)                        ■■■■■■■■■■■■ 10.8047
     * 05:00:00 TODAY                (Radom-Tochtermana)                            ■■■ 2.5300
     * 05:00:00 YESTERDAY            (Radom-Tochtermana)                            ■■■■■■■■■■■■■■■■ 14.1700
     * 05:00:00 DAY BEFORE YESTERDAY (Radom-Tochtermana)                            ■■■■■■ 5.7300
     *
     * @param beginHour      start date of period of time for which the graph will be created
     * @param endHour        end date of period of time for which the graph will be created
     * @param parameterName  name of the parameter for which the graph will be created
     * @param listOfStations names of the stations for which you want the graph to be created
     * @return String that is a graph that contains information about pollution, dates of measurement and names of the stations
     */
    public String barGraphForGivenParameterStationsAndPeriodOfTime(String beginHour,
                                                                   String endHour,
                                                                   String parameterName,
                                                                   List<String> listOfStations,
                                                                   LocalDate localDate) {

        Date hourBeginDate = Utils.getInstance().parseStringToHour(beginHour);
        Date hourEndDate = Utils.getInstance().parseStringToHour(endHour);

        if (!Utils.getInstance().checkWhetherParameterNameIsValid(parameterName)) {
            System.out.println("Given parameter name: \"" + parameterName + "\" is not valid");
            return null;
        }

        List<Station> allStations = storageReceiver.getAllStations();
        List<Station> validStations = Utils.getInstance().assignValidStations(listOfStations, allStations);
        allStations = Utils.getInstance().assignAllStations(allStations, validStations);

        int maxLengthOfLine = 110;
        int longestStationNameLength = findLongestStationName(allStations);

        double maxValue = findMaximumValueOfGivenParameter(allStations, parameterName);

        String stringBuilder;

        TreeMap<Date, List<String>> graphSortedByHours = new TreeMap<>();

        allStations.stream()
                .filter(Objects::nonNull)
                .forEach(station -> {
                    CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.getId());
                    if (sensors == null) return;
                    sensors.stream().map(sensor -> storageReceiver.getSensorDataForSpecificSensor(sensor.getId())).filter(sensorData -> sensorData != null && sensorData.getValues().length != 0 && sensorData.getKey().equals(parameterName)).flatMap(sensorData -> Arrays.stream(sensorData.getValues())).filter(value -> value.date.contains("-")).filter(value -> value.value != null).forEach(value -> {

                        Date actualDate = Utils.getInstance().multiThreadParseStringToDate(value.date);
                        String[] dateParts = value.date.split(" ");
                        Date actualHourDate = Utils.getInstance().parseStringToHour(dateParts[1]);

                        if (!Utils.getInstance().checkDateInterval(hourBeginDate, hourEndDate, actualHourDate)) return;

                        int lengthOfGraphLine = (int) ((value.value / maxValue) * maxLengthOfLine);

                        String graphLine = createStringOfGivenLengthAndCharacter(lengthOfGraphLine, "\u25a0");

                        int blankSpaceLength = longestStationNameLength - station.getStationName().length() + 1;

                        if (isToday(localDate, actualDate)) {
                            Date keyDate = Utils.getInstance().parseStringToHour(dateParts[1]);
                            String valueString = "\r" + dateParts[1] +
                                    " TODAY               " + " (" + station.getStationName() + ")" +
                                    createStringOfGivenLengthAndCharacter(blankSpaceLength, " ") +
                                    graphLine + " " + Utils.getInstance().getDecimalFormat().format(value.value) +
                                    "\n";
                            Utils.getInstance().addToTreeWithDateAndString(graphSortedByHours, keyDate, valueString);

                        } else if (wasYesterday(localDate, actualDate)) {
                            Date keyDate = Utils.getInstance().parseStringToHour(dateParts[1]);
                            String valueString = "\r" + dateParts[1] +
                                    " YESTERDAY           " + " (" + station.getStationName() + ")" +
                                    createStringOfGivenLengthAndCharacter(blankSpaceLength, " ") +
                                    graphLine + " " + Utils.getInstance().getDecimalFormat().format(value.value) +
                                    "\n";
                            Utils.getInstance().addToTreeWithDateAndString(graphSortedByHours, keyDate, valueString);

                        } else if (wasDayBeforeYesterday(localDate, actualDate)) {
                            Date keyDate = Utils.getInstance().parseStringToHour(dateParts[1]);
                            String valueString = "\r" + dateParts[1] +
                                    " DAY BEFORE YESTERDAY" + " (" + station.getStationName() + ")" +
                                    createStringOfGivenLengthAndCharacter(blankSpaceLength, " ") +
                                    graphLine + " " + Utils.getInstance().getDecimalFormat().format(value.value) +
                                    "\n";
                            Utils.getInstance().addToTreeWithDateAndString(graphSortedByHours, keyDate, valueString);
                        }
                    });
                });

        stringBuilder = graphSortedByHours.values().stream().map(String::valueOf).collect(Collectors.joining());

        String resultString = stringBuilder;
        resultString = Utils.getInstance().cleanUpString(resultString);

        return resultString;
    }


    private double findMaximumValueOfGivenParameter(List<Station> allStations, String parameterName) {
        return allStations.stream()
                .filter(Objects::nonNull)
                .map(station -> storageReceiver.getAllSensorsForSpecificStation(station.getId()))
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .map(sensor -> storageReceiver.getSensorDataForSpecificSensor(sensor.getId()))
                .filter(sensorData -> sensorData != null
                        && sensorData.getValues().length != 0
                        && sensorData.getKey().equals(parameterName))
                .flatMap(sensorData -> Arrays.stream(sensorData.getValues()))
                .filter(value -> value.value != null)
                .map(value -> value.value)
                .mapToDouble(value -> value)
                .filter(value -> value >= -1.0)
                .max()
                .orElse(-1.0);
    }

    private int findLongestStationName(List<Station> allStations) {
        return allStations.stream()
                .mapToInt(station -> station.getStationName().length())
                .max()
                .orElse(-1);
    }

    private String createStringOfGivenLengthAndCharacter(int length, String character) {
        return Strings.repeat(character, length);
    }

    /**
     * Returns information about whether the given date was yesterday
     *
     * @param actualDate date to check
     * @return boolean value, 1 - if the given date was yesterday, 0 - if it was not
     */
    private boolean wasYesterday(LocalDate localDate, Date actualDate) {
        LocalDate yesterday = localDate.minusDays(1);
        LocalDate realDate = actualDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return yesterday.equals(realDate);
    }

    /**
     * Returns information about whether the given date is today
     *
     * @param actualDate date to check
     * @return boolean value, 1 - if the given date is today, 0 - if it is not
     */
    private boolean isToday(LocalDate localDate, Date actualDate) {
        LocalDate realDate = actualDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.equals(realDate);
    }

    /**
     * Returns information about whether the given date was day before yesterday
     *
     * @param actualDate date to check
     * @return boolean value, 1 - if the given date was the day before yesterday, 0 - if it was not
     */
    private boolean wasDayBeforeYesterday(LocalDate localDate, Date actualDate) {
        LocalDate dayBeforeYesterday = localDate.minusDays(2);
        LocalDate realDate = actualDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return dayBeforeYesterday.equals(realDate);
    }
}

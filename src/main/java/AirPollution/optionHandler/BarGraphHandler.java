package AirPollution.optionHandler;

import AirPollution.storage.Storage;
import AirPollution.utils.Utils;
import AirPollution.model.Sensor;
import AirPollution.model.SensorData;
import AirPollution.model.Station;
import com.google.common.base.Strings;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Provides method that can be used to print graph which presents information about pollution, its date and place
 */
public class BarGraphHandler {

    private Storage storageReceiver;

    public BarGraphHandler(Storage storageReceiver) {
        this.storageReceiver = storageReceiver;
    }

    private LocalDate today = LocalDate.now(); // 2018-12-27
    private LocalDate yesterday = LocalDate.now().minusDays(1);
    private LocalDate dayBeforeYesterday = LocalDate.now().minusDays(2);

    /**
     * Returns a graph that contains information about pollution,
     * dates of measurements and names of the stations,
     * everything for desired period of time.
     * for beginDate: "00:00:00", endDate: "02:00:00", parameter CO and one station name - "Zgierz-Śródmieście" there is an exemplary graph:
     * 00:00:00 TODAY     (Zgierz-Śródmieście) ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 398.0
     * 00:00:00 YESTERDAY (Zgierz-Śródmieście) ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 565.0
     * 01:00:00 TODAY     (Zgierz-Śródmieście) ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 379.0
     * 01:00:00 YESTERDAY (Zgierz-Śródmieście) ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■ 516.0
     *
     * @param beginDate start date of period of time for which the graph will be created
     * @param endDate end date of period of time for which the graph will be created
     * @param parameterName name of the parameter for which the graph will be created
     * @param listOfStations names of the stations for which you want the graph to be created
     * @return String that is a graph that contains information about pollution, dates of measurement and names of the station
     */
    public String barGraphForGivenParameterStationsAndPeriodOfTime(String beginDate, String endDate, String parameterName, ArrayList<String> listOfStations) {
        Date hourBeginDate = Utils.parseStringToHour(beginDate);
        Date hourEndDate = Utils.parseStringToHour(endDate);

        if (!Utils.checkWhetherParameterNameIsValid(parameterName)) {
            System.out.println("Given parameter name: \"" + parameterName + "\" is not valid");
            return null;
        }

        ArrayList<Station> allStations = storageReceiver.getAllStations();
        ArrayList<Station> validStations = Utils.assignValidStations(listOfStations, allStations);
        allStations = Utils.assignAllStations(allStations, validStations);

        int maxLengthOfLine = 150;
        int longestStationNameLength = findLongestStationName(allStations);

        double maxValue = findMaximumValueOfGivenParameter(allStations, parameterName);

        StringBuilder stringBuilder = new StringBuilder();

        TreeMap<Date, ArrayList<String>> graphSortedByHours = new TreeMap<>();

        for (Station station : allStations) {
            if (station == null) continue;
            CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.getId());
            if (sensors == null) continue;
            for (Sensor sensor : sensors) {
                SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.getId());
                if (sensorData == null || sensorData.getValues().length == 0 || !sensorData.getKey().equals(parameterName))
                    continue;

                for (SensorData.Value value : sensorData.getValues()) {
                    if (!value.date.contains("-")) continue;
                    if (value.value == null) continue;
                    Date actualDate = Utils.multiThreadParseStringToDate(value.date);

                    String[] dateParts = value.date.split(" ");
                    Date actualHourDate = Utils.parseStringToHour(dateParts[1]);
                    if (!Utils.checkDateInterval(hourBeginDate, hourEndDate, actualHourDate)) continue;

                    int lengthOfGraphLine = (int) ((value.value / maxValue) * maxLengthOfLine);
                    String graphLine = createStringOfGivenLengthAndCharacter(lengthOfGraphLine, "\u25a0");
                    int blankSpaceLength = longestStationNameLength - station.getStationName().length() + 1;


                    if (isToday(actualDate)) {
                        Date keyDate = Utils.parseStringToHour(dateParts[1]);
                        String valueString = dateParts[1] +
                                " TODAY               " + " (" + station.getStationName() + ")" +
                                createStringOfGivenLengthAndCharacter(blankSpaceLength, " ") +
                                graphLine + " " + value.value +
                                "\n";
                        Utils.addToTreeWithDateAndString(graphSortedByHours, keyDate, valueString);

                    } else if (wasYesterday(actualDate)) {
                        Date keyDate = Utils.parseStringToHour(dateParts[1]);
                        String valueString = dateParts[1] +
                                " YESTERDAY           " + " (" + station.getStationName() + ")" +
                                createStringOfGivenLengthAndCharacter(blankSpaceLength, " ") +
                                graphLine + " " + value.value +
                                "\n";
                        Utils.addToTreeWithDateAndString(graphSortedByHours, keyDate, valueString);

                    } else if (wasDayBeforeYesterday(actualDate)) {
                        Date keyDate = Utils.parseStringToHour(dateParts[1]);
                        String valueString = dateParts[1] +
                                " DAY BEFORE YESTERDAY" + " (" + station.getStationName() + ")" +
                                createStringOfGivenLengthAndCharacter(blankSpaceLength, " ") +
                                graphLine + " " + value.value +
                                "\n";
                        Utils.addToTreeWithDateAndString(graphSortedByHours, keyDate, valueString);
                    }
                }
            }
        }

        for (Map.Entry<Date, ArrayList<String>> entry : graphSortedByHours.entrySet()) {
            stringBuilder.append(entry.getValue());
        }

        String resultString = stringBuilder.toString();
        resultString = cleanUpGraphString(resultString);

        return resultString;
    }


    private double findMaximumValueOfGivenParameter(ArrayList<Station> allStations, String parameterName) {
        double maxValue = -1.0;
        for (Station station : allStations) {
            if (station == null) continue;
            CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.getId());
            if (sensors == null) continue;
            for (Sensor sensor : sensors) {
                SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.getId());
                if (sensorData == null || sensorData.getValues().length == 0 || !sensorData.getKey().equals(parameterName))
                    continue;

                for (SensorData.Value value : sensorData.getValues()) {
                    if (value.value == null) continue;
                    if (value.value > maxValue) {
                        maxValue = value.value;
                    }
                }
            }
        }
        return maxValue;
    }

    private int findLongestStationName(ArrayList<Station> allStations) {
        int maxLength = -1;
        for (Station station : allStations) {
            if (station.getStationName().length() > maxLength) {
                maxLength = station.getStationName().length();
            }
        }
        return maxLength;
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
    public boolean wasYesterday(Date actualDate) {
        LocalDate realDate = actualDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return yesterday.equals(realDate);
    }

    /**
     * Returns information about whether the given date is today
     *
     * @param actualDate date to check
     * @return boolean value, 1 - if the given date is today, 0 - if it is not
     */
    public boolean isToday(Date actualDate) {
        LocalDate realDate = actualDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return today.equals(realDate);
    }

    /**
     * Returns information about whether the given date was day before yesterday
     *
     * @param actualDate date to check
     * @return boolean value, 1 - if the given date was the day before yesterday, 0 - if it was not
     */
    public boolean wasDayBeforeYesterday(Date actualDate) {
        LocalDate realDate = actualDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return dayBeforeYesterday.equals(realDate);
    }

    private String replaceChar(String str, Character ch, int index) {
        return str.substring(0, index) + ch + str.substring(index + 1);
    }

    private String cleanUpGraphString(String resultString) {
        resultString = resultString.replaceAll("\\[", "");
        resultString = resultString.replaceAll("]", "");

        int bracketCounter = 0;
        for (int i = 1; i < resultString.length(); i++) {
            if (resultString.charAt(i) == '(') {
                bracketCounter++;
            }
            if (resultString.charAt(i) == ')') {
                bracketCounter--;
            }

            if (bracketCounter % 2 == 0) {
                if (resultString.charAt(i - 1) == ',' && resultString.charAt(i) == ' ') {
                    resultString = replaceChar(resultString, Character.MIN_VALUE, i - 1);
                    resultString = replaceChar(resultString, Character.MIN_VALUE, i);


                }
            }
        }
        return resultString;
    }
}

package AirPollution;

import com.google.common.base.Strings;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;


public class BarGraphHandler {

    private Storage storageReceiver;

    public BarGraphHandler(Storage storageReceiver) {
        this.storageReceiver = storageReceiver;
    }

    LocalDate today = LocalDate.now(); // 2018-12-27
    LocalDate yesterday = LocalDate.now().minusDays(1);


    private double findMaximumValueOfGivenParameter(ArrayList<Station> allStations, String parameterName) {
        double maxValue = -1.0;
        for (Station station : allStations) {
            if (station == null) continue;
            CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.id);
            if (sensors == null) continue;
            for (Sensor sensor : sensors) {
                SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.id);
                if (sensorData == null || sensorData.values.length == 0 || !sensorData.key.equals(parameterName))
                    continue;

                for (SensorData.Value value : sensorData.values) {
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
            if (station.stationName.length() > maxLength) {
                maxLength = station.stationName.length();
            }
        }
        return maxLength;
    }

    private String createStringOfGivenLengthAndCharacter(int length, String character) {
        return Strings.repeat(character, length);
    }


    public boolean wasYesterday(Date actualDate) {
        LocalDate realDate = actualDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return yesterday.equals(realDate);
    }

    private boolean isToday(Date actualDate) {
        LocalDate realDate = actualDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return today.equals(realDate);
    }



    public String barGraphForGivenParameterStationsAndPeriodOfTime(String startDate, String endDate, String parameterName, ArrayList<String> listOfStations) {
        Date realStartDate = Utils.parseAndCheckDate(startDate);
        Date realEndDate = Utils.parseAndCheckDate(endDate);

        if (!Utils.checkWhetherParameterNameIsValid(parameterName)) {
            System.out.println("Given parameter name: \"" + parameterName + "\" is not valid");
            return null;
        }

        ArrayList<Station> allStations = storageReceiver.getAllStations();
        ArrayList<Station> validStations = Utils.assignValidStations(listOfStations, allStations);
        allStations = Utils.assignAllStations(allStations, validStations);

        int maxLengthOfLine = 100;
        int longestStationNameLength = findLongestStationName(allStations);

        double maxValue = findMaximumValueOfGivenParameter(allStations, parameterName);

        StringBuilder stringBuilder = new StringBuilder();

        for (Station station : allStations) {
            if (station == null) continue;
            CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.id);
            if (sensors == null) continue;
            for (Sensor sensor : sensors) {
                SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.id);
                if (sensorData == null || sensorData.values.length == 0) continue;
                if (!sensorData.key.equals(parameterName)) {
                    System.out.println("There is no such parameter as: " + parameterName +
                            " for station: " + station.stationName +
                            " and sensor: " + sensor.id);
                    continue;
                }
                for (SensorData.Value value : sensorData.values) {
                    if (!value.date.contains("-")) continue;
                    if (value.value == null) continue;
                    Date actualDate = Utils.multiThreadParseStringToDate(value.date);
                    if (!Utils.checkDateInterval(realStartDate, realEndDate, actualDate)) continue;

                    int lengthOfGraphLine = (int) ((value.value / maxValue) * maxLengthOfLine);
                    String graphLine = createStringOfGivenLengthAndCharacter(lengthOfGraphLine, "#");
                    String[] dateParts = value.date.split(" ");
                    int blankSpaceLength = longestStationNameLength - station.stationName.length() + 1;
                    if (isToday(actualDate)) {
                        stringBuilder.
                                append(dateParts[1]).
                                append(" TODAY               ").append(" (").append(station.stationName).append(")").
                                append(createStringOfGivenLengthAndCharacter(blankSpaceLength, " ")).
                                append(graphLine).append(" ").append(value.value).
                                append("\n");

                    } else if (wasYesterday(actualDate)) {
                        stringBuilder.
                                append(dateParts[1]).
                                append(" YESTERDAY           ").append(" (").append(station.stationName).append(")").
                                append(createStringOfGivenLengthAndCharacter(blankSpaceLength, " ")).
                                append(graphLine).append(" ").append(value.value).
                                append("\n");

                    } else {
                        stringBuilder.
                                append(dateParts[1]).
                                append(" DAY BEFORE YESTERDAY").append(" (").append(station.stationName).append(")").
                                append(createStringOfGivenLengthAndCharacter(blankSpaceLength, " ")).
                                append(graphLine).append(" ").append(value.value).
                                append("\n");
                    }
                }
            }
        }
        return stringBuilder.toString();
    }
}

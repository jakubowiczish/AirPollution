package AirPollution.optionHandler;

import AirPollution.storage.Storage;
import AirPollution.utils.Utils;
import AirPollution.model.Sensor;
import AirPollution.model.SensorData;
import AirPollution.model.Station;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class that is used to provide user with information about average pollution
 */
public class AveragePollutionHandler {
    private Storage storageReceiver;

    public AveragePollutionHandler(Storage storageReceiver) {
        this.storageReceiver = storageReceiver;
    }

    /**
     * Returns average pollution of given parameter for specified stations for given period of time
     *
     * @param beginDate start date of period of time for which you want to know the average value of given parameter
     * @param endDate end date of period of time for which you want to know the average value of given parameter
     * @param parameterName name of the parameter which average value you want to get to know
     * @param listOfStations list of names of stations for which average value of given parameter will be calculated
     * @return String that contains information about the average value of given parameter for specified stations and specified time period
     */
    public String averagePollutionValueOfGivenParameterForGivenStations(String beginDate, String endDate, String parameterName, ArrayList<String> listOfStations) {
        double sumOfValues = 0;
        int valuesCounter = 0;

        Date realBeginDate = Utils.parseAndCheckDate(beginDate);
        Date realEndDate = Utils.parseAndCheckDate(endDate);

        ArrayList<Station> allStations = storageReceiver.getAllStations();
        ArrayList<Station> validStations = Utils.assignValidStations(listOfStations, allStations);
        allStations = Utils.assignAllStations(allStations, validStations);


        if (!Utils.checkWhetherParameterNameIsValid(parameterName)) {
            System.out.println("Given parameter does not exist in the system");
            return null;
        }

        for (Station station : allStations) {
            if (station == null) continue;

            CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.getId());
            for (Sensor sensor : sensors) {
                if (sensor == null) continue;

                SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.getId());
                if (sensorData == null) continue;
                if (sensorData.getKey().equals(parameterName)) {
                    for (SensorData.Value value : sensorData.getValues()) {
                        if (!value.date.contains("-")) continue;
                        Date actualDate = Utils.multiThreadParseStringToDate(value.date);
                        // if date is between given period of time
                        if (Utils.checkDateInterval(realBeginDate, realEndDate, actualDate)) {
                            if (value.value != null) {
                                valuesCounter++;
                                sumOfValues += value.value;
                            }
                        }
                    }
                }
            }
        }

        StringBuilder stringBuilder = new StringBuilder();

        if (validStations != null && validStations.size() > 0) {
            stringBuilder.
                    append("Average pollution value of parameter: ").
                    append(parameterName).append("\nfrom: ").append(beginDate).append(" to: ").append(endDate).
                    append("\nfor GIVEN stations:\n");
            for (Station station : validStations) {
                stringBuilder.append(station.getStationName()).append("\n");
            }
            stringBuilder.append("is equal to: ");
        } else {
            stringBuilder.append("Average pollution value of parameter: ").append(parameterName).append("\nfrom: ").
                    append(beginDate).append(" to: ").append(endDate).append(" for ALL stations is equal to: ");
        }
        stringBuilder.append(Utils.decimalFormat.format(sumOfValues / valuesCounter));
        return stringBuilder.toString();
    }
}

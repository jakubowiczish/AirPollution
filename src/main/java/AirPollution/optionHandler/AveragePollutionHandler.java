package AirPollution.optionHandler;

import AirPollution.storage.Storage;
import AirPollution.utils.Utils;
import AirPollution.model.Sensor;
import AirPollution.model.SensorData;
import AirPollution.model.Station;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

public class AveragePollutionHandler {
    private Storage storageReceiver;

    public AveragePollutionHandler(Storage storageReceiver) {
        this.storageReceiver = storageReceiver;
    }

    public double averagePollutionValueOfGivenParameterForGivenStations(String beginDate, String endDate, String parameterName, ArrayList<String> listOfStations) {
        double sumOfValues = 0;
        int valuesCounter = 0;

        Date realBeginDate = Utils.parseAndCheckDate(beginDate);
        Date realEndDate = Utils.parseAndCheckDate(endDate);

        ArrayList<Station> allStations = storageReceiver.getAllStations();
        ArrayList<Station> validStations = Utils.assignValidStations(listOfStations, allStations);
        allStations = Utils.assignAllStations(allStations, validStations);


        if (!Utils.checkWhetherParameterNameIsValid(parameterName)) {
            System.out.println("Given parameter does not exist in the system");
            return -1.0;
        }

        for (Station station : allStations) {
            if (station == null) continue;
            CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.id);

//            boolean foundParameter = false;

            for (Sensor sensor : sensors) {
                if (sensor == null) continue;

                SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.id);
                if (sensorData == null) continue;

                if (sensorData.key.equals(parameterName)) {
//                    foundParameter = true;
                    for (SensorData.Value value : sensorData.values) {
                        if (value.date.contains("-")) {
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
//            if (!foundParameter) {
//                System.out.println("There is no such parameter as: \"" + parameterName + " for station: " + station.stationName);
//            }
        }

        if (validStations != null && validStations.size() > 0) {
            System.out.println("Average pollution value of parameter: " + parameterName +
                    "\nfrom: " + beginDate + " to: " + endDate + " for GIVEN stations: ");
            for (Station station : validStations) {
                System.out.println(station.stationName);
            }
            System.out.print("is equal to: ");
        } else {
            System.out.print("Average pollution value of parameter: " + parameterName +
                    "\nfrom: " + beginDate + " to: " + endDate + " for ALL stations is equal to: ");
        }

        return sumOfValues / valuesCounter;
    }
}

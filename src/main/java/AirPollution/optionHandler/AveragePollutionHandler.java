package AirPollution.optionHandler;

import AirPollution.storage.Storage;
import AirPollution.utils.Utils;
import AirPollution.model.Sensor;
import AirPollution.model.SensorData;
import AirPollution.model.Station;
import com.google.common.util.concurrent.AtomicDouble;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class AveragePollutionHandler {
    private Storage storageReceiver;

    public AveragePollutionHandler(Storage storageReceiver) {
        this.storageReceiver = storageReceiver;
    }

    public double averagePollutionValueOfGivenParameterForGivenStations(String beginDate, String endDate, String parameterName, ArrayList<String> listOfStations) {
        final AtomicDouble sumOfValues = new AtomicDouble();
        final AtomicInteger valuesCounter = new AtomicInteger();

        Date realStartDate = Utils.parseAndCheckDate(beginDate);
        Date realEndDate = Utils.parseAndCheckDate(endDate);

        ArrayList<Station> allStations = storageReceiver.getAllStations();
        ArrayList<Station> validStations = Utils.assignValidStations(listOfStations, allStations);
        allStations = Utils.assignAllStations(allStations, validStations);


        if (!Utils.checkWhetherParameterNameIsValid(parameterName)) {
            System.out.println("Given parameter does not exist in the system");
            return -1.0;
        }

        LinkedList<Thread> threads = new LinkedList<>();

        for (Station station : allStations) {
            if (station == null) continue;
            Thread thread = new Thread(() -> {
                CopyOnWriteArrayList<Sensor> sensors = storageReceiver.getAllSensorsForSpecificStation(station.id);

                boolean foundParameter = false;
                for (Sensor sensor : sensors) {
                    if (sensor == null) continue;

                    SensorData sensorData = storageReceiver.getSensorDataForSpecificSensor(sensor.id);
                    if (sensorData == null) continue;

                    if (sensorData.key.equals(parameterName)) {
                        foundParameter = true;
                        for (SensorData.Value value : sensorData.values) {
                            if (value.date.contains("-")) {
                                Date actualDate = Utils.multiThreadParseStringToDate(value.date);
                                // if date is between given period of time
                                if (Utils.checkDateInterval(realStartDate, realEndDate, actualDate)) {
                                    if (value.value != null) {
                                        valuesCounter.incrementAndGet();
                                        sumOfValues.addAndGet(value.value);
                                    }
                                }
                            }
                        }
                    }
                }
            });
            threads.add(thread);
        }


        Utils.startAndJoinThreads(threads);


        if (validStations != null && validStations.size() > 0) {
            System.out.println("Average pollution value of parameter: " + parameterName + " for GIVEN stations: ");
            for (Station station : validStations) {
                System.out.println(station.stationName);
            }
            System.out.print("is equal to: ");
        } else {
            System.out.print("Average pollution value of parameter: " + parameterName + " for ALL stations is equal to: ");
        }

        return sumOfValues.get() / valuesCounter.get();
    }
}

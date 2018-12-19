package AirPollution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataReceiver {

    public SensorData getSensorDataForSpecificSensor(int sensorID) {
        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();
        SensorData sensorData = null;
        try {
            sensorData = factory.createSensorData(jsonFetcher.getSensorData(sensorID));
        } catch (IOException e) {
            System.out.println("There is no SensorData for this sensor: " + sensorID);
            e.printStackTrace();
            return null;
        }
        return sensorData;
    }

    public AirIndex getAirIndexOfSpecificStation(int stationID) {
        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();
        AirIndex airIndex = null;
        try {
            airIndex = factory.createAirIndex(jsonFetcher.getQualityIndex(stationID));
        } catch (IOException e) {
            System.out.println("There is no AirIndex for this station: " + stationID);
            e.printStackTrace();
            return null;
        }
        return airIndex;
    }

    public CopyOnWriteArrayList<Sensor> getAllSensorsForSpecificStation(int stationID) {
        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();
        Sensor[] allSensors = new Sensor[0];

        try {
            allSensors = factory.createSensors(jsonFetcher.getSensors(stationID));
        } catch (IOException e) {
            e.printStackTrace();
        }
        CopyOnWriteArrayList<Sensor> validSensors = new CopyOnWriteArrayList<>();
        for (Sensor sensor : allSensors) {
            if (sensor != null) {
                validSensors.add(sensor);
            }
        }
//        System.out.println(validSensors.size() + " sensors found for station: \"" + stationName + "\"\n");
        return validSensors;
    }


    public ArrayList<Station> getAllStations() {
        Factory factory = new Factory();
        JsonFetcher jsonFetcher = new JsonFetcher();
        Station[] allStations = new Station[0];
        try {
            allStations = factory.createStations(jsonFetcher.getAllStations());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Station> validStations = new ArrayList<>();
        for(Station station : allStations) {
            if (station != null) {
                validStations.add(station);
            }
        }
        //        System.out.println(validStations.length + " stations found\n");
        return validStations;
    }


//    private <T> T[] getNoNullArray(Class<T> tClass, T[] array) {
//        int validElementsCounter = 0;
//        for (int i = 0; i < array.length; i++) {
//            if (array[i] != null) {
//                validElementsCounter++;
//            }
//        }
//
//        T[] newArray = (T[]) Array.newInstance(tClass, validElementsCounter);
//        int j = 0;
//
//        for (int i = 0; i < array.length; i++) {
//            if (array[i] != null) {
//                newArray[j++] = array[i];
//            }
//        }
//        return newArray;
//    }
}

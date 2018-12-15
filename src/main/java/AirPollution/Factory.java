package AirPollution;

import com.google.gson.Gson;

public class Factory {

    public Station[] createStations(String stationsString) {
        Gson gson = new Gson();
        return gson.fromJson(stationsString, Station[].class);
    }

    public SensorData createSensorData(String stationsString) {
        Gson gson = new Gson();
        return gson.fromJson(stationsString, SensorData.class);
    }

    public Sensor createSensor(String stationsString) {
        Gson gson = new Gson();
        return gson.fromJson(stationsString, Sensor.class);
    }


    public AirIndex createAirIndex(String stationsString) {
        Gson gson = new Gson();
        return gson.fromJson(stationsString, AirIndex.class);

    }
}

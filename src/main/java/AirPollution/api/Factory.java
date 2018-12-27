package AirPollution.api;

import AirPollution.model.AirIndex;
import AirPollution.model.Sensor;
import AirPollution.model.SensorData;
import AirPollution.model.Station;
import com.google.gson.Gson;

public class Factory {

    public Station[] createStations(String stationsString) {
        Gson gson = new Gson();
        return gson.fromJson(stationsString, Station[].class);
    }

    public Sensor[] createSensors(String stationsString) {
        Gson gson = new Gson();
        return gson.fromJson(stationsString, Sensor[].class);
    }

    public SensorData createSensorData(String stationsString) {
        Gson gson = new Gson();
        return gson.fromJson(stationsString, SensorData.class);
    }

    public AirIndex createAirIndex(String stationsString) {
        Gson gson = new Gson();
        return gson.fromJson(stationsString, AirIndex.class);

    }
}

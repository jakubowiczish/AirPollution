package AirPollution.api;

import AirPollution.model.AirIndex;
import AirPollution.model.Sensor;
import AirPollution.model.SensorData;
import AirPollution.model.Station;
import com.google.gson.Gson;

/**
 * Class used to parse String into objects that can be later used,
 * uses Gson - JsonParser created by Google
 */
public class Factory {

    /**
     * Parses given String, the internal content from: http://api.gios.gov.pl/pjp-api/rest/station/findAll
     *
     * @param stationsString content from http://api.gios.gov.pl/pjp-api/rest/station/findAll
     * @return Array of stations parsed from given String
     */
    public Station[] createStations(String stationsString) {
        Gson gson = new Gson();
        return gson.fromJson(stationsString, Station[].class);
    }

    /**
     * Parses given String, the internal content from: http://api.gios.gov.pl/pjp-api/rest/station/sensors/"stationID"
     * where in place of "stationID" there can be any id of station that exists in the system
     *
     * @param stationsString content from, for instance: http://api.gios.gov.pl/pjp-api/rest/station/sensors/444
     * @return Array of sensors parsed from given String
     */
    public Sensor[] createSensors(String stationsString) {
        Gson gson = new Gson();
        return gson.fromJson(stationsString, Sensor[].class);
    }

    /**
     * Parses given String, the internal content from: http://api.gios.gov.pl/pjp-api/rest/data/getData/"sensorID"
     * where in place of "sensorID" there can be any id of sensor that exists in the system
     *
     * @param stationsString content from, for instance: http://api.gios.gov.pl/pjp-api/rest/data/getData/3066
     * @return SensorData object parsed from given String
     */
    public SensorData createSensorData(String stationsString) {
        Gson gson = new Gson();
        return gson.fromJson(stationsString, SensorData.class);
    }

    /**
     * Parses given String, the internal content from: http://api.gios.gov.pl/pjp-api/rest/aqindex/getIndex/"stationID"
     * where in place of "stationID" there can be any id of station that exists in the system
     *
     * @param stationsString content from, for instance http://api.gios.gov.pl/pjp-api/rest/aqindex/getIndex/365
     * @return AirIndex object parsed from given String
     */
    public AirIndex createAirIndex(String stationsString) {
        Gson gson = new Gson();
        return gson.fromJson(stationsString, AirIndex.class);

    }
}

package AirPollution.api;

import java.io.IOException;

/**
 * Class used to fetch desired content from given URL Addresses that are provided by the API: http://powietrze.gios.gov.pl/pjp/content/api
 */
public class JsonFetcher {
    /**
     * Returns content from URL Address: http://api.gios.gov.pl/pjp-api/rest/station/findAll
     *
     * @return content from: http://api.gios.gov.pl/pjp-api/rest/station/findAll, as a String
     * @throws IOException exception
     * @see AirPollution.model.Station
     */
    public String getAllStations() throws IOException {
        return URLReader.getJSON("http://api.gios.gov.pl/pjp-api/rest/station/findAll");
    }

    /**
     * Returns content from URL Address: http://api.gios.gov.pl/pjp-api/rest/station/sensors/ + "stationID of desired station"
     *
     * @param stationID station id of desired station
     * @return content from URL Address: http://api.gios.gov.pl/pjp-api/rest/station/sensors/"stationID",
     * for instance http://api.gios.gov.pl/pjp-api/rest/station/sensors/444,
     * as a String
     * @throws IOException exception
     * @see AirPollution.model.Sensor
     */
    public String getSensors(int stationID) throws IOException {
        return URLReader.getJSON("http://api.gios.gov.pl/pjp-api/rest/station/sensors/" + stationID);
    }

    /**
     * Returns content from URL Address: http://api.gios.gov.pl/pjp-api/rest/data/getData/ + "sensorID of desired sensor"
     *
     * @param sensorID sensor id of desired sensor
     * @return content from URL Address: http://api.gios.gov.pl/pjp-api/rest/data/getData/"sensorID"
     * for instance http://api.gios.gov.pl/pjp-api/rest/data/getData/3068,
     * as a String
     * @throws IOException exception
     * @see AirPollution.model.SensorData
     */
    public String getSensorData(int sensorID) throws IOException {
        return URLReader.getJSON("http://api.gios.gov.pl/pjp-api/rest/data/getData/" + sensorID);
    }

    /**
     * Returns content from URL Address: http://api.gios.gov.pl/pjp-api/rest/aqindex/getIndex/ + "stationID for desired Air Quality Index"
     *
     * @param stationID station id of desired station
     * @return content from URL Address: http://api.gios.gov.pl/pjp-api/rest/aqindex/getIndex/"stationID"
     * for instance http://api.gios.gov.pl/pjp-api/rest/aqindex/getIndex/555,
     * as a String
     * @throws IOException exception
     * @see AirPollution.model.AirIndex
     */
    public String getQualityIndex(int stationID) throws IOException {
        return URLReader.getJSON("http://api.gios.gov.pl/pjp-api/rest/aqindex/getIndex/" + stationID);
    }
}

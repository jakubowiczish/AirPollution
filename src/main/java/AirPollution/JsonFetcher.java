package AirPollution;

import java.io.IOException;

public class JsonFetcher {
    public String getAllStations() throws IOException {
        return URLReader.getJSON("http://api.gios.gov.pl/pjp-api/rest/station/findAll");
    }

    public String getAllSensors(int stationID) throws IOException {
        return URLReader.getJSON("http://api.gios.gov.pl/pjp-api/rest/station/sensors/" + stationID);
    }

    public String getSensorData(int sensorID) throws IOException {
        return URLReader.getJSON("http://api.gios.gov.pl/pjp-api/rest/data/getData/" + sensorID);
    }

    public String getQualityIndex(int stationID) throws IOException {
        return URLReader.getJSON("http://api.gios.gov.pl/pjp-api/rest/aqindex/getIndex/" + stationID);
    }
}

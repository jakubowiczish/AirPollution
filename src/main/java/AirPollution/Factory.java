package AirPollution;

import com.google.gson.Gson;

public class Factory {

    public DataStation[] createStations(String stationsString) {
        Gson gson = new Gson();
        return gson.fromJson(stationsString, DataStation[].class);
    }

    public JsonAirIndex createIndices(String stationsString) {
        Gson gson = new Gson();
        return gson.fromJson(stationsString, JsonAirIndex.class);
    }


}

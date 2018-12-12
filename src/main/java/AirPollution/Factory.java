package AirPollution;

import com.google.gson.Gson;

public class Factory {
    private Gson gson;

    public Factory() {
        Gson gson = new Gson();
    }

    public DataStation[] createStations(String stationsString) {
        return gson.fromJson(stationsString, DataStation[].class);
    }



}

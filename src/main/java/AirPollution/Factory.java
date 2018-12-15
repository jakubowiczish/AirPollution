package AirPollution;

import com.google.gson.Gson;

public class Factory {

    public Station[] createStations(String stationsString) {
        Gson gson = new Gson();
        return gson.fromJson(stationsString, Station[].class);
    }




    public AirIndex createIndex(String stationsString) {
        Gson gson = new Gson();
        return gson.fromJson(stationsString, AirIndex.class);

    }



}

package AirPollution.model;

import AirPollution.utils.Utils;

public class StationFluctuation {

    private final Double difference;
    private final Station station;

    public StationFluctuation(Station station, Double difference) {
        this.station = station;
        this.difference = difference;
    }

    public Double getDifference() {
        return difference;
    }

    @Override
    public String toString() {
        return Utils.decimalFormat.format(difference) + "\n" + "This difference occurs for station:\n" + station.getStationName();
    }
}

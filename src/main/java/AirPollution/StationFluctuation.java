package AirPollution;

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
        return "StationFluctuation{" +
                "difference=" + difference +
                ", station=" + station +
                '}';
    }
}

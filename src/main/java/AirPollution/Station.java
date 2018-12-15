package AirPollution;

public class Station {
    int id;
    String stationName;
    double gegrLat;
    double gegrLon;
    City city;
    String addressStreet;

    class City {
        int id;
        String name;
        Commune commune;

        class Commune {
            String communeName;
            String districtName;
            String provinceName;
        }
    }

    public static int returnIdOfGivenStation(Station[] stations, String stationName) {
        int result = -1;
        int stationCounter = 0;
        for (Station station : stations) {
            if (station.stationName.equals(stationName)) {
                if (stationCounter == 0) {
                    result = station.id;
                }
                stationCounter++;
                if (stationCounter > 1) {
                    System.out.println("There is more than one station that name is: " + stationName + "\n");
                }
            }
        }
        if (stationCounter == 0) {
            throw new IllegalArgumentException("Given station does not exist");
        }
        return result;
    }

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", stationName='" + stationName + '\'' +
                ", gegrLat=" + gegrLat +
                ", gegrLon=" + gegrLon +
                ", city=" + city +
                ", addressStreet='" + addressStreet + '\'' +
                '}';
    }
}

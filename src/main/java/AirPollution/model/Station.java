package AirPollution.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Class used to store parsed information about station
 */
@Getter
@Setter
@NoArgsConstructor
public class Station {

    private int id;
    private String stationName;
    private double gegrLat;
    private double gegrLon;
    private City city;
    private String addressStreet;

    public Station(int id, String stationName) {
        this.id = id;
        this.stationName = stationName;
    }

    public static int returnIdOfGivenStation(List<Station> stations, String stationName) {
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

    @Getter
    @Setter
    static class City {

        private int id;
        private String name;
        private Commune commune;

        @Override
        public String toString() {
            return "           id:" + id + "\n" +
                    "           name:'" + name + '\'' + "\n" +
                    "           commune: {" + "\n" + commune + "\n" +
                    "       }";
        }

        @Getter
        @Setter
        static class Commune {

            private String communeName;
            private String districtName;
            private String provinceName;

            @Override
            public String toString() {
                return "               communeName:'" + communeName + '\'' + "\n" +
                        "               districtName:'" + districtName + '\'' + "\n" +
                        "               provinceName:'" + provinceName + '\'' + "\n" +
                        "           }";
            }
        }
    }

    @Override
    public String toString() {
        return "Station {" + "\n" +
                "       id:" + id + "\n" +
                "       stationName:'" + stationName + '\'' + "\n" +
                "       gegrLat:" + gegrLat + "\n" +
                "       gegrLon:" + gegrLon + "\n" +
                "       city: {" + "\n" + city + "\n" +
                "       addressStreet:'" + addressStreet + '\'' + "\n" +
                '}';
    }
}

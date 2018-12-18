package AirPollution;

import java.util.ArrayList;

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

        @Override
        public String toString() {
            return  "           id:" + id + "\n" +
                    "           name:'" + name + '\'' + "\n" +
                    "           commune: {" + "\n" + commune + "\n" +
                    "       }";
        }

        class Commune {
            String communeName;
            String districtName;
            String provinceName;

            @Override
            public String toString() {
                return  "               communeName:'" + communeName + '\'' + "\n" +
                        "               districtName:'" + districtName + '\'' + "\n" +
                        "               provinceName:'"  + provinceName + '\'' + "\n" +
                        "           }";
            }
        }
    }

    public static int returnIdOfGivenStation(ArrayList<Station> stations, String stationName) {
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

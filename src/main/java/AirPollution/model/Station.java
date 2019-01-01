package AirPollution.model;

import java.util.ArrayList;

public class Station {
    private int id;
    private String stationName;
    private double gegrLat;
    private double gegrLon;
    private City city;
    private String addressStreet;

    class City {
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

        class Commune {
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

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getGegrLat() {
        return gegrLat;
    }

    public void setGegrLat(double gegrLat) {
        this.gegrLat = gegrLat;
    }

    public double getGegrLon() {
        return gegrLon;
    }

    public void setGegrLon(double gegrLon) {
        this.gegrLon = gegrLon;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }
}

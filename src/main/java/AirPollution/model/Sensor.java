package AirPollution.model;

public class Sensor {
    private int id;
    private int stationId;
    public Param param;

    @Override
    public String toString() {
        return "Sensor{" +
                "id:" + id + " " +
                "stationId:" + stationId + " " +
                "param:" + param +
                "}";
    }

    public class Param {
        String paramName;
        public String paramFormula;
        String paramCode;
        int idParam;

        @Override
        public String toString() {
            return "{" +
                    "paramName:" + paramName + " " +
                    "paramFormula:" + paramFormula + " " +
                    "paramCode:" + paramCode + " " +
                    "idParam:" + idParam +
                    "}";
        }
    }

    public int getId() {
        return id;
    }
}

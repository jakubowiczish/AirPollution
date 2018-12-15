package AirPollution;

public class Sensor {
    int id;
    int stationId;
    Param param;

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", stationId=" + stationId +
                ", param=" + param +
                '}';
    }

    class Param {
        String paramName;
        String paramFormula;
        String paramCode;
        int idParam;

        @Override
        public String toString() {
            return "Param{" +
                    "paramName='" + paramName + '\'' +
                    ", paramFormula='" + paramFormula + '\'' +
                    ", paramCode='" + paramCode + '\'' +
                    ", idParam=" + idParam +
                    '}';
        }
    }
}

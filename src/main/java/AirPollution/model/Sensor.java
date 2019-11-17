package AirPollution.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class used to store parsed information about sensor
 */
@Getter
@Setter
@NoArgsConstructor
public class Sensor {

    private int id;
    private int stationId;
    private Param param;

    public Sensor(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id:" + id + " " +
                "stationId:" + stationId + " " +
                "param:" + param +
                "}";
    }

    @Getter
    @Setter
    public static class Param {

        String paramName;
        String paramFormula;
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
}

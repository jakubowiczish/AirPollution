package AirPollution.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

/**
 * Class used to store parsed sensor data
 */
@Getter
@Setter
@NoArgsConstructor
public class SensorData {

    private String key;
    private Value[] values;

    public SensorData(String key, Value[] values) {
        this.key = key;
        this.values = values;
    }

    public static class Value {
        public Value(String date, Double value) {
            this.date = date;
            this.value = value;
        }

        public String date;
        public Double value;

        @Override
        public String toString() {
            return "Value{" +
                    "date='" + date + '\'' +
                    ", value=" + value +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "key='" + key + '\'' +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}

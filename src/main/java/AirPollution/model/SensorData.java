package AirPollution.model;

import java.util.Arrays;

public class SensorData {
    public String key;
    public Value[] values;

    public class Value {
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

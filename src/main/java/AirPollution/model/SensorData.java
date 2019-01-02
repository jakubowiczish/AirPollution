package AirPollution.model;

import java.util.Arrays;

/**
 * Class used to store parsed sensor data
 */
public class SensorData {
    private  String key;
    private  Value[] values;

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

    public String getKey() {
        return key;
    }

    public Value[] getValues() {
        return values;
    }
}

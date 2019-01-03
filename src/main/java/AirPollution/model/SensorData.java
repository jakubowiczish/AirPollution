package AirPollution.model;

import java.util.Arrays;

/**
 * Class used to store parsed sensor data
 */
public class SensorData {
    private String key;
    private Value[] values;

    public SensorData() {}

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

    public String getKey() {
        return key;
    }

    public Value[] getValues() {
        return values;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValues(Value[] values) {
        this.values = values;
    }
}

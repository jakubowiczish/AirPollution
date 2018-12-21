package AirPollution;

public class Container<T> {

    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}

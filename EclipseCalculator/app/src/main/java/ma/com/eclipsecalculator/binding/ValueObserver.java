package ma.com.eclipsecalculator.binding;

public interface ValueObserver<V> {
    void update(V value);
}

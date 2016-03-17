package ma.com.eclipsecalculator.binding;

public class Int extends BaseObservable <ValueObserver<Integer>, Integer>{
    public Int(int value) {
        set(value);
    }
}

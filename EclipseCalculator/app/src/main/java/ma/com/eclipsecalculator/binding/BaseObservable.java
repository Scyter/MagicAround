package ma.com.eclipsecalculator.binding;

public abstract class BaseObservable<W extends ValueObserver<V>, V> {
    private W widget;
    private V value;

    public V get() {
        return value;
    }

    public void set(V value) {
        this.value = value;

        updateView();
    }

    public void setView(W widget) {
        this.widget = widget;
    }

    public void setValueOnly(V value) {
        this.value = value;
    }

    private void updateView() {
        if(widget == null) {
            return;
        }

        widget.update(value);
    }

}

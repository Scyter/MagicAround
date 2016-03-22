package ma.com.eclipsecalculator.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import ma.com.eclipsecalculator.R;
import ma.com.eclipsecalculator.binding.Int;
import ma.com.eclipsecalculator.binding.ValueObserver;

public class UpgradePicker extends FrameLayout implements ValueObserver<Integer>, View.OnClickListener {

    protected View parent, viewUp, viewDown;
    protected TextView textValue;
    protected ImageView image;

    private int value;
    private int maxValue = 100;
    private int minValue = 0;

    public UpgradePicker(Context context) {
        super(context);
        init(null);
    }

    public UpgradePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public UpgradePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public UpgradePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    @Override
    public void update(Integer value) {
        changeValue(value);
    }

    public void bindData(Int observable) {
        observable.setView(this);
        changeValue(observable.get());
    }

    protected int getLayout() {
        return R.layout.upgrade_picker;
    }

    protected View findViewUp() {
        return findViewById(R.id.view_up);
    }

    protected View findViewDown() {
        return findViewById(R.id.view_down);
    }

    protected TextView findValueView() {
        return (TextView) findViewById(R.id.view_down);
    }

    private void init(AttributeSet attrs) {
        Context context = getContext();
        inflate(context, getLayout(), this);
        parent = findViewById(R.id.upgrade_picker_parent);

        image = (ImageView) findViewById(R.id.upgrade_image);
        viewUp = findViewUp();
        viewUp.setOnClickListener(this);
        viewDown = findViewDown();
        viewDown.setOnClickListener(this);
        textValue = findValueView();

        initAttributes(attrs);
    }


    private void initAttributes(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        Context context = getContext();
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.Picker);

        Drawable background = attributes.getDrawable(R.styleable.Picker_background_color);
        if (background != null) {
            parent.setBackground(background);
        }

        Drawable upgrade = attributes.getDrawable(R.styleable.Picker_upgrade);
        if (upgrade != null) {
            image.setBackground(upgrade);
        }

        boolean reverse = attributes.getBoolean(R.styleable.Picker_reverse_text_color, false);
        if (viewDown instanceof TextView) {
            ((TextView) viewDown).setTextColor(getContext().getResources().getColor(reverse ? R.color.white : R.color.black));
        }

        maxValue = attributes.getInt(R.styleable.Picker_max_value, 100);
        minValue = attributes.getInt(R.styleable.Picker_min_value, 0);

        attributes.recycle();
    }

    private void changeValue(int value) {
        if (value < minValue) {
            this.value = minValue;
        } else if (value > maxValue) {
            this.value = maxValue;
        } else {
            this.value = value;
        }
        updateView();
    }

    private void updateView() {
        if (textValue != null) {
            textValue.setText(String.valueOf(value));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_up:
                changeValue(value + 1);
                break;
            case R.id.view_down:
                changeValue(value - 1);
                break;
            default:
                break;
        }
    }
}

package ma.com.eclipsecalculator;

import android.app.Application;

import ma.com.eclipsecalculator.utilsss.RandomUtils;

public class CalculatorApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RandomUtils.init(CalculatorApplication.this);
    }
}

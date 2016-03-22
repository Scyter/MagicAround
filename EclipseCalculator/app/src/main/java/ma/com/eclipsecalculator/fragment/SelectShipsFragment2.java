package ma.com.eclipsecalculator.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ma.com.eclipsecalculator.R;

public class SelectShipsFragment2 extends Fragment {
    ViewGroup main;
    LayoutInflater inflater;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        this.inflater = inflater;
         main = (ViewGroup) inflater.inflate(R.layout.main_layout, container, false);

        return main;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View text = main.findViewById(R.id.results);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ccc", "inflate ship");
                long start = System.currentTimeMillis();
                View tmp = inflater.inflate(R.layout.ship_layout, main, false);
                main.addView(tmp);

                Log.d("ccc", System.currentTimeMillis() - start + "");
            }
        });
    }
}

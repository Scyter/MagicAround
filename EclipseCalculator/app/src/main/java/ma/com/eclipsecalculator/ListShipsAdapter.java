package ma.com.eclipsecalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import ma.com.eclipsecalculator.model.Ship;

public class ListShipsAdapter extends ArrayAdapter<Ship> {

    protected List<Ship> ships;

    public ListShipsAdapter(Context context, int resource, List<Ship> objects) {
        super(context, resource, objects);
        ships = new ArrayList<>(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ship, parent, false);
        }

        return convertView;
    }
}

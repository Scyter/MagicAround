package ma.com.eclipsecalculator.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import ma.com.eclipsecalculator.R;
import ma.com.eclipsecalculator.model.Ship;
import ma.com.eclipsecalculator.widget.ShipViewer;

public class ShipsAdapter extends ArrayAdapter<Ship> {

    private LayoutInflater inflater;

    public ShipsAdapter(Context context, int resource, List<Ship> objects) {
        super(context, resource, objects);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ShipsAdapter(Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("bbb", "getView " + position);
        long start = System.currentTimeMillis();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ship_item, parent, false);
        }
        ShipViewer viewer = (ShipViewer) convertView.findViewById(R.id.ship);
        final Ship ship = getItem(position);
        viewer.bind(ship, new ShipViewer.RemoveListener() {
            @Override
            public void removeShip() {
                remove(ship);
            }
        });
        Log.d("bbb", (System.currentTimeMillis() - start) + "");
        return convertView;
    }

    public List<Ship> getShips() {
        ArrayList<Ship> ships = new ArrayList<>();
        for(int i=0 ; i<getCount() ; i++){
            Ship obj = getItem(i);
            ships.add(obj);
        }
        return ships;
    }
}

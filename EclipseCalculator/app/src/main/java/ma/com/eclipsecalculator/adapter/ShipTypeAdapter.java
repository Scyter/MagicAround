package ma.com.eclipsecalculator.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ma.com.eclipsecalculator.R;
import ma.com.eclipsecalculator.model.ShipType;

public class ShipTypeAdapter extends RecyclerView.Adapter<ShipTypeViewHolder> {

    private List<ShipType> listShipType;

    private ShipTypeAdapterListener listener;

    public ShipTypeAdapter(List<ShipType> list, ShipTypeAdapterListener listener) {
        this.listShipType = list;
        this.listener = listener;
    }

    public ShipTypeAdapter(ShipTypeAdapterListener listener) {
        this.listShipType = new ArrayList<>();
        listShipType.add(new ShipType(ShipType.INTERCEPTOR));
        listShipType.add(new ShipType(ShipType.CRUISER));
        listShipType.add(new ShipType(ShipType.DREADNOUGHT));
        listShipType.add(new ShipType(ShipType.STAR_BASE));
        listShipType.add(new ShipType(ShipType.ORBITAL));
        listShipType.add(new ShipType(ShipType.DEATH_MOON));
        listShipType.add(new ShipType(ShipType.ANCIENT));
        listShipType.add(new ShipType(ShipType.CENTER));
        listShipType.add(new ShipType(ShipType.ANOMALY));
        this.listener = listener;
    }

    @Override
    public ShipTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.ship_type_item, parent, false);
        return new ShipTypeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ShipTypeViewHolder holder, final int position) {
        final ShipType type = listShipType.get(position);
        holder.imageShipType.setImageResource(type.getDrawableId());
        holder.textShipType.setText(type.toString());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onShipTypeSelected(type);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listShipType.size();
    }
}
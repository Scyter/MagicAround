package ma.com.eclipsecalculator.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ma.com.eclipsecalculator.R;

public class ShipTypeViewHolder extends RecyclerView.ViewHolder {

    protected ImageView imageShipType;
    protected TextView textShipType;
    protected View layout;

    public ShipTypeViewHolder(View itemView) {
        super(itemView);
        imageShipType = (ImageView) itemView.findViewById(R.id.image_ship_type);
        textShipType = (TextView) itemView.findViewById(R.id.text_ship_type);
        layout = itemView.findViewById(R.id.layout_ship_type_item);
    }
}

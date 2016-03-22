package ma.com.eclipsecalculator.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ma.com.eclipsecalculator.R;
import ma.com.eclipsecalculator.adapter.ShipTypeAdapter;
import ma.com.eclipsecalculator.adapter.ShipTypeAdapterListener;
import ma.com.eclipsecalculator.constants.Fields;
import ma.com.eclipsecalculator.model.ShipType;

public class SelectShipTypeDialog extends DialogFragment implements ShipTypeAdapterListener {

    public static final String TAG = SelectShipTypeDialog.class.getSimpleName();

    private RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(R.string.select_type);
        View v = inflater.inflate(R.layout.select_ship_type_dialog, null);
        recyclerView = (RecyclerView) v.findViewById(R.id.list_ship_type);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        ShipTypeAdapter adapter = new ShipTypeAdapter(this);
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onShipTypeSelected(ShipType type) {
        Intent intent = new Intent();
        intent.putExtra(Fields.SHIP_TYPE, type.getValue());
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        this.dismiss();
    }
}

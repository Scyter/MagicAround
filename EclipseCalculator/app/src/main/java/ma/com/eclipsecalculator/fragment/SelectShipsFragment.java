package ma.com.eclipsecalculator.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Map;

import ma.com.eclipsecalculator.R;
import ma.com.eclipsecalculator.adapter.ShipsAdapter;
import ma.com.eclipsecalculator.constants.Defaults;
import ma.com.eclipsecalculator.constants.Fields;
import ma.com.eclipsecalculator.model.Battle;
import ma.com.eclipsecalculator.model.BattleResult;
import ma.com.eclipsecalculator.model.Ship;
import ma.com.eclipsecalculator.utilsss.L;
import ma.com.eclipsecalculator.utilsss.RandomUtils;


public class SelectShipsFragment extends Fragment {

    private static final int SELECT_ATTACKER = 100;
    private static final int SELECT_DEFENDER = 101;

    private ListView listShips;
    private ShipsAdapter adapter;
    private Button buttonAttacker, buttonDefender, buttonCalculate;
    private Battle battle;
    private TextView textResults;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View main = inflater.inflate(R.layout.main_layout, container, false);

        listShips = (ListView) main.findViewById(R.id.list_ships);
        buttonAttacker = (Button) main.findViewById(R.id.button_add_attacker);
        buttonDefender = (Button) main.findViewById(R.id.button_add_defender);
        buttonCalculate = (Button) main.findViewById(R.id.button_calculate);
        textResults = (TextView) main.findViewById(R.id.results);

        battle = new Battle();

        return main;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new ShipsAdapter(getActivity(), R.layout.ship_item);
        listShips.setAdapter(adapter);
        buttonAttacker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectShipTypeDialog(true);
            }
        });

        buttonDefender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectShipTypeDialog(false);
            }
        });

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                battle.clearShips();
                for (Ship ship : adapter.getShips()) {
                    battle.addShip(ship);
                }
                battle.calculate();

                Map<BattleResult, Integer> results = battle.getResults();
                StringBuilder resultText = new StringBuilder();
                for (Map.Entry<BattleResult, Integer> entry : results.entrySet()) {
                    BattleResult battleResult = entry.getKey();
                    Integer count = entry.getValue();
                    resultText.append(battleResult.getResult()).append(" : ").append(count).append("\n");
                }
                textResults.setText(resultText.toString());
                L.a(RandomUtils.rollsString());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            int shipType = data.getIntExtra(Fields.SHIP_TYPE, Defaults.UNKNOWN_POSITIVE_INT);
            if (shipType == Defaults.UNKNOWN_POSITIVE_INT) {
                return;
            }
            Ship addingShip = null;
            if (requestCode == SELECT_ATTACKER) {
                addingShip = Ship.getShip(true, shipType);
            } else if (requestCode == SELECT_DEFENDER) {
                addingShip = Ship.getShip(false, shipType);
            }
            if (addingShip == null) {
                return;
            }
            adapter.add(addingShip);
        }
    }

    private void showSelectShipTypeDialog(boolean isAttacker) {
        SelectShipTypeDialog dialog = new SelectShipTypeDialog();
        dialog.setTargetFragment(SelectShipsFragment.this, isAttacker ? SELECT_ATTACKER : SELECT_DEFENDER);
        dialog.show(getFragmentManager(), SelectShipTypeDialog.TAG);
    }


}

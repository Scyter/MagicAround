package ma.com.eclipsecalculator.fragment;

import android.app.Fragment;
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
import ma.com.eclipsecalculator.model.Battle;
import ma.com.eclipsecalculator.model.BattleResult;
import ma.com.eclipsecalculator.model.Ship;
import ma.com.eclipsecalculator.model.ShipType;
import ma.com.eclipsecalculator.utilsss.L;
import ma.com.eclipsecalculator.utilsss.RandomUtils;


public class SelectShipsFragment extends Fragment {

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
                Ship addingShip = new Ship(true, ShipType.INTERCEPTOR, 1, 2, 1, 0, 0,
                        1, 0, 0, 0, 0,
                        0, 0, 0, 0,
                        0);

                adapter.add(addingShip);
            }
        });

        buttonDefender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ship addingShip = new Ship(false, ShipType.ANCIENT, 1, 2, 1, 0, 1,
                        2, 0, 0, 0, 0,
                        0, 0, 0, 0,
                        0);
                adapter.add(addingShip);
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
                for (Map.Entry<BattleResult,Integer> entry : results.entrySet()) {
                    BattleResult battleResult = entry.getKey();
                    Integer count = entry.getValue();
                    resultText.append(battleResult.getResult()).append(" : ").append(count / 10).append("\n");
                }
                textResults.setText(resultText.toString());
                L.a(RandomUtils.rollsString());
            }
        });
    }
}

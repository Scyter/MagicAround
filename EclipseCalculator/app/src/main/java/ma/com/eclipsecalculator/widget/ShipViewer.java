package ma.com.eclipsecalculator.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import ma.com.eclipsecalculator.R;
import ma.com.eclipsecalculator.model.Ship;

public class ShipViewer extends LinearLayout {

    private Ship ship;
    private View parent;
    private UpgradePicker count, initiative, computer, shield, hull, ion, plasma, soliton,
            antiMaterial, rift, ionMissile, plasmaMissile, solitonMissile, antiMaterialMissile, regeneration;

    public ShipViewer(Context context) {
        super(context);
        init(null);
    }

    public ShipViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ShipViewer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ShipViewer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    protected int getLayout() {
        return R.layout.ship;
    }

    private void init(AttributeSet attrs) {
        parent = inflate(getContext(), getLayout(), this);

        count = (UpgradePicker) findViewById(R.id.count);
        initiative = (UpgradePicker) findViewById(R.id.initiative);
        computer = (UpgradePicker) findViewById(R.id.computer);
        shield = (UpgradePicker) findViewById(R.id.shield);
        hull = (UpgradePicker) findViewById(R.id.hull);
        ion = (UpgradePicker) findViewById(R.id.ion);
        plasma = (UpgradePicker) findViewById(R.id.plasma);
        soliton = (UpgradePicker) findViewById(R.id.soliton);
        antiMaterial = (UpgradePicker) findViewById(R.id.anti_material);
        rift = (UpgradePicker) findViewById(R.id.rift);
        ionMissile = (UpgradePicker) findViewById(R.id.ion_missile);
        plasmaMissile = (UpgradePicker) findViewById(R.id.plasma_missile);
        solitonMissile = (UpgradePicker) findViewById(R.id.soliton_missile);
        antiMaterialMissile = (UpgradePicker) findViewById(R.id.anti_material_missile);
        regeneration = (UpgradePicker) findViewById(R.id.regeneration);

        bind();
    }

    private void bind() {
        count.bindData(ship.getCount());
    }
}

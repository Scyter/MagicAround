package ma.com.runtracker;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RunMapFragment extends SupportMapFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String ARG_RUN_ID = "RUN_ID";
    private GoogleMap mGoogleMap;
    private static final int LOAD_LOCATIONS = 0;
    private RunDatabaseHelper.LocationCursor mLocationCursor;

    public static RunMapFragment newInstance(long runId) {
        Bundle args = new Bundle();
        args.putLong(ARG_RUN_ID, runId);
        RunMapFragment rf = new RunMapFragment();
        rf.setArguments(args);
        return rf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        mGoogleMap = getMap();
        mGoogleMap.setMyLocationEnabled(true);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// Проверка идентификатора серии в аргументе и поиск серии
        Bundle args = getArguments();
        if (args != null) {
            long runId = args.getLong(ARG_RUN_ID, -1);
            if (runId != -1) {
                LoaderManager lm = getLoaderManager();
                lm.initLoader(LOAD_LOCATIONS, args, this);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        long runId = args.getLong(ARG_RUN_ID, -1);
        return new LocationListCursorLoader(getActivity(), runId);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mLocationCursor = (RunDatabaseHelper.LocationCursor)cursor;
        updateUI();
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
// Завершение работы с данными
        mLocationCursor.close();
        mLocationCursor = null;
    }

    private void updateUI() {
        if (mGoogleMap == null || mLocationCursor == null)
            return;
// Создание накладки с позициями серии.
// Создаем ломаную со всеми точками.
        PolylineOptions line = new PolylineOptions();
// Также создается объект LatLngBounds для масштабирования по размерам.
        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
// Перебор позиций
        mLocationCursor.moveToFirst();
        while (!mLocationCursor.isAfterLast()) {
            Location loc = mLocationCursor.getLocation();
            LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            line.add(latLng);
            latLngBuilder.include(latLng);
            mLocationCursor.moveToNext();
        }
// Добавление маршрута на карту
        mGoogleMap.addPolyline(line);
// Масштабирование карты по маршруту с дополнительными отступами
        // В качестве ограничивающего прямоугольника выбирается
// размер текущего экрана.
        Display display = getActivity().getWindowManager().getDefaultDisplay();
// Построение инструкции перемещения для камеры карты.
        LatLngBounds latLngBounds = latLngBuilder.build();
        CameraUpdate movement = CameraUpdateFactory.newLatLngBounds(latLngBounds,
                display.getWidth(), display.getHeight(), 15);
        mGoogleMap.moveCamera(movement);
    }
}
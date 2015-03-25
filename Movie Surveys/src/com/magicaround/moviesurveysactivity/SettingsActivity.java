package com.magicaround.moviesurveysactivity;

import com.magicaround.moviesurveys.Consts;
import com.magicaround.moviesurveys.R;

import android.app.Activity;

import android.content.SharedPreferences;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ListView;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	String[] languages = { "Русский", "English" };
	public SharedPreferences sPref;
	public boolean settings;
	public ListView lvSettings;
	public Button bLanguage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		lvSettings = (ListView) findViewById(R.id.lvLanguage);
		bLanguage = (Button) findViewById(R.id.bLanguage);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, languages);
		lvSettings.setAdapter(adapter);
		sPref = getSharedPreferences(Consts.SETTINGS, MODE_PRIVATE);
		settings = false;
		if (sPref != null) {
			settings = sPref.getBoolean(Consts.LANGUAGE_SETTED, false);
		}
		if (settings) {
			setLanguage();
		} else {
			shoseLanguage();

		}
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				lvSettings.setVisibility(View.INVISIBLE);
				bLanguage.setVisibility(View.VISIBLE);
				bLanguage.setText(languages[position]);
				Editor ed = sPref.edit();
				ed.putString(Consts.LANGUAGE, languages[position]);
				ed.putInt(Consts.LANGUAGE_NUMBER, position);
				ed.putBoolean(Consts.LANGUAGE_SETTED, true);
				ed.commit();

			}
		};
		lvSettings.setOnItemClickListener(listener);
		// Log.d("aa", getResources().getConfiguration().locale.getLanguage());
	}

	public void setLanguage() {
		String lang = sPref.getString(Consts.LANGUAGE, "");
		lvSettings.setVisibility(View.INVISIBLE);
		bLanguage.setVisibility(View.VISIBLE);
		bLanguage.setText(lang);
	}

	public void shoseLanguage() {
		lvSettings.setVisibility(View.VISIBLE);
		bLanguage.setVisibility(View.INVISIBLE);
	}

	public void onClick(View v) {
		Log.d("aa", "lc");
		Toast.makeText(this, "sadfasdf", Toast.LENGTH_LONG).show();
		// Intent intent = new Intent(this, SurveysActivity.class);
		// startActivity(intent);
		shoseLanguage();
	}
}

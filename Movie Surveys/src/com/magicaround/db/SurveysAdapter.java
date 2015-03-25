package com.magicaround.db;

import java.util.ArrayList;

import com.magicaround.moviesurveys.R;
import com.magicaround.moviesurveys.Survey;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SurveysAdapter extends BaseAdapter {
	private Activity activity;
	// private ArrayList<Map<String, Object>> data;
	private ArrayList<Survey> surveys;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;

	public SurveysAdapter(Activity a, ArrayList<Survey> s) {
		super();
		activity = a;
		// data = d;
		surveys = s;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());

	}

	@Override
	public int getCount() {
		return surveys.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.s_item, null);
		TextView tvText = (TextView) vi.findViewById(R.id.tvS);
		TextView tvInfo = (TextView) vi.findViewById(R.id.tvSI);
		ImageView ivPicture = (ImageView) vi.findViewById(R.id.ivS);
		ImageView ivNew = (ImageView) vi.findViewById(R.id.ivNew);

		// String question = (String) data.get(position).get(
		// DatabaseManager.COLUMN_Q_QUESTION);
		// String info = (String) data.get(position).get(
		// DatabaseManager.COLUMN_Q_INFO);
		// String picture = (String) data.get(position).get(
		// DatabaseManager.COLUMN_Q_PICTURE);

		tvText.setText(surveys.get(position).question);
		tvInfo.setText(surveys.get(position).info);
		imageLoader.DisplayImage(surveys.get(position).picture, ivPicture);
		ivNew.setImageResource(R.drawable.test);
		if (surveys.get(position).state != 0) {
			ivNew.setVisibility(View.INVISIBLE);
		}

		return vi;
	}
}

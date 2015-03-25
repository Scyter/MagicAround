package com.ma.wcc.android;

import java.util.ArrayList;
import com.ma.wcc.Assets;
import com.ma.wcc.FootballTeam;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultsAdapter extends BaseAdapter {
	Activity activity;
	ArrayList<FootballTeam> teams;
	private static LayoutInflater inflater = null;

	public ResultsAdapter(Activity a, ArrayList<FootballTeam> t) {
		super();
		activity = a;
		// data = d;
		teams = t;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return Assets.teams.size();
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
			vi = inflater.inflate(R.layout.result_item, null);
		TextView place = (TextView) vi.findViewById(R.id.tvPlace);
		TextView tv = (TextView) vi.findViewById(R.id.tvTeam);
		ImageView iv = (ImageView) vi.findViewById(R.id.ivTeam);
		ImageView ivContry = (ImageView) vi.findViewById(R.id.ivContry);
		TextView result = (TextView) vi.findViewById(R.id.tvResults);
		String c = "";
		double res = 0;
		try {
			c = teams.get(position).name;
			res = teams.get(position).result;
		} catch (NullPointerException e) {
			c = "";
			res = 0;
		}
		tv.setText(c);

		int pos = position + 1;
		if (res == 0)
			pos = 0;

		String ps = "";
		if (pos < 10)
			ps = "0" + String.valueOf(pos);
		else
			ps = String.valueOf(pos);
		place.setText(String.valueOf(ps));

		String resString = "";

		if (res < 1000) {
			resString = String.valueOf((int) res);
		} else if ((res >= 1000) && (res < 1000000)) {
			String t1 = String.valueOf((int) (res / 1000));
			String t2 = String.valueOf((int) (((int) (res)) % 1000) / 100);
			resString = t1 + "," + t2 + "K";
		} else if ((res >= 1000000) && (res < 1000000000)) {
			resString = String.valueOf((int) (res / 1000000)) + ","
					+ String.valueOf((int) (((int) (res)) % 1000000) / 100000)
					+ "M";
		} else
			resString = String.valueOf((int) (res / 1000000000)) + "MM";
		result.setText(resString);
		try {
			iv.setImageBitmap(Consts.teamsImages.get(teams.get(position).id));

			ivContry.setImageBitmap(Consts.countriesImages.get(teams
					.get(position).countryID));
		} catch (Exception e) {
			// iv.setImageBitmap(Consts.teamsImages.get(0));
			// ivContry.setImageBitmap(Consts.countriesImages.get(0));
		}

		return vi;
	}
}

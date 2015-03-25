package com.magicaround.db;

import java.util.ArrayList;

import com.magicaround.moviesurveys.Answer;
import com.magicaround.moviesurveys.R;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultAdapterGlobal extends ResultAdapter {

	public ResultAdapterGlobal(Activity a, ArrayList<Answer> ans) {
		super(a, ans);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.r_item, null);
		TextView textView = (TextView) vi.findViewById(R.id.tvResItemAnswer);
		TextView infoView = (TextView) vi.findViewById(R.id.tvResItemInfo);
		TextView globTV = (TextView) vi.findViewById(R.id.textDiff);
		TextView posView = (TextView) vi.findViewById(R.id.tvRplace);
		ImageView imageView = (ImageView) vi.findViewById(R.id.ivResItem);
		ImageView globIV = (ImageView) vi.findViewById(R.id.imageDiff);

		// String info = (String) data.get(position).get(
		// DatabaseManager.COLUMN_A_INFO);
		// String text = (String) data.get(position).get(
		// DatabaseManager.COLUMN_A_TEXT);
		// String picture = (String) data.get(position).get(
		// DatabaseManager.COLUMN_A_PICTURE);

		textView.setText(answers.get(position).text);
		infoView.setText(answers.get(position).info);
		imageLoader.DisplayImage(answers.get(position).picture, imageView);

		int pos = position + 1;
		String ps = "";
		if (pos < 10)
			ps = "0" + String.valueOf(pos);
		else
			ps = String.valueOf(pos);
		posView.setText(String.valueOf(ps));

		String glob = "";
		Integer last = answers.get(position).lastPosition;
		// (Integer) data.get(position).get(
		// DatabaseManager.COLUMN_A_LAST_POSITION);
		Integer preLast = answers.get(position).preLastPosition;
		// (Integer) data.get(position).get(
		// DatabaseManager.COLUMN_A_PRE_LAST_POSITION);
		Integer diff = 0;
		diff = last - (position + 1);
		if (preLast != 0)
			diff = preLast - (position + 1);

		if (diff < 0) {
			glob = diff.toString();
			globIV.setImageResource(R.drawable.down_arrow);
		}
		if (diff == 0) {
			glob = "-";
			globIV.setImageResource(R.drawable.ic_launcher);
		}
		if (diff > 0) {
			glob = "+" + diff.toString();
			globIV.setImageResource(R.drawable.up_arrow);
		}
		globTV.setText(glob);

		return vi;
	}
}

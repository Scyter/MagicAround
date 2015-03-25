package com.magicaround.db;

import java.util.ArrayList;

import com.magicaround.moviesurveys.Answer;
import com.magicaround.moviesurveys.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultAdapter extends BaseAdapter {

	protected Activity activity;
	protected ArrayList<Answer> answers;
	protected static LayoutInflater inflater = null;
	protected ImageLoader imageLoader;

	public ResultAdapter(Activity a, ArrayList<Answer> ans) {
		super();
		activity = a;
		answers = ans;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	@Override
	public int getCount() {
		return answers.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		TextView textView;
		TextView infoView;
		TextView posView;
		ImageView imageView;
		ImageView globIV;
		TextView globTV;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;

		if (vi == null) {
			vi = inflater.inflate(R.layout.r_item, null);
			ViewHolder holder = new ViewHolder();
			holder.textView = (TextView) vi.findViewById(R.id.tvResItemAnswer);
			holder.infoView = (TextView) vi.findViewById(R.id.tvResItemInfo);
			holder.posView = (TextView) vi.findViewById(R.id.tvRplace);
			holder.imageView = (ImageView) vi.findViewById(R.id.ivResItem);
			holder.globIV = (ImageView) vi.findViewById(R.id.imageDiff);
			holder.globTV = (TextView) vi.findViewById(R.id.textDiff);
			vi.setTag(holder);

		}
		// if (convertView == null)
		// vi = inflater.inflate(R.layout.r_item, null);
		// TextView textView = (TextView) vi.findViewById(R.id.tvResItemAnswer);
		// TextView infoView = (TextView) vi.findViewById(R.id.tvResItemInfo);
		// TextView posView = (TextView) vi.findViewById(R.id.tvRplace);
		// ImageView imageView = (ImageView) vi.findViewById(R.id.ivResItem);
		// ImageView globIV = (ImageView) vi.findViewById(R.id.imageDiff);
		// TextView globTV = (TextView) vi.findViewById(R.id.textDiff);

		// String infoFromBase = (String) data.get(position).get(
		// DatabaseManager.COLUMN_A_INFO);
		// String textFromBase = (String) data.get(position).get(
		// DatabaseManager.COLUMN_A_TEXT);
		// String pictureFromBase = (String) data.get(position).get(
		// DatabaseManager.COLUMN_A_PICTURE);
		Answer item = answers.get(position);
		if (item != null) {
			ViewHolder holder = (ViewHolder) vi.getTag();

			holder.textView.setText(item.text);
			holder.infoView.setText(item.info);
			imageLoader.DisplayImage(item.picture, holder.imageView);

			int pos = position + 1;
			String ps = "";
			if (pos < 10)
				ps = "0" + String.valueOf(pos);
			else
				ps = String.valueOf(pos);
			holder.posView.setText(String.valueOf(ps));

			String glob = "";
			// Integer diff = (Integer)
			// data.get(position).get(Consts.GLOBAL_DIFF);
			Integer diff = answers.get(position).tmp1;
			if (diff < 0) {
				glob = diff.toString();
				holder.globIV.setImageResource(R.drawable.down_arrow);
			}
			if (diff == 0) {
				glob = diff.toString();
				holder.globIV.setVisibility(View.INVISIBLE);
			}
			if (diff > 0) {
				glob = "+" + diff.toString();
				holder.globIV.setImageResource(R.drawable.up_arrow);
			}
			holder.globTV.setText(glob);
		}

		return vi;
	}
}
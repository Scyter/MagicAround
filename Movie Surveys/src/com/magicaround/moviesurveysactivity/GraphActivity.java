package com.magicaround.moviesurveysactivity;

import java.util.ArrayList;

import com.magicaround.db.DatabaseManager;
import com.magicaround.moviesurveys.Answer;
import com.magicaround.moviesurveys.R;
import com.magicaround.task.GetGraphTask;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class GraphActivity extends Activity {
	DrawSurfaceView dsv;
	DatabaseManager db;
	TextView tvUp;
	TextView tvDown;
	ImageView ivUp;
	ImageView ivDown;

	TextView tvBest;
	TextView tvWorse;
	TextView tvFrom;
	TextView tvSelect;

	int curID;
	boolean local;
	ArrayList<Answer> answers;
	GetGraphTaskUI ggt;
	boolean settedLayout;
	ArrayList<Integer> data;
	ArrayList<Integer> dataNew;
	ProgressBar pb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.graph);
		pb = (ProgressBar) findViewById(R.id.progressBarGraph);
		dsv = (DrawSurfaceView) findViewById(R.id.dsv);
		settedLayout = false;
		curID = getIntent().getIntExtra("id", -1);
		local = getIntent().getBooleanExtra("local", true);
		answers = getIntent().getParcelableArrayListExtra("answers");

		data = new ArrayList<Integer>();
		dataNew = new ArrayList<Integer>();

		tvUp = (TextView) findViewById(R.id.tvUp);
		tvDown = (TextView) findViewById(R.id.tvDown);
		ivUp = (ImageView) findViewById(R.id.ivUp);
		ivDown = (ImageView) findViewById(R.id.ivDown);
		tvBest = (TextView) findViewById(R.id.tvBest);
		tvWorse = (TextView) findViewById(R.id.tvWorse);
		tvFrom = (TextView) findViewById(R.id.tvFrom);
		tvSelect = (TextView) findViewById(R.id.tvSelect);
		db = new DatabaseManager(this);
		DatabaseManager.initializeInstance(DatabaseManager.mDatabaseHelper);
		setData(true);

	}

	private void setLayout() {
		pb.setVisibility(View.INVISIBLE);
		dsv.setVisibility(View.VISIBLE);
		settedLayout = true;
	}

	private void unSetLayout() {
		pb.setVisibility(View.VISIBLE);
		// dsv.setVisibility(View.INVISIBLE);
		settedLayout = false;
	}

	private void setData(boolean get) {
		dataNew = getGraphData();
		if (dataNew.size() >= 2) {
			if (!settedLayout)
				setLayout();
			changeData();
		}
		if (get) {
			ggt = new GetGraphTaskUI(this, answers.get(curID).q_id);
			ggt.execute(answers.get(curID).id);
		}
	}

	private void changeData() {
		if ((dataNew.size() >= 2) && !compateData()) {
			data = new ArrayList<Integer>();
			for (Integer t : dataNew)
				data.add(t);
			dsv.setData(data);
			Integer best = data.get(0);
			for (Integer i : data)
				if (i < best)
					best = i;
			Log.d("aa", best.toString());
			if (tvBest == null) {
				Log.d("aa", "tvBest Null");
			}
			tvBest.setText(best.toString());
			Integer worse = data.get(0);
			for (Integer i : data)
				if (i > worse)
					worse = i;
			tvWorse.setText(worse.toString());
			tvFrom.setText(String.valueOf(answers.size()));
			tvSelect.setText(answers.get(curID).text);
		}
	}

	private boolean compateData() {
		if (data == null)
			return false;
		if (data.size() != dataNew.size())
			return false;
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i) != dataNew.get(i))
				return false;
		}
		return true;
	}

	private ArrayList<Integer> getGraphData() {
		ArrayList<Integer> d = new ArrayList<Integer>();
		db.sqliteDB = DatabaseManager.getInstance().openDatabase();
		Cursor c = db.getGraphsByAnswerID(answers.get(curID).id);
		Log.d("aa", "getGraphData From Base Start");
		if (c.moveToFirst()) {
			do {
				Integer pos = c.getInt(c
						.getColumnIndex(DatabaseManager.COLUMN_G_POSITION));
				d.add(pos);
			} while (c.moveToNext());
		}
		DatabaseManager.getInstance().closeDatabase();
		Log.d("aa", "getGraphData From Base End");
		return d;
	}

	public void onClick(View v) {
		dsv.setDiff(300);
	}

	public void onClickUp(View v) {
		try {
			unSetLayout();
			if (curID <= 1) {
				Toast.makeText(this,
						getResources().getString(R.string.end_pic),
						Toast.LENGTH_SHORT).show();
				return;
			}
			curID--;
			setData(true);

		} catch (Exception e) {
			Toast.makeText(this,
					getResources().getString(R.string.known_error),
					Toast.LENGTH_SHORT).show();
		}

	}

	public void onClickDown(View v) {
		try {
			unSetLayout();
			if (curID >= answers.size()) {
				Toast.makeText(this,
						getResources().getString(R.string.end_pic),
						Toast.LENGTH_SHORT).show();
				return;
			}
			curID++;
			setData(true);
		} catch (Exception e) {
			Toast.makeText(this,
					getResources().getString(R.string.known_error),
					Toast.LENGTH_SHORT).show();
		}
	}

	public class GetGraphTaskUI extends GetGraphTask {

		public GetGraphTaskUI(Context ctx, int id) {
			super(ctx, id);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result)
				setData(false);
		}
	}
}

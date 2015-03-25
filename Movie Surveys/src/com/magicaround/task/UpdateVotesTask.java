package com.magicaround.task;

import com.magicaround.db.DatabaseManager;

import android.content.Context;
import android.os.AsyncTask;

public class UpdateVotesTask extends AsyncTask<Void, Void, Void> {
	DatabaseManager db;
	int userID;

	public UpdateVotesTask(Context ctx, int userID) {
		db = new DatabaseManager(ctx);
		DatabaseManager.initializeInstance(DatabaseManager.mDatabaseHelper);
		this.userID = userID;
	}

	@Override
	protected Void doInBackground(Void... params) {
		// db.open();
		// NB
		// db.close();

		return null;

	}

}

package com.magicaround.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ImageDownloaderTask extends AsyncTask<String, Integer, Boolean> {
	Context cnt;

	public ImageDownloaderTask(Context cntx) {
		cnt = cntx;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		for (String str : params) {
			Log.d("aa", str);
		}
		return null;
	}

}

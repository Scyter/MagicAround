package com.magicaround.task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BackgroundReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		final String LOG_TAG = "aa";
		Log.d(LOG_TAG, "onReceive " + intent.getAction());
		context.startService(new Intent(context, BackgroundService.class));
	}
}

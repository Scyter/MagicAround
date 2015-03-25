package com.magicaround.task;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BackgroundService extends Service {

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("aa", "onStartCommand в сервисе");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}

package com.magicaround.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.magicaround.moviesurveys.Consts;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ImageChekerTask extends AsyncTask<String, Void, Boolean> {
	InputStream inputStream;
	File file;
	File folder;
	Context ctx;
	ImageDownloaderTask idt;

	public ImageChekerTask(Context cntx) {
		super();
		this.ctx = cntx;
		this.folder = new File(ctx.getFilesDir(), "pictures");
		folder.mkdirs();
		idt = new ImageDownloaderTask(cntx);
	}

	@Override
	protected Boolean doInBackground(String... params) {
		for (String str : params) {
			try {
				long fileLength = 0;
				long urlLength = 0;
				File file = new File(folder, str);
				if (file != null)
					fileLength = file.length();
				// Log.d("aa", "fileLength = " + String.valueOf(fileLength));

				if (fileLength > 1000) {// !NB
					return true;
				}

				URL url = new URL(Consts.PICTERES_ADDRESS + str);
				HttpURLConnection urlConnection = (HttpURLConnection) url
						.openConnection();
				urlConnection.getInputStream().available();
				urlConnection.connect();
				urlLength = urlConnection.getContentLength();
				Log.d("aa", "urlLength = " + String.valueOf(urlLength));
				if ((urlLength == fileLength) && (urlLength > 0)) {
					urlConnection.disconnect();
					return true;
				}

				inputStream = urlConnection.getInputStream();
				FileOutputStream fileOutput = new FileOutputStream(file);
				int downloadedSize = 0;
				byte[] buffer = new byte[1024];
				int bufferLength = 0;
				while ((bufferLength = inputStream.read(buffer)) > 0) {
					fileOutput.write(buffer, 0, bufferLength);
					downloadedSize += bufferLength;
				}
				Log.d("aa", "downloaded " + String.valueOf(downloadedSize)
						+ " from " + String.valueOf(urlLength));
				fileOutput.close();
				inputStream.close();
				urlConnection.disconnect();
				// if (downloadedSize != urlLength) //NB
				// return false;
				// else
				return true;

			} catch (IOException e) {
				Log.e("aa", "Error parsing data " + e.toString());
				e.printStackTrace();
			}
			return false;
		}
		return false;
	}
}

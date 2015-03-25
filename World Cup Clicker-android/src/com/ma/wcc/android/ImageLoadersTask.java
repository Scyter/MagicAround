package com.ma.wcc.android;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ma.wcc.Settings;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

public class ImageLoadersTask extends AsyncTask<String, Object, Boolean> {
	private File cacheDir;
	Context c;

	public ImageLoadersTask(Context cnt) {
		c = cnt;
	}

	@Override
	protected void onPreExecute() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					Consts.TITLE);
		else {
			cacheDir = new File(c.getFilesDir(), Consts.TITLE);
			// cacheDir = context.getCacheDir();
		}
		boolean b = cacheDir.exists();
		if (!b)
			cacheDir.mkdirs();
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(String... params) {
		boolean res = false;
		for (String s : params) {
			String sUrl = Consts.PICTERES_ADDRESS + s;
			File f = getFile(sUrl);
			InputStream is;
			OutputStream os;
			// from SD cache
			Bitmap b = Consts.decodeFile(f);
			if (b != null) {
				try {
					long fileLen = f.length();
					Log.d("aa", "Len existed file=" + String.valueOf(fileLen));
					HttpClient httpclient = new DefaultHttpClient();
					String conStr = Consts.SITE_ADDRESS
							+ Consts.GET_IMAGE_SIZE_SQRIPT + "?"
							+ Consts.IMAGE_SIZE_PARAM + "=" + s;
					HttpPost httppost = new HttpPost(conStr);
					Log.d("aa", conStr);
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();

					is = entity.getContent();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is, "utf8"), 8);
					StringBuilder sb = new StringBuilder();
					sb.append(reader.readLine());
					is.close();
					String result = sb.toString();
					long urlLen = Long.parseLong(result);
					Log.d("aa", "UrlLen=" + String.valueOf(urlLen));
					if ((urlLen == fileLen) && (urlLen > 1))
						return true;
				} catch (Throwable th) {
					th.printStackTrace();
				}
			}
			// from web
			try {

				URL imageUrl = new URL(sUrl);
				HttpURLConnection conn = (HttpURLConnection) imageUrl
						.openConnection();
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				conn.setInstanceFollowRedirects(true);
				is = conn.getInputStream();
				os = new FileOutputStream(f);
				Consts.CopyStream(is, os);
				os.close();
				conn.disconnect();
				res = true;
			} catch (Throwable ex) {
				ex.printStackTrace();
				res = false;
			}
		}
		return res;

	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result)
			Settings.needReload = true;
	}

	public File getFile(String url) {
		String filename = String.valueOf(url.hashCode());
		File f = new File(cacheDir, filename);
		return f;
	}

}

package com.magicaround.db;

import java.io.File;
import android.content.Context;

public class FileCache {

	private File cacheDir;

	public FileCache(Context context) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"MovieSurveys");
		else {
			cacheDir = new File(context.getFilesDir(), "pictures");
			// cacheDir = context.getCacheDir();
		}
		boolean b = cacheDir.exists();
		if (!b)
			cacheDir.mkdirs();
	}

	public File getFile(String url) {
		// I identify images by hashcode. Not a perfect solution, good for the
		// demo.
		String filename = String.valueOf(url.hashCode());
		// Another possible solution (thanks to grantland)
		// String filename = URLEncoder.encode(url);
		File f = new File(cacheDir, filename);
		return f;

	}

	public File getFileTMP(String url) {
		return new File(cacheDir, url);
	}

	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
	}

}
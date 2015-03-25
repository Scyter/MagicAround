package com.ma.wcc.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Consts {
	public static final String TITLE = "Magic Around";
	public static final String DB_NAME = "CLICKER";

	public static final String SITE_ADDRESS = "http://scyter.zz.vc/";
	public static final String PICTERES_ADDRESS = "http://scyter.zz.vc/pictures/";
	public static final String USER_SQRIPT = "get_user.php";
	public static final String GET_IMAGE_SIZE_SQRIPT = "get_image_size.php";
	public static final String IMAGE_SIZE_PARAM = "i";

	public static final String SETTINGS = "Settings";
	public static final String USER_ID = "UserID";
	public static final String ADD_DOWNLOAD = "AddDownload";
	public static final String ASK_DOWNLOAD = "AskDownload";
	public static final String LOGINED = "LogIned";
	public static final String FIRST_LOAD = "FirstLoad";

	public static String imName = "wcc001";

	public static final String ADD_CLICKER_RESULT_SCRIPT = "clicker_add_result.php";
	public static final String CALC_CLICKER_RESULT_SCRIPT = "calc_clicker_result.php";
	public static final String PARAM_USERID = "u";
	public static final String PARAM_RESULT = "r";
	public static final String PARAM_GAME = "g";
	public static final String PARAM_LEVEL = "l";
	public static final String PARAM_TIME = "t";
	public static final String PARAM_FAVORITE = "f";
	public static final String PARAM_HATED = "h";
	public static final String PARAM_INFO = "i";
	// public static final String PARAM_USERID = "u";

	public static ArrayList<Bitmap> countriesImages;
	public static ArrayList<Bitmap> teamsImages;

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			int loaded = 0;
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
				loaded += count;
				Log.d("aa", String.valueOf(loaded) + " загружено");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// decodes image and scales it to reduce memory consumption
	public static Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return null;
	}
}

package com.magicaround.lines;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.badlogic.gdx.Gdx;

public class LinesSettings {
	public static boolean musicEnabled = true;
	public static boolean soundEnabled = true;
	public final static String file = "lines.mas";
	public static int highscore;
	public static boolean loggedIn = false;

	public static void loadSettings() {
		BufferedReader in = null;
		try {

			in = new BufferedReader(new InputStreamReader(Gdx.files.local(file)
					.read()));
			musicEnabled = Boolean.parseBoolean(in.readLine());
			soundEnabled = Boolean.parseBoolean(in.readLine());

		} catch (Throwable e) {

		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
			}
		}
	}

	public static void saveSettings() {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(Gdx.files.local(
					file).write(false)));

			out.write(Boolean.toString(musicEnabled) + "\r\n");
			out.write(Boolean.toString(soundEnabled) + "\r\n");

		} catch (Throwable e) {
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
			}
		}
	}

}

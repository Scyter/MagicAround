package com.ma.wcc;

import magicaround.BrainPlayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

	public final static String file = "results.mac";
	public final static String settings = "settings.mac";
	public static int favorite = -1;
	public static int hated = -1;
	public static boolean soundEnabled = true;
	public static boolean musicEnabled = true;
	public static boolean info = false;
	public static boolean loging = false;
	public static boolean needReload = false;

	// public static ArrayList<GameResult> loadResults() {
	// loadSettings();
	// BufferedReader in = null;
	// ArrayList<GameResult> results = new ArrayList<GameResult>();
	// try {
	// in = new BufferedReader(new InputStreamReader(Gdx.files.internal(
	// file).read()));
	// do {
	// GameResult r = new GameResult(1, 0, 0, -1, -1, "");
	// r.game = Integer.parseInt(in.readLine());
	// r.level = Integer.parseInt(in.readLine());
	// r.result = Double.parseDouble(in.readLine());
	// results.add(r);
	// } while (true);
	// } catch (Throwable e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (in != null)
	// in.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// return results;
	// }

	public static void loadSettings() {
		Preferences prefs = Gdx.app.getPreferences("My Preferences");
		favorite = prefs.getInteger("F", -1);
		hated = prefs.getInteger("H", -1);
		info = prefs.getBoolean("I", false);
	}

	public static void saveSettings(BrainPlayer p) {
		Preferences prefs = Gdx.app.getPreferences("My Preferences");
		prefs.putInteger("F", p.favorite);
		prefs.putInteger("H", p.hated);
		prefs.putBoolean("I", info);
		prefs.flush();
	}

	//
	// private static void loadSettingsOld() {
	// BufferedReader in = null;
	// try {
	// in = new BufferedReader(new InputStreamReader(Gdx.files.internal(
	// settings).read()));
	// favorite = Integer.parseInt(in.readLine());
	// hated = Integer.parseInt(in.readLine());
	// } catch (Throwable e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (in != null)
	// in.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	// public static void saveSettingsOld(ClickerPlayer p) {
	// BufferedWriter out = null;
	// try {
	// out = new BufferedWriter(new OutputStreamWriter(Gdx.files.internal(
	// settings).write(false)));
	// out.write(String.valueOf(p.favorite) + "\r\n"
	// + String.valueOf(p.hated) + "\r\n");
	// } catch (Throwable e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (out != null)
	// out.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	// public static void saveResult(GameResult r) {
	// BufferedWriter out = null;
	// try {
	// out = new BufferedWriter(new OutputStreamWriter(Gdx.files.internal(
	// file).write(true)));
	// out.write(String.valueOf(r.game) + "\r\n" + r.level + "\r\n"
	// + r.result + "\r\n");
	// } catch (Throwable e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// if (out != null)
	// out.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
}

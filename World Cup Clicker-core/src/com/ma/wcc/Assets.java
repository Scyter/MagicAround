package com.ma.wcc;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ma.wcc.android.Consts;
import com.ma.wcci.Platform;

public class Assets {

	public static Texture texture;

	public static TextureRegion shirt;
	public static TextureRegion chert;
	public static TextureRegion ball;
	public static TextureRegion background;
	public static TextureRegion backgroundPole;
	public static TextureRegion cells;
	public static TextureRegion playGame;
	public static TextureRegion graph;
	public static ArrayList<TextureRegion> countriesFlags;
	public static ArrayList<TextureRegion> teamsFlags;
	public static ArrayList<TextureRegion> stars;
	public static ArrayList<TextureRegion> progress;
	public static TextureRegion rating;

	public static TextureRegion question;
	public static TextureRegion hand;
	public static TextureRegion cup;
	public static TextureRegion no;
	public static TextureRegion arrow;
	public static TextureRegion cubeRandom;
	public static TextureRegion google;
	public static TextureRegion v;
	public static TextureRegion nullPoint;
	public static TextureRegion axe;
	public static TextureRegion axeEnd;
	public static TextureRegion pause;
	public static TextureRegion mainMenu;

	public static TextureRegion tmpRegion;
	static TextBounds tb;

	static FreeTypeFontGenerator generator;
	public static BitmapFont scoreFont;
	public static BitmapFont totalScoreFont;
	public static BitmapFont infoFont;
	public static BitmapFont graphFont;
	public static BitmapFont timeFont;
	public static BitmapFont smallTimeFont;
	public static BitmapFont otherFont;

	public static Music music;
	public static Sound hit;
	public static Sound miss;
	public static Sound miss2;
	public static Sound lose_ball;
	public static Sound appl;
	public static Sound appl2;

	public static Random r;

	public static ArrayList<FootballTeam> teams;
	public static ArrayList<String> countriesAll;
	public static ArrayList<String> countries;
	// public static ArrayList<String> teamsTMP;
	// public static ArrayList<Float> teamsTBX;
	// public static ArrayList<Float> teamsTBY;

	public static String info1 = "Косайся мячей и получай очки";
	public static float info1X;
	public static float info1Y;
	public static String info1a = "За 50 очков будет открыт следующий уровень";
	public static float info1aX;
	public static float info1aY;
	public static String info1b = "В каждой игре у вас будет 3 попытки";
	public static float info1bX;
	public static float info1bY;
	public static String info2 = "За несколько удачных попаданий подряд";
	public static float info2X;
	public static float info2Y;
	public static String info2x = "вы получите Звезду";
	public static float info2xX;
	public static float info2xY;
	public static String info2a = "Больше Звезд - больше очков";
	public static float info2aX;
	public static float info2aY;
	public static String info3 = "За промах вы потеряете 1 Звезду";
	public static float info3X;
	public static float info3Y;
	public static String info3a = "За упущенный мяч вы потеряете все Звезды";
	public static float info3aX;
	public static float info3aY;

	public static String next_level = "СЛЕДУЮЩИЙ УРОВЕНЬ: ";
	public static float next_levelX;
	public static float next_levelY;
	public static String max_level = "Вы достигли максимального уровня";
	public static float max_levelX;
	public static float max_levelY;
	public static String max_level1 = "Больше уровней в скорых обновлениях";
	public static float max_level1X;
	public static float max_level1Y;
	public static String need50 = "Надо 50 очков";
	public static float need50X;
	public static float need50Y;
	public static String need50_a = "для открытия следующего уровня";
	public static float need50_aX;
	public static float need50_aY;
	public static String cur_level = "ТЕКУЩИЙ УРОВЕНЬ: ";
	public static float cur_levelX;
	public static float cur_levelY;
	public static String game_over = "ИГРА ОКОНЧЕНА";
	public static float game_overX;
	public static float game_overY;
	public static String level = "УРОВЕНЬ: ";
	public static float levelX;
	public static float levelY;
	public static String need_tap = "Прикоснись, чтобы продолжить";
	public static float need_tapX;
	public static float need_tapY;

	public static String favorite = "Любимая";
	public static float favoriteX;
	public static float favoriteY;
	public static String hated = "Нелюбимая";
	public static float hatedX;
	public static float hatedY;
	public static String team = "команда";
	public static float teamX;
	public static float teamY;

	// public static String info1;
	// public static String info1;
	// public static String info1;

	public static void load(Platform p) {
		if ((teams == null) || (teams.size() < 1)) {
			teams = new ArrayList<FootballTeam>();
			teams.add(new FootballTeam(0, "Real Madrid", 0));
			teams.add(new FootballTeam(1, "Atletico Madrid", 0));
			teams.add(new FootballTeam(2, "Barcelona", 0));
			teams.add(new FootballTeam(3, "Manchester City", 1));
			teams.add(new FootballTeam(4, "Liverpool", 1));
			teams.add(new FootballTeam(5, "Chelsea", 1));
			teams.add(new FootballTeam(6, "Bayern Munich", 2));
			teams.add(new FootballTeam(7, "Borussia Dortmund", 2));
			teams.add(new FootballTeam(8, "Schalke 04", 2));
			teams.add(new FootballTeam(9, "Juventus", 3));
			teams.add(new FootballTeam(10, "Roma", 3));
			teams.add(new FootballTeam(11, "Benfica", 4));
			teams.add(new FootballTeam(12, "Sporting CP", 4));
			teams.add(new FootballTeam(13, "Paris Saint-Germain", 5));
			teams.add(new FootballTeam(14, "Monaco", 5));
			teams.add(new FootballTeam(15, "Shakhtar Donetsk", 6));
			teams.add(new FootballTeam(16, "CSKA Moscow", 7));
			teams.add(new FootballTeam(17, "Ajax", 8));
			teams.add(new FootballTeam(18, "Galatasaray", 9));
			teams.add(new FootballTeam(19, "Anderlecht", 10));
			teams.add(new FootballTeam(20, "Olympiacos", 11));
			teams.add(new FootballTeam(21, "Basel", 12));
			teams.add(new FootballTeam(22, "Maribor", 13));
			teams.add(new FootballTeam(23, "Malmо FF", 14));
			teams.add(new FootballTeam(24, "APOEL", 15));
			teams.add(new FootballTeam(25, "Ludogorets Razgrad", 16));
			teams.add(new FootballTeam(26, "BATE Borisov", 17));
			teams.add(new FootballTeam(27, "Arsenal", 1));
			teams.add(new FootballTeam(28, "Zenit Saint Petersburg", 7));
			teams.add(new FootballTeam(29, "Bayer Leverkusen", 2));
			teams.add(new FootballTeam(30, "Porto", 4));
			teams.add(new FootballTeam(31, "Athletic Bilbao", 0));

		}
		countriesAll = new ArrayList<String>();

		countriesAll.add("Albania");
		countriesAll.add("Andorra");
		countriesAll.add("Armenia");
		countriesAll.add("Austria");
		countriesAll.add("Azerbaijan");
		countriesAll.add("Belarus");
		countriesAll.add("Belgium");
		countriesAll.add("Bosnia-Herzegovina");
		countriesAll.add("Bulgaria");
		countriesAll.add("Croatia");
		countriesAll.add("Cyprus");
		countriesAll.add("Czech Republic");
		countriesAll.add("Denmark");
		countriesAll.add("England");
		countriesAll.add("Estonia");
		countriesAll.add("Faroe Islands");
		countriesAll.add("Finland");
		countriesAll.add("France");
		countriesAll.add("FYR Macedonia");
		countriesAll.add("Georgia");
		countriesAll.add("Germany");
		countriesAll.add("Gibraltar");
		countriesAll.add("Greece");
		countriesAll.add("Hungary");
		countriesAll.add("Iceland");
		countriesAll.add("Israel");
		countriesAll.add("Italy");
		countriesAll.add("Kazakhstan");
		countriesAll.add("Latvia");
		countriesAll.add("Liechtenstein");
		countriesAll.add("Lithuania");
		countriesAll.add("Luxembourg");
		countriesAll.add("Malta");
		countriesAll.add("Moldova");
		countriesAll.add("Montenegro");
		countriesAll.add("Netherlands");
		countriesAll.add("Northern Ireland");
		countriesAll.add("Norway");
		countriesAll.add("Poland");
		countriesAll.add("Portugal");
		countriesAll.add("Republic of Ireland");
		countriesAll.add("Romania");
		countriesAll.add("Russia");
		countriesAll.add("San Marino");
		countriesAll.add("Scotland");
		countriesAll.add("Serbia");
		countriesAll.add("Slovakia");
		countriesAll.add("Slovenia");
		countriesAll.add("Spain");
		countriesAll.add("Sweden");
		countriesAll.add("Switzerland");
		countriesAll.add("Turkey");
		countriesAll.add("Ukraine");
		countriesAll.add("Wales");

		countries = new ArrayList<String>();
		countries.add("Spain");
		countries.add("England");
		countries.add("Germany");
		countries.add("Italy");
		countries.add("Portugal");
		countries.add("France");
		countries.add("Ukraine");
		countries.add("Russia");
		countries.add("Netherlands");
		countries.add("Turkey");
		countries.add("Belgium");
		countries.add("Greece");
		countries.add("Switzerland");
		countries.add("Slovenia");
		countries.add("Sweden");
		countries.add("Cyprus");
		countries.add("Bulgaria");
		countries.add("Belarus");

		texture = new Texture("ball.png");
		ball = new TextureRegion(texture, 0, 0, 512, 512);
		texture = new Texture("background.png");
		background = new TextureRegion(texture, 0, 0, 640, 1024);
		texture = new Texture("menu_back.png");
		backgroundPole = new TextureRegion(texture, 0, 0, 350, 512);
		texture = new Texture("cells.png");
		cells = new TextureRegion(texture, 0, 0, 1, 1);

		texture = new Texture("menu.png");
		playGame = new TextureRegion(texture, 0, 0, 512, 100);
		graph = new TextureRegion(texture, 0, 100, 512, 100);// текст Graph
		texture = new Texture("graph.png");
		graph = new TextureRegion(texture, 0, 0, 256, 256);

		texture = new Texture("teams_flags.png");
		tmpRegion = new TextureRegion(texture, 0, 0, 2048, 2048);
		countriesFlags = new ArrayList<TextureRegion>();
		teamsFlags = new ArrayList<TextureRegion>();
		for (int i = 0; i < 32; i++) {
			int line = i / 4;
			int col = i % 4;
			// flags =
			countriesFlags.add(new TextureRegion(texture, 256 * col,
					256 * line, 256, 256));
		}
		for (int i = 0; i < 32; i++) {
			int line = i % 8;
			int col = i / 8;
			// flags =
			teamsFlags.add(new TextureRegion(texture, 1024 + 256 * col,
					256 * line, 256, 256));
		}
		texture = new Texture("rating.png");
		rating = new TextureRegion(texture, 0, 0, 256, 256);

		stars = new ArrayList<TextureRegion>();
		texture = new Texture("star2.png");
		stars.add(new TextureRegion(texture, 0, 0, 256, 256));
		texture = new Texture("star1.png");
		stars.add(new TextureRegion(texture, 0, 0, 256, 256));
		texture = new Texture("star3.png");
		stars.add(new TextureRegion(texture, 0, 0, 256, 256));

		texture = new Texture("question.png");
		question = new TextureRegion(texture, 0, 0, 256, 256);

		texture = new Texture("hand.png");
		hand = new TextureRegion(texture, 0, 0, 256, 256);
		texture = new Texture("s_cup.png");
		cup = new TextureRegion(texture, 0, 0, 256, 256);
		texture = new Texture("no.png");
		no = new TextureRegion(texture, 0, 0, 128, 128);
		texture = new Texture("cube.png");
		cubeRandom = new TextureRegion(texture, 0, 0, 256, 256);
		texture = new Texture("arrow.png");
		arrow = new TextureRegion(texture, 0, 0, 256, 256);
		texture = new Texture("v.png");
		v = new TextureRegion(texture, 0, 0, 256, 256);
		texture = new Texture("google.png");
		google = new TextureRegion(texture, 0, 0, 300, 300);

		texture = new Texture("nullpoint.png");
		nullPoint = new TextureRegion(texture, 0, 0, 64, 64);
		texture = new Texture("axe.png");
		axe = new TextureRegion(texture, 0, 0, 64, 64);
		texture = new Texture("axe_end.png");
		axeEnd = new TextureRegion(texture, 0, 0, 64, 64);
		texture = new Texture("pause.png");
		pause = new TextureRegion(texture, 0, 0, 64, 64);
		texture = new Texture("main_menu.png");
		mainMenu = new TextureRegion(texture, 0, 0, 64, 64);

		progress = new ArrayList<TextureRegion>();
		texture = new Texture("new_progress1.png");
		tmpRegion = new TextureRegion(texture, 0, 0, 256, 256);
		for (int i = 0; i <= 10; i++)
			progress.add(new TextureRegion(texture, 0, 0, 256 / 10 * i, 256));

		r = new Random();

		final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"'<>абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
		generator = new FreeTypeFontGenerator(
				Gdx.files.internal("aassuan_bold.ttf"));
		FreeTypeFontParameter ftfp = new FreeTypeFontParameter();
		int scoreFontSize = 30;
		int totalScoreFontSize = 20;
		int infoFontSize = 28;
		int timeFontSize = 45;
		int smallTimeFontSize = 22;
		int otherFontSize = 18;
		int graphFontSize = 21;

		float fntRatio = (float) Gdx.graphics.getWidth() / 600;
		ftfp.size = (int) (scoreFontSize * fntRatio);
		ftfp.characters = FONT_CHARACTERS;
		scoreFont = generator.generateFont(ftfp);

		generator = new FreeTypeFontGenerator(
				Gdx.files.internal("aassuan_bold.ttf"));
		ftfp.size = (int) (totalScoreFontSize * fntRatio);
		ftfp.characters = FONT_CHARACTERS;
		totalScoreFont = generator.generateFont(ftfp);
		generator = new FreeTypeFontGenerator(Gdx.files.internal("times.ttf"));
		ftfp.size = (int) (infoFontSize * fntRatio);
		ftfp.characters = FONT_CHARACTERS;
		infoFont = generator.generateFont(ftfp);
		ftfp.size = (int) (graphFontSize * fntRatio);
		ftfp.characters = FONT_CHARACTERS;
		graphFont = generator.generateFont(ftfp);
		generator = new FreeTypeFontGenerator(Gdx.files.internal("magic.ttf"));
		ftfp.size = (int) (timeFontSize * fntRatio);
		ftfp.characters = FONT_CHARACTERS;
		timeFont = generator.generateFont(ftfp);
		ftfp.size = (int) (smallTimeFontSize * fntRatio);
		ftfp.characters = FONT_CHARACTERS;
		smallTimeFont = generator.generateFont(ftfp);
		smallTimeFont.setColor(Color.WHITE);
		generator = new FreeTypeFontGenerator(
				Gdx.files.internal("aassuan_bold.ttf"));
		ftfp.characters = FONT_CHARACTERS;
		ftfp.size = (int) (otherFontSize * fntRatio);
		otherFont = generator.generateFont(ftfp);

		changeCountries(true);

		tb = Assets.infoFont.getBounds(Assets.info1);
		info1X = tb.width;
		info1Y = tb.height;
		tb = Assets.infoFont.getBounds(Assets.info1a);
		info1aX = tb.width;
		info1aY = tb.height;
		tb = Assets.infoFont.getBounds(Assets.info1b);
		info1bX = tb.width;
		info1bY = tb.height;
		tb = Assets.infoFont.getBounds(Assets.info2);
		info2X = tb.width;
		info2Y = tb.height;
		tb = Assets.infoFont.getBounds(Assets.info2x);
		info2xX = tb.width;
		info2xY = tb.height;
		tb = Assets.infoFont.getBounds(Assets.info2a);
		info2aX = tb.width;
		info2aY = tb.height;
		tb = Assets.infoFont.getBounds(Assets.info3);
		info3X = tb.width;
		info3Y = tb.height;
		tb = Assets.infoFont.getBounds(Assets.info3a);
		info3aX = tb.width;
		info3aY = tb.height;

		tb = Assets.scoreFont.getBounds(Assets.next_level);
		next_levelX = tb.width;
		next_levelY = tb.height;
		tb = Assets.infoFont.getBounds(Assets.max_level);
		max_levelX = tb.width;
		max_levelY = tb.height;
		tb = Assets.infoFont.getBounds(Assets.max_level1);
		max_level1X = tb.width;
		max_level1Y = tb.height;
		tb = Assets.infoFont.getBounds(Assets.need50);
		need50X = tb.width;
		need50Y = tb.height;
		tb = Assets.infoFont.getBounds(Assets.need50_a);
		need50_aX = tb.width;
		need50_aY = tb.height;
		tb = Assets.scoreFont.getBounds(Assets.cur_level);
		cur_levelX = tb.width;
		cur_levelY = tb.height;
		tb = Assets.scoreFont.getBounds(Assets.game_over);
		game_overX = tb.width;
		game_overY = tb.height;
		tb = Assets.scoreFont.getBounds(Assets.level);
		levelX = tb.width;
		levelY = tb.height;
		tb = Assets.infoFont.getBounds(Assets.favorite);
		favoriteX = tb.width;
		favoriteY = tb.height;
		tb = Assets.infoFont.getBounds(Assets.hated);
		hatedX = tb.width;
		hatedY = tb.height;
		tb = Assets.infoFont.getBounds(Assets.team);
		teamX = tb.width;
		teamY = tb.height;
		tb = Assets.infoFont.getBounds(Assets.need_tap);
		need_tapX = tb.width;
		need_tapY = tb.height;

		// s = new Texture("shirt.png");
		// c = new Texture("chert.png");
		// shirt = new TextureRegion(s, 0, 0, 512, 512);
		// chert = new TextureRegion(c, 0, 0, 512, 512);
		// music = Gdx.audio.newMusic(Gdx.files.internal("music_apl.mp3"));
		// music.setLooping(true);
		// music.setVolume(0.3f);
		// playMusic(music);
		Settings.needReload = false;

		hit = Gdx.audio.newSound(Gdx.files.internal("hit.mp3"));
		miss = Gdx.audio.newSound(Gdx.files.internal("miss.mp3"));
		miss2 = Gdx.audio.newSound(Gdx.files.internal("miss2.mp3"));
		lose_ball = Gdx.audio.newSound(Gdx.files.internal("miss.mp3"));
		appl = Gdx.audio.newSound(Gdx.files.internal("appl.mp3"));
		appl2 = Gdx.audio.newSound(Gdx.files.internal("applause-8.mp3"));

		reloadGraph();
	}

	public static void changeCountries(boolean t) {
		// teamsTMP = new ArrayList<String>();
		// int len;
		// String fs = "";
		// String s = "";
		// for (int i = 0; i < teamsOLD.size(); i++) {
		// s = teamsOLD.get(i);
		// len = s.length();
		// if (len > 14) {
		// fs = s.substring(0, 11);
		// fs += "...";
		// } else
		// fs = s;
		// teamsTMP.add(fs);
		// }
		if (t) {
			for (FootballTeam team : teams) {
				tb = Assets.smallTimeFont.getBounds(team.nameTMP);
				team.setBounds(tb.width, tb.height);
			}
		}
	}

	public static void disposeTextures() {
		texture.dispose();
	}

	public static void close() {
		disposeTextures();

	}

	public static void playSound(Sound sound) {
		if (Settings.soundEnabled)
			sound.play(1);
	}

	public static void playSound(Sound sound, float volume) {
		if (Settings.soundEnabled)
			sound.play(volume);
	}

	public static void playMusic(Music music) {
		if (Settings.musicEnabled) {
			music.play();
		}
	}

	public static void reloadGraph() {
		boolean isExtAvailable = Gdx.files.isExternalStorageAvailable();
		boolean is = Gdx.files.isLocalStorageAvailable();
		if (isExtAvailable) {
			try {
				String url = Consts.PICTERES_ADDRESS + Consts.imName;
				String filename = String.valueOf(url.hashCode());
				FileHandle handle = Gdx.files.external(Consts.TITLE + "/"
						+ filename);
				texture = new Texture(handle);
				background = new TextureRegion(texture, 0, 0, 660, 1024);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if (Settings.needReload == true)
			Settings.needReload = false;
	}

	public static Vector2 V3toV2(Vector3 v) {
		Vector2 v2 = new Vector2(v.x, v.y);
		return v2;
	}

	public static void music() {
		if (Settings.musicEnabled)
			Settings.musicEnabled = false;
		else
			Settings.musicEnabled = true;
		if (Settings.musicEnabled)
			Assets.music.play();
		else
			Assets.music.pause();
	}

	public static void sound() {
		if (Settings.soundEnabled)
			Settings.soundEnabled = false;
		else
			Settings.soundEnabled = true;
	}

}

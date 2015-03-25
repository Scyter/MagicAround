package com.magicaround.lines;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class LinesAssets {
	static FreeTypeFontGenerator generator;

	public static BitmapFont scoreFont;
	public static BitmapFont gametypeFont;
	public static BitmapFont scoreFont2;
	private static int scoreFontSize;
	public static BitmapFont hScoreFont;
	private static int hScoreFontSize;
	public static BitmapFont levelFont;
	private static int levelFontSize;
	public static BitmapFont otherFont;
	private static int otherFontSize;
	public static BitmapFont heroFont;
	private static int heroFontSize;
	public static BitmapFont nextTernSizeFont;
	private static int nextTernSizeFontSize;
	public static BitmapFont infoFont;
	private static int infoFontSize;

	public static Texture texture;
	public static TextureRegion mmClassic;
	public static TextureRegion mmHard;
	public static TextureRegion mmCom;
	public static TextureRegion mmSur;
	public static TextureRegion menuRegion;
	public static TextureRegion backRegion;
	public static ArrayList<TextureRegion> backs;
	public static TextureRegion cellsRegion;
	public static TextureRegion buttonNewBack;
	public static TextureRegion buttonNewGame;
	public static TextureRegion curBackTexture;
	public static Random rand;

	public static TextureRegion[] balls;
	public static TextureRegion ball;

	public static Texture magicTexture;
	public static TextureRegion[] magicRegion;
	public static TextureRegion skill1;
	public static TextureRegion skill2;
	public static TextureRegion skill3;
	public static TextureRegion skill4;
	public static TextureRegion skill5;

	public static TextureRegion sInfo;
	public static TextureRegion levelRegion;
	public static TextureRegion levelEndRegion;
	public static TextureRegion musicRegion;
	public static TextureRegion soundRegion;
	public static TextureRegion noRegion;

	public static TextureRegion ratingRegion;
	public static TextureRegion achiveRegion;

	public static Animation rainAnimation;
	public static TextureRegion firsRain;

	public static Animation lightning;

	public static TextureRegion arrow;

	public static int MAX_EFFECTS = 10;// NB

	public static ArrayList<ArrayList<ParticleEffect>> ballClearEffects;
	public static ArrayList<ParticleEffect> addClearEffects;
	public static float fntRatio;

	public static Music music;

	public static String i1 = "Move balls to create 5+ balls-line.";
	public static float i1X;
	public static float i1Y;
	public static String i1a = "(horisontal, vertical or diagonal)";
	public static float i1aX;
	public static float i1aY;
	public static String i2 = "Line removes and you score some points.";
	public static float i2X;
	public static float i2Y;
	public static String i3 = "3 new balls will appear each tern.";
	public static float i3X;
	public static float i3Y;
	public static String i4 = "Collect as more points as possible.";
	public static float i4X;
	public static float i4Y;
	public static String i5 = "Some new will balls appear each tern.";
	public static float i5X;
	public static float i5Y;
	public static String i6 = "More balls appear at higher levels.";
	public static float i6X;
	public static float i6Y;
	public static String i7 = "You have magic power and skills to use.";
	public static float i7X;
	public static float i7Y;
	public static String i8 = "FIREBALL";
	public static float i8X;
	public static float i8Y;
	public static String i9 = "Remove any ball at game pole";
	public static float i9X;
	public static float i9Y;
	public static String i10 = " magic";
	public static float i10X;
	public static float i10Y;
	public static String i11 = "SWITCH NEXT TERN";
	public static float i11X;
	public static float i11Y;
	public static String i12 = "Change next tern balls for other";
	public static float i12X;
	public static float i12Y;
	public static String i13 = "...";
	public static float i13X;
	public static float i13Y;
	public static String i14 = "...";
	public static float i14X;
	public static float i14Y;
	public static String i15 = "...";
	public static float i15X;
	public static float i15Y;
	static TextBounds tb;

	public static String s1 = "GAME OVER!";
	public static float s1X;
	public static float s1Y;
	public static String s2 = "Score:";
	public static float s2X;
	public static float s2Y;
	public static String s3 = "Hight score:";
	public static float s3X;
	public static float s3Y;
	public static String s4 = "Classic Lines";
	public static float s4X;
	public static float s4Y;
	public static String s5 = "Hardcore Lines";
	public static float s5X;
	public static float s5Y;
	public static String s6 = "Survival Lines";
	public static float s6X;
	public static float s6Y;
	public static String s7 = "Kids Lines";
	public static float s7X;
	public static float s7Y;
	public static String s8 = "Not Enough Magic!";
	public static float s8X;
	public static float s8Y;
	public static String s9 = "Survival Lines: level";
	public static float s9X;
	public static float s9Y;
	public static String s10 = "Next level in";
	public static float s10X;
	public static float s10Y;
	public static String s11 = "moves";
	public static float s11X;
	public static float s11Y;
	public static String s12 = "Hero Level:";
	public static float s12X;
	public static float s12Y;

	public static int language = 0;

	public static void playSound(Sound sound) {
		if (LinesSettings.soundEnabled)
			sound.play(1);
	}

	public static void playMusic(Music music) {
		if (LinesSettings.musicEnabled)
			music.play();
	}

	public static Texture loadTexture(String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static Vector2 V3toV2(Vector3 v) {
		Vector2 v2 = new Vector2(v.x, v.y);
		return v2;
	}

	public static void music() {
		if (LinesSettings.musicEnabled)
			LinesSettings.musicEnabled = false;
		else
			LinesSettings.musicEnabled = true;
		if (LinesSettings.musicEnabled) {
			// LinesAssets.music.play(); ///NB!
		} else
			LinesAssets.music.pause();
	}

	public static void sound() {
		if (LinesSettings.soundEnabled)
			LinesSettings.soundEnabled = false;
		else
			LinesSettings.soundEnabled = true;
	}

	public static void load1() {
		rand = new Random();
		backs = new ArrayList<TextureRegion>();
		texture = LinesAssets.loadTexture("data/texture/1.jpg");
		backRegion = new TextureRegion(texture, 0, 0, texture.getWidth(),
				texture.getHeight());
		backs.add(backRegion);
		curBackTexture = new TextureRegion(texture, 0, 0, texture.getWidth(),
				texture.getHeight());
		texture = new Texture(Gdx.files.internal("data/menu/menu_tmp.png"));
		if (language == 1) {

			mmClassic = new TextureRegion(texture, 600, 0, 600, 100);
			mmHard = new TextureRegion(texture, 600, 100, 600, 100);
			mmCom = new TextureRegion(texture, 600, 200, 600, 100);
			mmSur = new TextureRegion(texture, 600, 300, 600, 100);
			menuRegion = new TextureRegion(texture, 600, 500, 600, 300);
		} else {

			mmClassic = new TextureRegion(texture, 0, 0, 600, 100);
			mmHard = new TextureRegion(texture, 0, 100, 600, 100);
			mmCom = new TextureRegion(texture, 0, 200, 600, 100);
			mmSur = new TextureRegion(texture, 0, 300, 600, 100);
			menuRegion = new TextureRegion(texture, 0, 500, 600, 300);
		}
		texture = new Texture(Gdx.files.internal("data/arrow.png"));
		arrow = new TextureRegion(texture, 0, 0, 256, 256);

		texture = new Texture(Gdx.files.internal("data/magic/rain.png"));
		firsRain = new TextureRegion(texture, 100, 100, 100, 100);
		rainAnimation = new Animation(0.2f, new TextureRegion(texture, 0, 0,
				32, 32), new TextureRegion(texture, 0, 32, 32, 32),
				new TextureRegion(texture, 0, 64, 32, 32), new TextureRegion(
						texture, 0, 96, 32, 32), new TextureRegion(texture, 0,
						128, 32, 32));
		texture = new Texture(Gdx.files.internal("data/magic/lightning.png"));
		lightning = new Animation(0.002f, new TextureRegion(texture, 0, 0, 128,
				512), new TextureRegion(texture, 128, 0, 128, 512),
				new TextureRegion(texture, 256, 0, 128, 512),
				new TextureRegion(texture, 384, 0, 128, 512),
				new TextureRegion(texture, 256, 0, 128, 512),
				new TextureRegion(texture, 128, 0, 128, 512),
				new TextureRegion(texture, 0, 0, 128, 512));

	}

	public static void load2() {
		loadCells();
		loadBalls();
	}

	public static void load3() {
		fntRatio = (float) Gdx.graphics.getWidth() * Gdx.graphics.getWidth()
				/ (355 * Gdx.graphics.getHeight());
		FreeTypeFontParameter ftfp = new FreeTypeFontParameter();
		scoreFontSize = 38;
		hScoreFontSize = 28;
		levelFontSize = 50;
		otherFontSize = 30;
		heroFontSize = 35;
		nextTernSizeFontSize = 45;
		infoFontSize = 35;

		final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"'<>�������������������������������������Ũ��������������������������";
		generator = new FreeTypeFontGenerator(
				Gdx.files.internal("data/aassuan_bold.ttf"));
		ftfp.characters = FONT_CHARACTERS;

		ftfp.size = (int) (scoreFontSize * fntRatio);
		scoreFont = generator.generateFont(ftfp);
		ftfp.size = (int) (scoreFontSize * fntRatio);
		gametypeFont = generator.generateFont(ftfp);
		ftfp.size = (int) (scoreFontSize * fntRatio);
		scoreFont2 = generator.generateFont(ftfp);
		ftfp.size = (int) (hScoreFontSize * fntRatio);
		hScoreFont = generator.generateFont(ftfp);

		generator = new FreeTypeFontGenerator(
				Gdx.files.internal("data/magic2.ttf"));
		ftfp.size = (int) (levelFontSize * fntRatio);
		levelFont = generator.generateFont(ftfp);
		ftfp.size = (int) (otherFontSize * fntRatio);
		otherFont = generator.generateFont(ftfp);

		generator = new FreeTypeFontGenerator(
				Gdx.files.internal("data/magic1.ttf"));
		ftfp.size = (int) (heroFontSize * fntRatio);
		heroFont = generator.generateFont(ftfp);
		ftfp.size = (int) (nextTernSizeFontSize * fntRatio);
		nextTernSizeFont = generator.generateFont(ftfp);
		ftfp.size = (int) (infoFontSize * fntRatio);
		infoFont = generator.generateFont(ftfp);

		tb = hScoreFont.getBounds(i1);
		i1X = tb.width;
		i1Y = tb.height;
		tb = hScoreFont.getBounds(i1a);
		i1aX = tb.width;
		i1aY = tb.height;
		tb = hScoreFont.getBounds(i2);
		i2X = tb.width;
		i2Y = tb.height;
		tb = hScoreFont.getBounds(i3);
		i3X = tb.width;
		i3Y = tb.height;
		tb = hScoreFont.getBounds(i4);
		i4X = tb.width;
		i4Y = tb.height;
		tb = hScoreFont.getBounds(i5);
		i5X = tb.width;
		i5Y = tb.height;
		tb = hScoreFont.getBounds(i6);
		i6X = tb.width;
		i6Y = tb.height;
		tb = hScoreFont.getBounds(i7);
		i7X = tb.width;
		i7Y = tb.height;
		tb = levelFont.getBounds(i8);
		i8X = tb.width;
		i8Y = tb.height;
		tb = hScoreFont.getBounds(i9);
		i9X = tb.width;
		i9Y = tb.height;
		tb = hScoreFont.getBounds(i10);
		i10X = tb.width;
		i10Y = tb.height;
		tb = levelFont.getBounds(i11);
		i11X = tb.width;
		i11Y = tb.height;
		tb = hScoreFont.getBounds(i12);
		i12X = tb.width;
		i12Y = tb.height;
		tb = hScoreFont.getBounds(i13);
		i13X = tb.width;
		i13Y = tb.height;
		tb = hScoreFont.getBounds(i14);
		i14X = tb.width;
		i14Y = tb.height;
		tb = hScoreFont.getBounds(i15);
		i15X = tb.width;
		i15Y = tb.height;
		tb = scoreFont.getBounds(s1);
		s1X = tb.width;
		s1Y = tb.height;
		tb = hScoreFont.getBounds(s2);
		s2X = tb.width;
		s2Y = tb.height;
		tb = hScoreFont.getBounds(s3);
		s3X = tb.width;
		s3Y = tb.height;
		tb = hScoreFont.getBounds(s4);
		s4X = tb.width;
		s4Y = tb.height;
		tb = hScoreFont.getBounds(s5);
		s5X = tb.width;
		s5Y = tb.height;
		tb = hScoreFont.getBounds(s6);
		s6X = tb.width;
		s6Y = tb.height;
		tb = hScoreFont.getBounds(s7);
		s7X = tb.width;
		s7Y = tb.height;
		tb = scoreFont.getBounds(s8);
		s8X = tb.width;
		s8Y = tb.height;
		tb = scoreFont.getBounds(s9);
		s9X = tb.width;
		s9Y = tb.height;
		tb = scoreFont.getBounds(s10);
		s10X = tb.width;
		s10Y = tb.height;
		tb = scoreFont.getBounds(s11);
		s11X = tb.width;
		s11Y = tb.height;

		texture = LinesAssets.loadTexture("data/heart.png");
		// buttonNewBack = new TextureRegion(buttons, 2 / 16 * 256, 3 / 16 *
		// 256,
		// 12 / 16 * 256, 11 / 16 * 256);
		buttonNewBack = new TextureRegion(texture, 32, 48, (int) 192, (int) 176);
		// newGameImage = LinesAssets.loadTexture("data/restart.png");
		// buttonNewGame = new TextureRegion(newGameImage, 0, 0, 128, 128);
	}

	public static void load4() {
		loadBackgrounds();
	}

	public static void load5() {
		loadMagicAndLevel();
	}

	public static void load6() {
		ballClearEffects = new ArrayList<ArrayList<ParticleEffect>>();
		addClearEffects = new ArrayList<ParticleEffect>();
		loadParticleEffects();
	}

	public static void load7() {
		loadParticleEffects2();
	}

	public static void load8() {
		// music =
		// Gdx.audio.newMusic(Gdx.files.internal("data/music/music.mp3"));
		// music.setLooping(true);
		// music.setVolume(0.3f);
		if (LinesSettings.soundEnabled) {
			// music.play();///NB!
		}
	}

	public static void loadParticleEffects() {
		ArrayList<ParticleEffect> ape = new ArrayList<ParticleEffect>();
		ballClearEffects.add(ape);
		for (int j = 1; j <= 5; j++) {
			ArrayList<ParticleEffect> apeX = new ArrayList<ParticleEffect>();
			for (int i = 0; i < MAX_EFFECTS; i++) {
				ParticleEffect pe = new ParticleEffect();
				if (Gdx.graphics.getWidth() > 300) {
					pe.load(Gdx.files.internal("data/remove/!"
							+ String.valueOf(j)),
							Gdx.files.internal("data/remove"));// 44
																// размер
																// эффекта
				} else if (Gdx.graphics.getWidth() > 400) {
					pe.load(Gdx.files.internal("data/remove/b"
							+ String.valueOf(j)),
							Gdx.files.internal("data/remove"));// 66
																// размер
																// эффекта
				} else if (Gdx.graphics.getWidth() > 600) {
					pe.load(Gdx.files.internal("data/remove/a"
							+ String.valueOf(j)),
							Gdx.files.internal("data/remove"));// 88
																// размер
																// эффекта
				} else if (Gdx.graphics.getWidth() > 900) {
					pe.load(Gdx.files.internal("data/remove/c"
							+ String.valueOf(j)),
							Gdx.files.internal("data/remove"));// 132
																// размер
																// эффекта
				} else
					pe.load(Gdx.files.internal("data/remove/z"
							+ String.valueOf(j)),
							Gdx.files.internal("data/remove"));// 32
																// размер
																// эффекта
				apeX.add(pe);
			}
			ballClearEffects.add(apeX);
			for (int i = 0; i < MAX_EFFECTS; i++) {
				ParticleEffect pe = new ParticleEffect();
				if (Gdx.graphics.getWidth() > 300) {
					pe.load(Gdx.files.internal("data/remove/!"
							+ String.valueOf(j)),
							Gdx.files.internal("data/remove"));// 44
																// размер
																// эффекта
				} else if (Gdx.graphics.getWidth() > 400) {
					pe.load(Gdx.files.internal("data/remove/b"
							+ String.valueOf(j)),
							Gdx.files.internal("data/remove"));// 66
																// размер
																// эффекта
				} else if (Gdx.graphics.getWidth() > 600) {
					pe.load(Gdx.files.internal("data/remove/a"
							+ String.valueOf(j)),
							Gdx.files.internal("data/remove"));// 88
																// размер
																// эффекта
				} else if (Gdx.graphics.getWidth() > 900) {
					pe.load(Gdx.files.internal("data/remove/c"
							+ String.valueOf(j)),
							Gdx.files.internal("data/remove"));// 132
																// размер
																// эффекта
				} else
					pe.load(Gdx.files.internal("data/remove/z"
							+ String.valueOf(j)),
							Gdx.files.internal("data/remove"));// 32
																// размер
																// эффекта
				apeX.add(pe);
			}
		}

	}

	public static void loadParticleEffects2() {
		for (int j = 6; j <= 9; j++) {
			ArrayList<ParticleEffect> apeX = new ArrayList<ParticleEffect>();
			for (int i = 0; i < MAX_EFFECTS; i++) {
				ParticleEffect pe = new ParticleEffect();
				if (Gdx.graphics.getWidth() > 400) {
					pe.load(Gdx.files.internal("data/remove/!"
							+ String.valueOf(j)),
							Gdx.files.internal("data/remove"));// 44
																// размер
																// эффекта
				} else if (Gdx.graphics.getWidth() > 600) {
					pe.load(Gdx.files.internal("data/remove/b"
							+ String.valueOf(j)),
							Gdx.files.internal("data/remove"));// 66
																// размер
																// эффекта
				} else if (Gdx.graphics.getWidth() > 800) {
					pe.load(Gdx.files.internal("data/remove/a"
							+ String.valueOf(j)),
							Gdx.files.internal("data/remove"));// 88
																// размер
																// эффекта
				} else if (Gdx.graphics.getWidth() > 1200) {
					pe.load(Gdx.files.internal("data/remove/c"
							+ String.valueOf(j)),
							Gdx.files.internal("data/remove"));// 132
																// размер
																// эффекта
				} else
					pe.load(Gdx.files.internal("data/remove/z"
							+ String.valueOf(j)),
							Gdx.files.internal("data/remove"));// 32
																// размер
																// эффекта
				apeX.add(pe);
			}
			ballClearEffects.add(apeX);
		}

		for (int i = 0; i < 20; i++) {
			ParticleEffect pe = new ParticleEffect();
			if (Gdx.graphics.getWidth() > 400) {
				pe.load(Gdx.files.internal("data/remove/!8"),
						Gdx.files.internal("data/remove"));// 44 размер
															// эффекта
			} else if (Gdx.graphics.getWidth() > 600) {
				pe.load(Gdx.files.internal("data/remove/b8"),
						Gdx.files.internal("data/remove"));// 66 размер
															// эффекта
			} else if (Gdx.graphics.getWidth() > 800) {
				pe.load(Gdx.files.internal("data/remove/a8"),
						Gdx.files.internal("data/remove"));// 88 размер
															// эффекта
			} else if (Gdx.graphics.getWidth() > 1200) {
				pe.load(Gdx.files.internal("data/remove/c8"),
						Gdx.files.internal("data/remove"));// 132 размер
															// эффекта
			} else
				pe.load(Gdx.files.internal("data/remove/z8"),
						Gdx.files.internal("data/remove"));// 32 размер
															// эффекта
			addClearEffects.add(pe);
		}

	}

	private static void loadBalls() {
		balls = new TextureRegion[10];
		for (int i = 0; i <= 9; i++) {
			texture = LinesAssets.loadTexture("data/ball/!!"
					+ String.valueOf(i) + ".png");
			balls[i] = new TextureRegion(texture, 0, 0, 256, 256);
		}

	}

	private static void loadCells() {

		texture = LinesAssets.loadTexture("data/cells.png");
		cellsRegion = new TextureRegion(texture, 0, 0, 1, 1);

	}

	private static void loadBackgrounds() {
		backs.clear();

		texture = LinesAssets.loadTexture("data/texture/1.jpg");
		backRegion = new TextureRegion(texture, 0, 0, texture.getWidth(),
				texture.getHeight());
		backs.add(backRegion);
		texture = LinesAssets.loadTexture("data/texture/2.jpg");
		backRegion = new TextureRegion(texture, 0, 0, texture.getWidth(),
				texture.getHeight());
		backs.add(backRegion);
		texture = LinesAssets.loadTexture("data/texture/4.jpg");
		backRegion = new TextureRegion(texture, 0, 0, texture.getWidth(),
				texture.getHeight());
		backs.add(backRegion);

	}

	public static void loadBackTexrure() {
		TextureRegion tempRegion = new TextureRegion(curBackTexture);
		try {
			curBackTexture = backs.get(rand.nextInt(backs.size()));
		} catch (Exception e) {
			e.printStackTrace();
			curBackTexture = tempRegion;
		}

	}

	private static void loadMagicAndLevel() {
		magicTexture = LinesAssets.loadTexture("data/magic/magic_small.png");
		magicRegion = new TextureRegion[101];
		for (int i = 0; i <= 100; i++) {
			magicRegion[i] = new TextureRegion(magicTexture,
					magicTexture.getWidth(), magicTexture.getHeight(),
					magicTexture.getWidth() * (-1), (-1)
							* magicTexture.getHeight() * i / 100);
		}
		// NB!
		texture = LinesAssets.loadTexture("data/magic/skills_tmp.png");
		skill1 = new TextureRegion(texture, 0, 0, 512, 512);
		skill2 = new TextureRegion(texture, 0, 512, 512, 512);
		skill3 = new TextureRegion(texture, 0, 1024, 512, 512);
		skill4 = new TextureRegion(texture, 0, 1024 + 512, 512, 512);
		skill5 = new TextureRegion(texture, 512, 0, 512, 512);

		texture = LinesAssets.loadTexture("data/magic/remove_1.png");
		skill1 = new TextureRegion(texture, 0, 0, 256, 256);
		texture = LinesAssets.loadTexture("data/magic/skill5.png");
		skill2 = new TextureRegion(texture, 0, 0, 256, 256);

		texture = LinesAssets.loadTexture("data/magic/info.png");
		sInfo = new TextureRegion(texture, 0, 0, 256, 256);
		texture = LinesAssets.loadTexture("data/magic/level.png");
		levelRegion = new TextureRegion(texture, 0, 0, 4, 256);
		levelEndRegion = new TextureRegion(texture, 0, 257, 256, 256);

		texture = LinesAssets.loadTexture("data/menu/note.png");
		musicRegion = new TextureRegion(texture, 0, 0, 256, 256);
		texture = LinesAssets.loadTexture("data/menu/sound.png");
		soundRegion = new TextureRegion(texture, 0, 0, 256, 256);
		texture = LinesAssets.loadTexture("data/menu/no.png");
		noRegion = new TextureRegion(texture, 0, 0, 128, 128);

		texture = LinesAssets.loadTexture("data/magic/rating.png");
		ratingRegion = new TextureRegion(texture, 0, 0, 256, 256);
		texture = LinesAssets.loadTexture("data/magic/achive.png");
		achiveRegion = new TextureRegion(texture, 0, 0, 256, 256);

	}

	public static void disposeTextures() {
		texture.dispose();
		magicTexture.dispose();
	}

	public static void close() {
		scoreFont.dispose();
		hScoreFont.dispose();
		disposeTextures();

	}

}

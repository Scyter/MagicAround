package com.ma.wcc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen extends ScreenClass {
	WorldCupClicker game;
	int width;
	int height;
	OrthographicCamera camera;
	Vector3 touchPoint;
	Vector2 touch;
	SpriteBatch batch;
	Rectangle playGame;
	Rectangle graph;
	Rectangle favoriteTeam;
	Rectangle hatedTeam;
	Rectangle rating;
	Rectangle gPlus;

	public MainMenuScreen(WorldCupClicker g) {
		game = g;
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		touchPoint = new Vector3();
		touch = new Vector2();
		camera = new OrthographicCamera(width, height);
		camera.position.set(width / 2, height / 2, 0);
		touchPoint = new Vector3();
		batch = new SpriteBatch();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		playGame = new Rectangle(width * 0.2f, height * 0.45f, 0.6f * width,
				height * 0.2f);

		favoriteTeam = new Rectangle(width * 0.1f, height * 0.7f, width * 0.3f,
				width * 0.3f);
		hatedTeam = new Rectangle(width * 0.9f - width * 0.3f, height * 0.7f,
				width * 0.3f, width * 0.3f);
		rating = new Rectangle(width * 0.5f, height * 0.25f, width * 0.2f,
				width * 0.2f);
		graph = new Rectangle(width * 0.75f, height * 0.25f, width * 0.2f,
				width * 0.2f);
		gPlus = new Rectangle(width * 0.78f, 0.02f * height, width * 0.15f,
				width * 0.15f);
		game.platform.showAdMob(false);

	}

	@Override
	public void render(float delta) {
		update(delta);
		draw();
	}

	private void draw() {
		drawBackground();
		batch.begin();
		batch.enableBlending();
		drawMenu();
		drawFlags();
		batch.end();
	}

	private void drawMenu() {
		batch.draw(Assets.playGame, playGame.x, playGame.y, playGame.width,
				playGame.height);
		batch.draw(Assets.graph, graph.x, graph.y, graph.width, graph.height);
		batch.draw(Assets.rating, rating.x, rating.y, rating.width,
				rating.height);
		batch.draw(Assets.google, gPlus.x, gPlus.y, gPlus.width, gPlus.height);
		if (Settings.loging)
			batch.draw(Assets.v, gPlus.x, gPlus.y, gPlus.width, gPlus.height);
	}

	private void drawFlags() {

		if (game.player.favorite < 0)
			batch.draw(Assets.question, favoriteTeam.x, favoriteTeam.y,
					favoriteTeam.width, favoriteTeam.height);
		else {
			batch.draw(Assets.teamsFlags.get(game.player.favorite),
					favoriteTeam.x, favoriteTeam.y, favoriteTeam.width,
					favoriteTeam.height);
			Assets.smallTimeFont.draw(
					batch,
					Assets.teams.get(game.player.favorite).nameTMP,
					favoriteTeam.x + (0.5f * favoriteTeam.width)
							- Assets.teams.get(game.player.favorite).X / 2,
					favoriteTeam.y);
		}

		Assets.infoFont.draw(batch, Assets.favorite, favoriteTeam.x
				+ favoriteTeam.width / 2 - Assets.favoriteX / 2, favoriteTeam.y
				+ favoriteTeam.height + Assets.favoriteY + Assets.teamY + 3);
		Assets.infoFont.draw(batch, Assets.team, favoriteTeam.x
				+ favoriteTeam.width / 2 - Assets.teamX / 2, favoriteTeam.y
				+ favoriteTeam.height + Assets.teamY);

		if (game.player.hated < 0)
			batch.draw(Assets.question, hatedTeam.x, hatedTeam.y,
					hatedTeam.width, hatedTeam.height);
		else {
			batch.draw(Assets.teamsFlags.get(game.player.hated), hatedTeam.x,
					hatedTeam.y, hatedTeam.width, hatedTeam.height);
			Assets.smallTimeFont.draw(
					batch,
					Assets.teams.get(game.player.hated).nameTMP,
					hatedTeam.x + hatedTeam.width / 2
							- Assets.teams.get(game.player.hated).X / 2,
					hatedTeam.y);
		}
		Assets.infoFont.draw(batch, Assets.hated, hatedTeam.x + hatedTeam.width
				/ 2 - Assets.hatedX / 2, hatedTeam.y + hatedTeam.height
				+ Assets.hatedY + Assets.teamY + 3);
		Assets.infoFont.draw(batch, Assets.team, hatedTeam.x + hatedTeam.width
				/ 2 - Assets.teamX / 2, hatedTeam.y + hatedTeam.height
				+ Assets.teamY);
	}

	private void drawBackground() {
		batch.begin();
		batch.disableBlending();
		batch.draw(Assets.backgroundPole, 0, 0, width, height);
		batch.end();

	}

	private void update(float delta) {
		backgroundUpdate(delta);
		if (Gdx.input.justTouched()) {
			camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));
			Gdx.app.log("t", touchPoint.toString());
			touch = new Vector2(touchPoint.x, touchPoint.y);
			if (playGame.contains(touch))
				game.setScreen(new ClickerScreen(game));
			else if (graph.contains(touch)) {
				game.setScreen(new GraphScreen(game));
			} else if (favoriteTeam.contains(touch)) {
				game.setScreen(new SelectCountryScreen(game, true, this));
			} else if (hatedTeam.contains(touch)) {
				game.setScreen(new SelectCountryScreen(game, false, this));
			} else if (rating.contains(touch)) {
				game.platform.showTeamResutls();
			} else if (gPlus.contains(touch)) {
				game.platform.loginGPGS();

			}
			game.platform.showAdMob(false);
		}

	}

	private void backgroundUpdate(float delta) {
		// TODO Auto-generated method stub

	}

}

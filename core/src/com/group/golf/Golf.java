package com.group.golf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.group.golf.screens.IntroScreen;

/**
 * Main class for the Crazy Golf game
 * @author Julian Marrades
 * @version 0.1, 03/03/2018
 */
public class Golf extends Game {

	public static final String GAME_NAME = "Crazy Golf";
	public static final int VIRTUAL_WIDTH = 1000;
	public static final int VIRTUAL_HEIGHT = 700;
	public static final float DELTA = 0.04f;

	public SpriteBatch batch;
	public BitmapFont font;
	public ShapeRenderer shapeRenderer;
	
	@Override
	public void create () {
		this.batch = new SpriteBatch();
		this.font = new BitmapFont();
		this.shapeRenderer = new ShapeRenderer();
		this.setScreen(new IntroScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		this.batch.dispose();
		this.font.dispose();
		this.shapeRenderer.dispose();
	}
}

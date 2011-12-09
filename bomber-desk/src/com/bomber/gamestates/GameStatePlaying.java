package com.bomber.gamestates;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.physics.box2d.World;
import com.bomber.GameScreen;

public class GameStatePlaying extends GameState {

	public GameStatePlaying(GameScreen _gameScreen) {
		super(_gameScreen);
	}

	public void update()
	{
		parseInput();
		mGameScreen.mWorld.update();

	}

	public void parseInput()
	{
		if (Gdx.app.getType() != Application.ApplicationType.Android)
		{
			
			if (Gdx.input.isKeyPressed(Keys.LEFT))
				mGameScreen.mWorld.getLocalPlayer().moveLeft();
			
			else if (Gdx.input.isKeyPressed(Keys.RIGHT))
				mGameScreen.mWorld.getLocalPlayer().moveRight();
			else if (Gdx.input.isKeyPressed(Keys.DOWN))
				mGameScreen.mWorld.getLocalPlayer().moveDown();
			else if (Gdx.input.isKeyPressed(Keys.UP))
				mGameScreen.mWorld.getLocalPlayer().moveUp();
			else 
				mGameScreen.mWorld.getLocalPlayer().stop();
		}
	}

	public void present(float _interpolation)
	{
		mGameScreen.mWorldRenderer.render();
	}

}
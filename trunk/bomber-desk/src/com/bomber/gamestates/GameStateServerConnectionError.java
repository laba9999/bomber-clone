package com.bomber.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.bomber.Game;
import com.bomber.common.assets.GfxAssets;

public class GameStateServerConnectionError extends GameState {

	private String mMessage;

	public GameStateServerConnectionError(Game _gameScreen, String _message) {
		super(_gameScreen);

		mMessage = _message;
	}

	@Override
	public void onUpdate()
	{
		if (Gdx.input.justTouched() || Gdx.input.isKeyPressed(Keys.ENTER))
			Game.goBackToActivities();

	}

	public void parseInput()
	{
		throw new UnsupportedOperationException();
	}

	public void onPresent(float _interpolation)
	{
		mBatcher.setProjectionMatrix(mUICamera.combined);

		mBatcher.draw(GfxAssets.Pixmaps.getRed(), 0, 0);
		BitmapFont font = GfxAssets.mGenericFont;

		TextBounds tx = font.getBounds(mMessage);
		font.draw(mBatcher, mMessage, 400 - tx.width / 2, 250);

	}

	@Override
	protected void onFinish()
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void onUpdateFinishing()
	{
		// TODO Auto-generated method stub

	}
}
package com.bomber.gamestates;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.bomber.GameScreen;
import com.bomber.common.Assets;
import com.bomber.input.InputPausedState;

public class GameStatePaused extends GameState {

	public GameStatePaused(GameScreen _gameScreen) {
		super(_gameScreen);
		
		mInput = new InputPausedState(this);
	}

	public void update()
	{
		mInput.update();
	}


	public void present(float _interpolation)
	{
		mWorldRenderer.render();
		mBatcher.setProjectionMatrix(mUICamera.combined);
		
		BitmapFont font = Assets.mFont;
	
		mBatcher.begin();	

		//desenha nivel ao canto
		font.draw(mBatcher,"PAUSED", 600 , 470);
		
		Rectangle[] zones = mInput.getZones();
		for (int i = 0; i < zones.length; i++)
			mBatcher.draw(Assets.mAtlas.findRegion("tiles_", 123), zones[i].x, zones[i].y, zones[i].width, zones[i].height);
		
		mBatcher.end();

	}
}
package com.bomber.gamestates;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.bomber.GameScreen;
import com.bomber.common.Assets;
import com.bomber.input.InputGameOverState;
import com.bomber.input.InputPlayingState;

public class GameStateGameOver extends GameState {

	public GameStateGameOver(GameScreen _gameScreen) {
		super(_gameScreen);
		
		mInput = new InputGameOverState(this);
	}

	@Override
	public void onUpdate()
	{
		mInput.update();
	}

	public void present(float _interpolation)
	{
		mWorldRenderer.render();
		mBatcher.setProjectionMatrix(mUICamera.combined);
		
		BitmapFont font = Assets.mFont;
	
		mBatcher.begin();	

	
		Rectangle[] zones = mInput.getZones();
		for (int i = 0; i < zones.length; i++)
			mBatcher.draw(Assets.mAtlas.findRegion("tiles_", 123), zones[i].x, zones[i].y, zones[i].width, zones[i].height);

		font.draw(mBatcher,"N N N A A A B B B O O O", 350 , 250);

		mBatcher.end();	}

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
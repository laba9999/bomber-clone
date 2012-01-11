package com.bomber.gamestates;

import com.bomber.Game;
import com.bomber.Settings;
import com.bomber.common.ObjectFactory;
import com.bomber.common.assets.GfxAssets;
import com.bomber.gametypes.GameTypeHandler;
import com.bomber.remote.RemoteConnections;
import com.bomber.renderers.WorldRenderer;
import com.bomber.world.GameWorld;

public class GameStateLoading extends GameState {

	public GameStateLoading(Game _gameScreen) {
		super(_gameScreen);

	}

	@Override
	public void onUpdate()
	{
		if (GfxAssets.mFinishedLoading)
		{
			if (Settings.GAME_TYPE == GameTypeHandler.CAMPAIGN)
			{
				mGame.mWorld = new GameWorld(mGame, ObjectFactory.CreateGameTypeHandler.Create(Game.mGameType), Game.mLevelToLoad);
				mGame.mWorldRenderer = new WorldRenderer(mBatcher, mGame.mWorld);
				mGame.mMessagesHandler.mWorld = mGame.mWorld;

			}
			
			mGame.setGameState(new GameStatePlaying(mGame));
		}
		
	}

	public void onPresent()
	{
	}

	@Override
	protected void onFinish()
	{
	}

	@Override
	protected void onUpdateFinishing()
	{
	}
}
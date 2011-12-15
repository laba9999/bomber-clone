package com.bomber.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.bomber.DebugSettings;
import com.bomber.GameScreen;
import com.bomber.common.Assets;
import com.bomber.gameobjects.UIMovableObject;
import com.bomber.input.InputPausedState;

public class GameStatePaused extends GameState {

	// Memory Leaks!!
	// Sâo criadas como estáticas para não termos que fazer o dispose de cada
	// vez que e criado uma nova instancia desta classe
	private static Pixmap mPixmap = null;
	private static Texture mDarkGlass;

	private static UIMovableObject mOptionsPanel = null;

	public GameStatePaused(GameScreen _gameScreen) {
		super(_gameScreen);

		if (mPixmap == null)
		{
			mPixmap = new Pixmap(1024, 512, Pixmap.Format.RGBA4444);

			// Cria o vidro escuro
			mPixmap.setColor(0, 0, 0, 0.8f);
			mPixmap.fill();

			mDarkGlass = new Texture(mPixmap);
			mDarkGlass.draw(mPixmap, 0, 0);
			mDarkGlass.bind();
		}

		if (mOptionsPanel == null)
		{
			int animationSpeed = 25;
			int animationDuration = Assets.mPauseScreen.getRegionWidth() / animationSpeed;
			animationDuration *=2;
			mOptionsPanel = new UIMovableObject(animationSpeed, 800 + Assets.mPauseScreen.getRegionWidth(), 0, -1.0f, 0f, animationDuration);
			mOptionsPanel.mTexture = Assets.mPauseScreen;
		}


		mInput = new InputPausedState(this);
	}

	

	@Override
	public void reset()
	{
		super.reset();
		mOptionsPanel.reset();
		mOptionsPanel.setDirection(-1.0f, 0f);
		mOptionsPanel.mIsMoving = true;
	}
	
	@Override
	public void onUpdate()
	{
		if (!mOptionsPanel.mIsMoving)
		{
			mInput.update();
			return;
		}

		mOptionsPanel.update();
	}

	@Override
	protected void onUpdateFinishing()
	{
		if (!mOptionsPanel.mIsMoving)
		{
			mGameScreen.setGameState(mNextGameState);
			return;
		}
		
		mOptionsPanel.update();
	}
	
	public void onPresent(float _interpolation)
	{
		mWorldRenderer.render();
		mBatcher.setProjectionMatrix(mUICamera.combined);

		// Desenha o vidro escuro
		mBatcher.draw(mDarkGlass, 0, 0);

		// Desenha o painel de opções
		mBatcher.draw(mOptionsPanel.mTexture, mOptionsPanel.mPosition.x, mOptionsPanel.mPosition.y);

	}

	private void drawDebugInfo(BitmapFont _font)
	{
		if (DebugSettings.UI_DRAW_INPUT_ZONES)
		{
			Rectangle[] zones = mInput.getZones();
			for (int i = 0; i < zones.length; i++)
				mBatcher.draw(Assets.mAtlas.findRegion("tiles_", 123), zones[i].x, zones[i].y, zones[i].width, zones[i].height);
		}

		if (DebugSettings.UI_DRAW_FPS)
		{
			Integer fps = Gdx.graphics.getFramesPerSecond();
			_font.draw(mBatcher, fps.toString(), 115, 470);
			_font.draw(mBatcher, GameScreen.ticksPerSecond.toString(), 165, 470);
		}
	}
	
	
	@Override
	public void onFinish()
	{
		mOptionsPanel.mIsMoving = true;
		mOptionsPanel.mTicksCounter = 0;
		mOptionsPanel.setDirection(1, 0);
	}


}
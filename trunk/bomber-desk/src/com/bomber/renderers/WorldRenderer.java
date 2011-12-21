package com.bomber.renderers;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bomber.Game;
import com.bomber.OverlayingText;
import com.bomber.common.PlayerEffect;
import com.bomber.common.assets.Assets;
import com.bomber.gameobjects.Bomb;
import com.bomber.gameobjects.Drawable;
import com.bomber.gameobjects.Flag;
import com.bomber.gameobjects.Player;
import com.bomber.gameobjects.Tile;
import com.bomber.gameobjects.bonus.Bonus;
import com.bomber.gameobjects.monsters.Monster;
import com.bomber.gametypes.GameTypeHandler;
import com.bomber.world.GameMap;
import com.bomber.world.GameWorld;

public class WorldRenderer {
	static final float FRUSTUM_WIDTH = 800;
	static final float FRUSTUM_HEIGHT = 480;

	GameWorld mWorld;
	SpriteBatch mBatch;
	OrthographicCamera mCamera;

	private short mTicksSincePlayerLastBlink;
	private boolean mBlinkLocalPlayer = false;

	public WorldRenderer(SpriteBatch _batch, GameWorld _world) {
		mWorld = _world;
		mBatch = _batch;

		mCamera = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
	}

	public void render()
	{
		// Porque é chamado antes da apresentação da UI
		mBatch.end();

		// TODO: retirar quando já não for necessário para ver os FPS
		GLCommon gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// Actualiza a posição da camera para seguir o local player
		updateCameraPosition();

		// Apresenta os GFX's espectaculares (tem que ser por esta ordem!)
		mBatch.disableBlending();
		renderTiles();

		mBatch.enableBlending();
		mBatch.begin();
		renderBonus();
		renderTilesExplosions();
		renderFlags();
		renderBombs();
		renderExplosions();
		renderPlayers();
		renderMonsters();

		renderOverlayingText();
	}

	private void renderFlags()
	{
		if (Game.mGameType != GameTypeHandler.CTF && Game.mGameType != GameTypeHandler.TEAM_CTF)
			return;

		Flag flag = mWorld.mFlags[0];
		Vector2 drawingPos = flag.drawingPoint();
		mBatch.draw(flag.getTexture(), drawingPos.x, drawingPos.y);

		flag = mWorld.mFlags[1];
		drawingPos = flag.drawingPoint();
		mBatch.draw(flag.getTexture(), drawingPos.x, drawingPos.y);

	}

	private void updateCameraPosition()
	{
		GameMap map = mWorld.mMap;
		Vector2 localPlayerPos = mWorld.getLocalPlayer().mPosition;

		mCamera.position.y = (int) localPlayerPos.y - 10;
		mCamera.position.y = java.lang.Math.min(mCamera.position.y, map.mHeightPixels - Tile.TILE_SIZE * 4);
		mCamera.position.y = java.lang.Math.max(mCamera.position.y, Tile.TILE_SIZE * 4);

		mCamera.position.x = (int) localPlayerPos.x - 10;
		mCamera.position.x = java.lang.Math.min(mCamera.position.x, map.mWidthPixels - Tile.TILE_SIZE * 8);
		mCamera.position.x = java.lang.Math.max(mCamera.position.x, Tile.TILE_SIZE * 8);

		mCamera.update();

		mBatch.setProjectionMatrix(mCamera.combined);
	}

	private void renderTiles()
	{
		List<Tile> map = mWorld.mMap.mTilesMap;

		mBatch.begin();
		for (int i = 0; i < map.size(); i++)
		{
			Tile tmpTile = map.get(i);

			mBatch.draw(tmpTile.mCurrentFrame, tmpTile.mPosition.x, tmpTile.mPosition.y);
		}

		mBatch.end();

	}

	private void renderTilesExplosions()
	{

		for (Tile t : mWorld.mMap.mTilesBeingDestroyed)
			mBatch.draw(t.mCurrentFrame, t.mPosition.x, t.mPosition.y);
	}

	private void renderBonus()
	{
		for (Bonus b : mWorld.mSpawnedBonus)
		{
			Vector2 drawingPoint = b.drawingPoint();
			drawingPoint.x += (Tile.TILE_SIZE - b.mCurrentFrame.getRegionWidth()) / 2;
			mBatch.draw(b.mCurrentFrame, drawingPoint.x, drawingPoint.y);
		}
	}

	private void renderBombs()
	{
		for (Bomb b : mWorld.mBombs)
		{
			Vector2 drawingPoint = b.drawingPoint();
			drawingPoint.x += (Tile.TILE_SIZE - b.mCurrentFrame.getRegionWidth()) / 2;
			mBatch.draw(b.mCurrentFrame, drawingPoint.x, drawingPoint.y);
		}
	}

	private void renderExplosions()
	{
		for (Drawable b : mWorld.mExplosions)
		{
			Vector2 drawingPoint = b.drawingPoint();
			drawingPoint.x += (Tile.TILE_SIZE - b.mCurrentFrame.getRegionWidth()) / 2;
			mBatch.draw(b.mCurrentFrame, drawingPoint.x, drawingPoint.y);
		}
	}

	private void renderPlayers()
	{
		for (Player p : mWorld.mPlayers)
		{
			Vector2 drawingPoint = p.drawingPoint();
			drawingPoint.x += (Tile.TILE_SIZE - p.mCurrentFrame.getRegionWidth()) / 2;

			// Verifica se é suposto apresentar o piscar da imunidade do spawn
			if (p.isImmune())
			{
				if (mTicksSincePlayerLastBlink++ > Player.PLAYER_BLINK_SPEED)
				{
					mTicksSincePlayerLastBlink = 0;
					mBlinkLocalPlayer = !mBlinkLocalPlayer;
				}
			} else
				mBlinkLocalPlayer = false;

			if (mBlinkLocalPlayer)
				mBatch.setColor(1, 1, 1, 0.5f);

			mBatch.draw(p.mCurrentFrame, drawingPoint.x, drawingPoint.y);
			mBatch.setColor(1, 1, 1, 1);

			// Desenha os efeitos
			for (PlayerEffect ef : p.mEffects)
			{
				drawingPoint = ef.drawingPoint();
				mBatch.draw(ef.mCurrentFrame, drawingPoint.x, drawingPoint.y);
			}

			// mBatch.draw(Assets.mAtlas.findRegion("tiles_",123),
			// p.getBoundingBox().x, p.getBoundingBox().y,
			// p.getBoundingBox().width, p.getBoundingBox().height);
		}
	}

	private void renderMonsters()
	{
		for (Monster m : mWorld.mMonsters)
		{
			Vector2 drawingPoint = m.drawingPoint();
			drawingPoint.x += (Tile.TILE_SIZE - m.mCurrentFrame.getRegionWidth()) / 2;
			mBatch.draw(m.mCurrentFrame, drawingPoint.x, drawingPoint.y);

			// mBatch.draw(Assets.mAtlas.findRegion("tiles_",123),
			// m.getBoundingBox().x, m.getBoundingBox().y,
			// m.getBoundingBox().width, m.getBoundingBox().height);
		}
	}

	private void renderOverlayingText()
	{
		// TODO: alterar font
		BitmapFont font = Assets.mFont;

		for (OverlayingText t : mWorld.mOverlayingPoints)
		{
			Vector2 drawingPoint = t.mPosition;
			font.draw(mBatch, t.mText, drawingPoint.x, drawingPoint.y);
		}
	}
}

package com.bomber.renderers;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bomber.common.Assets;
import com.bomber.gameobjects.Player;
import com.bomber.gameobjects.Tile;
import com.bomber.gameobjects.monsters.Monster;
import com.bomber.world.GameMap;
import com.bomber.world.GameWorld;

public class WorldRenderer {
	static final float FRUSTUM_WIDTH = 800;
	static final float FRUSTUM_HEIGHT = 480;

	GameWorld mWorld;
	SpriteBatch mBatch;
	OrthographicCamera mCamera;

	public WorldRenderer(SpriteBatch _batch, GameWorld _world) {
		mWorld = _world;
		mBatch = _batch;

		mCamera = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		mCamera.position.set(FRUSTUM_WIDTH / 2, FRUSTUM_HEIGHT / 2, 0);
	}

	public void render()
	{

		// TODO: retirar quando já não for necessário para ver os FPS
		GLCommon gl = Gdx.gl;
		gl.glClearColor(1, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		// Actualiza a posição da camera para seguir o local player
		updateCameraPosition();

		// Apresenta os GFX's espectaculares (tem que ser por esta ordem!)
		mBatch.disableBlending();
		renderTiles();

		mBatch.enableBlending();
		renderBonus();
		renderBombs();
		renderExplosions();
		renderPlayers();
		renderMonsters();
		renderFPS();
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

	private void renderFPS()
	{
		mBatch.begin();
		Integer fps = Gdx.graphics.getFramesPerSecond();
		Assets.mFont.draw(mBatch, fps.toString(), 100, 650);
		mBatch.end();
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

	private void renderBonus()
	{

	}

	private void renderBombs()
	{

	}

	private void renderExplosions()
	{

	}

	private void renderPlayers()
	{
		mBatch.begin();
		for (Player p : mWorld.mPlayers)
		{
			Vector2 drawingPoint = p.drawingPoint();
			drawingPoint.x += (Tile.TILE_SIZE - p.mCurrentFrame.getRegionWidth()) / 2;
			mBatch.draw(p.mCurrentFrame, drawingPoint.x, drawingPoint.y);
			// mBatch.draw(Assets.mAtlas.findRegion("tiles_",123),
			// p.getBoundingBox().x, p.getBoundingBox().y,
			// p.getBoundingBox().width, p.getBoundingBox().height);
		}
		mBatch.end();
	}

	private void renderMonsters()
	{
		mBatch.begin();
		for (Monster m : mWorld.mMonsters)
		{
			Vector2 drawingPoint = m.drawingPoint();
			drawingPoint.x += (Tile.TILE_SIZE - m.mCurrentFrame.getRegionWidth()) / 2;
			mBatch.draw(m.mCurrentFrame, drawingPoint.x, drawingPoint.y);

			// mBatch.draw(Assets.mAtlas.findRegion("tiles_",123),
			// m.getBoundingBox().x, m.getBoundingBox().y,
			// m.getBoundingBox().width, m.getBoundingBox().height);
		}
		mBatch.end();
	}

}

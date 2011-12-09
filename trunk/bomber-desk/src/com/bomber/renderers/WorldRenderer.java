package com.bomber.renderers;

import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bomber.gameobjects.Player;
import com.bomber.gameobjects.Tile;
import com.bomber.gameobjects.monsters.Monster;
import com.bomber.world.GameWorld;

public class WorldRenderer {
	static final float FRUSTUM_WIDTH = 800;
	static final float FRUSTUM_HEIGHT = 480;

	GameWorld mWorld;
	OrthographicCamera mCamera;
	SpriteBatch mBatch;

	public WorldRenderer(SpriteBatch _batch, GameWorld _world) {
		mWorld = _world;
		mCamera = new OrthographicCamera(FRUSTUM_WIDTH,FRUSTUM_HEIGHT);
		mCamera.position.set(FRUSTUM_WIDTH / 2, FRUSTUM_HEIGHT / 2, 0);
		//mCamera.position.set(800 / 2, 480 / 2, 0);
		
		mBatch = _batch;
	}

	public void render()
	{
		Player localPlayer = mWorld.getLocalPlayer();

		// Actualiza a posi��o da camera para seguir o local player
		if (localPlayer.mPosition.y > mCamera.position.y)
			mCamera.position.y = localPlayer.mPosition.y-100;

		if (localPlayer.mPosition.x > mCamera.position.x)
			mCamera.position.x = localPlayer.mPosition.x;

		mCamera.update();
		mBatch.setProjectionMatrix(mCamera.combined);

		// Apresenta os GFX's espectaculares, e tem que ser por esta ordem
		mBatch.disableBlending();
		renderTiles();
		
		mBatch.enableBlending();
		renderBonus();
		renderBombs();
		renderExplosions();
		renderPlayers();
		renderMonsters();
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
		for(Player p : mWorld.mPlayers)
		{
			Vector2 drawingPoint = p.drawingPoint();
			mBatch.draw(p.mCurrentFrame, drawingPoint.x, drawingPoint.y);
		}
		mBatch.end();
	}
	
	private void renderMonsters()
	{
		mBatch.begin();
		for(Monster m : mWorld.mMonsters)
		{
			Vector2 drawingPoint = m.drawingPoint();
			mBatch.draw(m.mCurrentFrame,drawingPoint.x, drawingPoint.y);
		}
		mBatch.end();
	}
	

}
package com.bomber.renderers;

import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bomber.common.Assets;
import com.bomber.gameobjects.Player;
import com.bomber.gameobjects.Tile;
import com.bomber.world.GameWorld;

public class WorldRenderer {
	static final float FRUSTUM_WIDTH = 10;
	static final float FRUSTUM_HEIGHT = 15;

	GameWorld mWorld;
	OrthographicCamera mCamera;
	SpriteBatch mBatch;

	public WorldRenderer(SpriteBatch _batch, GameWorld _world) {
		mWorld = _world;
		mCamera = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		mCamera.position.set(FRUSTUM_WIDTH / 2, FRUSTUM_HEIGHT / 2, 0);
		mBatch = _batch;
	}

	public void render()
	{
		Player localPlayer = mWorld.getLocalPlayer();

		if (localPlayer.mPosition.y > mCamera.position.y)
			mCamera.position.y = localPlayer.mPosition.y;

		if (localPlayer.mPosition.x > mCamera.position.x)
			mCamera.position.x = localPlayer.mPosition.x;

		mCamera.update();
		mBatch.setProjectionMatrix(mCamera.combined);

		renderTiles();
	}

	public void renderTiles()
	{
		mBatch.disableBlending();
		
		List<Tile> map = mWorld.mMap.mTilesMap;
		mBatch.begin();
		for (int i = 0; i < map.size(); i++)
		{
			Tile tmpTile = map.get(i);
			mBatch.draw(tmpTile.mCurrentFrame, tmpTile.mPosition.x, tmpTile.mPosition.y);
		}
		mBatch.end();
	}

}

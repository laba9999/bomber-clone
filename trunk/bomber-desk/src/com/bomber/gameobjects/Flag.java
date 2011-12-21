package com.bomber.gameobjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.bomber.common.assets.Assets;

public class Flag extends GameObject {

	public short mBelongsToTeamId;
	public Player mTransporter = null;

	public Vector2 mSpawnPosition;

	// Posição 0 : flag com pole
	// Posição 1 : flag para transporte
	private TextureRegion[] mFlags = new TextureRegion[2];

	public Flag(short _teamID, Vector2 _spawnPos) {
		mBelongsToTeamId = _teamID;
		mSpawnPosition = _spawnPos;
		mPosition.set(_spawnPos);
		
		mTransporter = null;

		if (mBelongsToTeamId == 0)
		{
			mFlags[0] = Assets.mFlags.get("flag_pole_team1");
			mFlags[1] = Assets.mFlags.get("flag_transport_team1");
		} else
		{
			mFlags[0] = Assets.mFlags.get("flag_pole_team2");
			mFlags[1] = Assets.mFlags.get("flag_transport_team2");
		}
	}

	@Override
	public Vector2 drawingPoint()
	{
		if (mTransporter == null)
			return mPosition;

		mDrawingPoint.set(mPosition);
		mDrawingPoint.y += Tile.TILE_SIZE_HALF;

		return mDrawingPoint;
	}

	public TextureRegion getTexture()
	{
		if (mTransporter != null)
			return mFlags[1];
		else
			return mFlags[0];
	}

	@Override
	public void update()
	{
		if (mTransporter != null)
			mPosition.set(mTransporter.mPosition);

	}

	@Override
	public void reset()
	{
		mPosition.set(mSpawnPosition);
		mTransporter = null;
	}

}

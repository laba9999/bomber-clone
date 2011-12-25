package com.amov.bomber;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bomber.DebugSettings;
import com.bomber.common.Achievements;
import com.bomber.common.BonusBuild;
import com.bomber.gametypes.GameTypeHandler;
import com.bomber.remote.Message;

public class BuildActivity extends GameActivity
{
	private static final int[] EXPLOSION_INDICATORS_RESOURCES = { R.id.imageBuildExplosion1, R.id.imageBuildExplosion2, R.id.imageBuildExplosion3 };

	private static final int[] BOMBS_INDICATORS_RESOURCES = { R.id.imageBuildBombs1, R.id.imageBuildBombs2, R.id.imageBuildBombs3 };

	private static final int[] SPEED_INDICATORS_RESOURCES = { R.id.imageBuildSpeed1, R.id.imageBuildSpeed2, R.id.imageBuildSpeed3 };

	Integer mAvailablePoints = 0;
	int mExplosionPoints = 0;
	int mBombsPoints = 0;
	int mSpeedPoints = 0;

	TextView mTextAvailablePoints;
	EditText mEditUsername;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.build);

		if (Achievements.isMonsterKillsCompleted())
			mAvailablePoints++;
		if (Achievements.isPlayerKillsCompleted())
			mAvailablePoints++;
		if (Achievements.isDMWinsCompleted())
			mAvailablePoints++;
		if (Achievements.isCTFWinsCompleted())
			mAvailablePoints++;
		if (Achievements.isTimePlayedCompleted())
			mAvailablePoints++;
		if (Achievements.isCampaignCompleted())
			mAvailablePoints++;

		// Coloca o username que foi guardado nas preferencias
		mEditUsername = (EditText) findViewById(R.id.editBuildUsername);
		InputFilter maxLengthFilter = new InputFilter.LengthFilter(Message.STRING_MAX_SIZE);
		mEditUsername.setFilters(new InputFilter[] { maxLengthFilter });
		mEditUsername.setText(DebugSettings.PLAYER_NAME);

		// Lê a build das preferências
		for (int i = 0; i < DebugSettings.GAME_PREFS.getInt("buildExplosionSize", 0); i++)
		{
			onExplosionPlusButton(null);
			mAvailablePoints--;
		}
		for (int i = 0; i < DebugSettings.GAME_PREFS.getInt("buildBombCount", 0); i++)
		{
			onBombsPlusButton(null);
			mAvailablePoints--;
		}

		for (int i = 0; i < DebugSettings.GAME_PREFS.getInt("buildSpeed", 0); i++)
		{
			onSpeedPlusButton(null);
			mAvailablePoints--;
		}

		mTextAvailablePoints = (TextView) findViewById(R.id.textBuildAvailablePointsValue);
		mTextAvailablePoints.setText(mAvailablePoints.toString());
	}

	public void onExplosionPlusButton(View v)
	{
		if (mAvailablePoints > 0 && mExplosionPoints < 3)
		{
			ImageView indicator = (ImageView) findViewById(EXPLOSION_INDICATORS_RESOURCES[mExplosionPoints]);
			mExplosionPoints++;

			Bitmap on = BitmapFactory.decodeResource(getResources(), R.drawable.indicator_on);
			indicator.setImageBitmap(on);
			mAvailablePoints--;
			mTextAvailablePoints.setText(mAvailablePoints.toString());
		}
	}

	public void onExplosionMinusButton(View v)
	{
		if (mExplosionPoints > 0)
		{
			mExplosionPoints--;
			ImageView indicator = (ImageView) findViewById(EXPLOSION_INDICATORS_RESOURCES[mExplosionPoints]);

			Bitmap off = BitmapFactory.decodeResource(getResources(), R.drawable.indicator_off);
			indicator.setImageBitmap(off);
			mAvailablePoints++;
			mTextAvailablePoints.setText(mAvailablePoints.toString());

		}
	}

	public void onBombsPlusButton(View v)
	{
		if (mAvailablePoints > 0 && mBombsPoints < 3)
		{
			ImageView indicator = (ImageView) findViewById(BOMBS_INDICATORS_RESOURCES[mBombsPoints]);
			mBombsPoints++;

			Bitmap on = BitmapFactory.decodeResource(getResources(), R.drawable.indicator_on);
			indicator.setImageBitmap(on);
			mAvailablePoints--;
			mTextAvailablePoints.setText(mAvailablePoints.toString());
		}
	}

	public void onStartButton(View v)
	{
		BonusBuild.mExplosionSize = mExplosionPoints;
		BonusBuild.mBombCount = mBombsPoints;
		BonusBuild.mSpeed = mSpeedPoints;

		DebugSettings.PLAYER_NAME = mEditUsername.getText().toString();

		SharedPreferences.Editor edit = DebugSettings.GAME_PREFS.edit();
		edit.putInt("buildExplosionSize", mExplosionPoints);
		edit.putInt("buildBombCount", mBombsPoints);
		edit.putInt("buildSpeed", mSpeedPoints);

		edit.putString("playerName", DebugSettings.PLAYER_NAME);
		edit.commit();

		DebugSettings.GAME_TYPE = GameTypeHandler.CTF;
		launchActivity(AndroidGame.class);
	}

	public void onBombsMinusButton(View v)
	{
		if (mBombsPoints > 0)
		{
			mBombsPoints--;
			ImageView indicator = (ImageView) findViewById(BOMBS_INDICATORS_RESOURCES[mBombsPoints]);

			Bitmap off = BitmapFactory.decodeResource(getResources(), R.drawable.indicator_off);
			indicator.setImageBitmap(off);
			mAvailablePoints++;
			mTextAvailablePoints.setText(mAvailablePoints.toString());

		}
	}

	public void onSpeedPlusButton(View v)
	{
		if (mAvailablePoints > 0 && mSpeedPoints < 3)
		{
			ImageView indicator = (ImageView) findViewById(SPEED_INDICATORS_RESOURCES[mSpeedPoints]);
			mSpeedPoints++;

			Bitmap on = BitmapFactory.decodeResource(getResources(), R.drawable.indicator_on);
			indicator.setImageBitmap(on);
			mAvailablePoints--;
			mTextAvailablePoints.setText(mAvailablePoints.toString());
		}
	}

	public void onSpeedMinusButton(View v)
	{
		if (mSpeedPoints > 0)
		{
			mSpeedPoints--;
			ImageView indicator = (ImageView) findViewById(SPEED_INDICATORS_RESOURCES[mSpeedPoints]);

			Bitmap off = BitmapFactory.decodeResource(getResources(), R.drawable.indicator_off);
			indicator.setImageBitmap(off);
			mAvailablePoints++;
			mTextAvailablePoints.setText(mAvailablePoints.toString());
		}
	}
}

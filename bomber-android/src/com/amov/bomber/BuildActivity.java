package com.amov.bomber;

import java.io.IOException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bomber.Game;
import com.bomber.Settings;
import com.bomber.common.Achievements;
import com.bomber.common.BonusBuild;
import com.bomber.common.Utils;
import com.bomber.gametypes.GameTypeHandler;
import com.bomber.remote.Message;
import com.bomber.remote.Protocols;

public class BuildActivity extends GameActivity
{
	private static final int[] EXPLOSION_INDICATORS_RESOURCES = { R.id.imageBuildExplosion1, R.id.imageBuildExplosion2, R.id.imageBuildExplosion3 };

	private static final int[] BOMBS_INDICATORS_RESOURCES = { R.id.imageBuildBombs1, R.id.imageBuildBombs2, R.id.imageBuildBombs3 };

	private static final int[] SPEED_INDICATORS_RESOURCES = { R.id.imageBuildSpeed1, R.id.imageBuildSpeed2, R.id.imageBuildSpeed3 };

	static final int DIALOG_PROGRESS = 0;

	int mAvailablePoints;
	int mExplosionPoints;
	int mBombsPoints;
	int mSpeedPoints;

	TextView mTextAvailablePoints;
	EditText mEditUsername;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.build);
		
		mAvailablePoints = 0;
		mExplosionPoints = 0;
		mBombsPoints = 0;
		mSpeedPoints = 0;
		
		
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
		mEditUsername.setText(Settings.PLAYER_NAME);

		mTextAvailablePoints = (TextView) findViewById(R.id.textBuildAvailablePointsValue);

		// Lê a build das preferências
		for (int i = 0; i < Settings.GAME_PREFS.getInt("buildExplosionSize", 0); i++)
			onExplosionPlusButton(null);

		for (int i = 0; i < Settings.GAME_PREFS.getInt("buildBombCount", 0); i++)
			onBombsPlusButton(null);

		for (int i = 0; i < Settings.GAME_PREFS.getInt("buildSpeed", 0); i++)
			onSpeedPlusButton(null);

		mTextAvailablePoints.setText(new Integer(mAvailablePoints).toString());
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
			mTextAvailablePoints.setText(new Integer(mAvailablePoints).toString());
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
			mTextAvailablePoints.setText(new Integer(mAvailablePoints).toString());

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
			mTextAvailablePoints.setText(new Integer(mAvailablePoints).toString());
		}
	}

	public void onStartButton(View v)
	{
		BonusBuild.mExplosionSize = mExplosionPoints;
		BonusBuild.mBombCount = mBombsPoints;
		BonusBuild.mSpeed = mSpeedPoints;

		Settings.PLAYER_NAME = Utils.filterName(mEditUsername.getText().toString());

		SharedPreferences.Editor edit = Settings.GAME_PREFS.edit();
		edit.putInt("buildExplosionSize", mExplosionPoints);
		edit.putInt("buildBombCount", mBombsPoints);
		edit.putInt("buildSpeed", mSpeedPoints);

		edit.putString("playerName", Settings.PLAYER_NAME);
		edit.commit();

		if (Settings.GAME_TYPE == 0)
			Settings.GAME_TYPE = GameTypeHandler.CTF;

		if (Settings.PLAYING_ONLINE)
		{
			Settings.REMOTE_PROTOCOL_IN_USE = Settings.PROTOCOL_TO_USE_ONLINE;
			if (Settings.START_ANDROID_AS_SERVER)
				new RegisterOnlineServer().execute();
			else if (!Settings.START_ANDROID_AS_SERVER)
				new FetchOnlineServer().execute();
		} else
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
			mTextAvailablePoints.setText(new Integer(mAvailablePoints).toString());

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
			mTextAvailablePoints.setText(new Integer(mAvailablePoints).toString());
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
			mTextAvailablePoints.setText(new Integer(mAvailablePoints).toString());
		}
	}

	protected Dialog onCreateDialog(int id)
	{
		Dialog dialog;
		switch (id)
		{
		case DIALOG_PROGRESS:
			try
			{
				removeDialog(DIALOG_PROGRESS);
				dialog = ProgressDialog.show(BuildActivity.this, "", getApplication().getString(R.string.loading_progress), true, true);

				dialog.setOnCancelListener(new OnCancelListener()
				{
					public void onCancel(DialogInterface _dialog)
					{
						finish();
					}
				});

			} catch (Exception e)
			{
				dialog = null;
			}
			break;
		default:
			dialog = null;
		}

		return dialog;
	}

	private class FetchOnlineServer extends AsyncTask<Void, Void, String>
	{
		@Override
		protected void onPreExecute()
		{
			showDialog(DIALOG_PROGRESS);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String _result)
		{

			removeDialog(DIALOG_PROGRESS);

			if (isCancelled())
			{
				Toast.makeText(getApplication(), getApplication().getString(R.string.error_serverconnection), Toast.LENGTH_SHORT).show();
				return;
			}

			Game.LOGGER.log("Fetch server res: " + _result);
			String[] results = _result.split(";");
			if (results[0].equals("N/A2"))
			{
				Toast.makeText(getApplication(), getApplication().getString(R.string.error_no_online_servers), Toast.LENGTH_LONG).show();
			} else if (!results[0].equals("OK"))
			{
				if (results[0].equals("BAN"))
				{
					Toast.makeText(getApplication(), getApplication().getString(R.string.error_registration_ban), Toast.LENGTH_SHORT).show();
					return;
				} else
					Toast.makeText(getApplication(), getApplication().getString(R.string.error_server_registration), Toast.LENGTH_SHORT).show();
				return;
			}

			Settings.REMOTE_SERVER_ADDRESS = results[1];
			Game.LOGGER.log("Fetched remote addr: " + results[1]);
			launchActivity(AndroidGame.class);

			super.onPostExecute(_result);
		}

		@Override
		protected String doInBackground(Void... _params)
		{
			try
			{
				return NetUtils.getDBResult("fetch_server.php?mac=" + NetUtils.getIMEI(BuildActivity.this));

			} catch (IOException e)
			{
				e.printStackTrace();
				cancel(true);
			}

			return null;
		}

	}

	private class RegisterOnlineServer extends AsyncTask<Void, Void, String>
	{
		@Override
		protected void onPreExecute()
		{
			showDialog(DIALOG_PROGRESS);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String _result)
		{

			removeDialog(DIALOG_PROGRESS);

			if (isCancelled())
			{
				Toast.makeText(getApplication(), getApplication().getString(R.string.error_serverconnection), Toast.LENGTH_SHORT).show();
				return;
			}

			String[] results = _result.split(";");
			if (!results[0].equals("OK"))
			{
				if (results[0].equals("BAN"))
				{
					Toast.makeText(getApplication(), getApplication().getString(R.string.error_registration_ban), Toast.LENGTH_SHORT).show();
					return;
				} else
					Toast.makeText(getApplication(), getApplication().getString(R.string.error_server_registration), Toast.LENGTH_SHORT).show();
				return;
			}

			Settings.AVERAGE_WAITING_TIME_ONLINE = getApplication().getString(R.string.average_waiting_time) + results[1];

			launchActivity(AndroidGame.class);

			super.onPostExecute(_result);
		}

		@Override
		protected String doInBackground(Void... _params)
		{
			try
			{
				String[] addressComponents = Settings.LOCAL_SERVER_ADDRESS.split(":");
				return NetUtils.getDBResult("register_server.php?port=" + addressComponents[1] + "&mac=" + NetUtils.getIMEI(BuildActivity.this));

			} catch (IOException e)
			{
				e.printStackTrace();
				cancel(true);
			}

			return null;
		}


	}

}

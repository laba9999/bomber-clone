package com.amov.bomber;

import java.io.IOException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.utils.Base64Coder;
import com.bomber.Game;
import com.bomber.Settings;
import com.bomber.common.Utils;

public class TopActivity extends GameActivity
{
	static final int DIALOG_NAME = 0;
	static final int DIALOG_PROGRESS = 1;

	private boolean mListingTop = false;
	

	private int mNumberScoresInServer = 0;
	private int mPlayerRank = 0;
	private int mCurrentPage;
	private int mMaxPage;
	private final int mScoresPerPage = 10;

	EditText mEditUsername;;

	LinearLayout mLayout;
	LayoutInflater mInflater;

	String[] mSplitedData;
	String mAnswerString;

	Button mLeftButton;
	Button mRightButton;
	Button mFirstPageButton;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if(mGoneBackToAssetsLoader)
		{
			finish();
			return;
		}
		setContentView(R.layout.top);

		mEditUsername = new EditText(this);
		mEditUsername.setText(Settings.PLAYER_NAME);

		mLayout = (LinearLayout) findViewById(R.id.topItemsLayout);
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mLeftButton = (Button) findViewById(R.id.buttonTopLeft);
		mRightButton = (Button) findViewById(R.id.ButtonTopRight);
		mFirstPageButton = (Button) findViewById(R.id.buttonFirstPage);

		mLeftButton.setEnabled(false);
		mLeftButton.setVisibility(Button.INVISIBLE);

		mRightButton.setEnabled(false);
		mRightButton.setVisibility(Button.INVISIBLE);

		mFirstPageButton.setEnabled(false);
		mFirstPageButton.setVisibility(Button.INVISIBLE);
		
		registerReceiver(this.myWifiReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

		if (NetUtils.getLocalIpAddress() == null)
		{
			showDialog(DIALOG_PROGRESS);
			if (!((WifiManager) getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(true))
			{
				Toast.makeText(this, this.getString(R.string.error_wificonnection), Toast.LENGTH_SHORT).show();

				removeDialog(DIALOG_PROGRESS);
				finish();
			}

		} else
			listTop();

	}



	@Override
	protected void onDestroy()
	{
		if (myWifiReceiver != null)
			unregisterReceiver(myWifiReceiver);

		removeDialog(DIALOG_PROGRESS);

		super.onDestroy();
	}

	private BroadcastReceiver myWifiReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context arg0, Intent intent)
		{
			// Game.LOGGER.log("broadcast1");
			NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
			{
				// Game.LOGGER.log("broadcast2");
				if (networkInfo.isConnected())
				{
					// Game.LOGGER.log("broadcast3");
					if (!mListingTop)
						listTop();

				}
			}
		}
	};

	protected Dialog onCreateDialog(int id)
	{
		Dialog dialog;
		switch (id)
		{
		case DIALOG_NAME:

			removeDialog(DIALOG_NAME);

			dialog = new AlertDialog.Builder(TopActivity.this).setMessage(R.string.username).setView(mEditUsername).setPositiveButton("Ok", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int whichButton)
				{
					String value = Utils.filterName(mEditUsername.getText().toString());
					Settings.PLAYER_NAME = value;

					if (Settings.PLAYER_NAME.equals("Bomber"))
					{
						Toast.makeText(TopActivity.this, TopActivity.this.getString(R.string.error_select_different_username), Toast.LENGTH_SHORT).show();
						TopActivity.this.finish();
						return;
					}

					// Guarda o nome
					SharedPreferences.Editor edit = Settings.GAME_PREFS.edit();
					edit.putString("playerName", Settings.PLAYER_NAME);
					edit.commit();

					listTop();
				}
			}).show();

			dialog.setCancelable(false);
			break;

		case DIALOG_PROGRESS:
			try
			{
				removeDialog(DIALOG_PROGRESS);
				dialog = ProgressDialog.show(TopActivity.this, "", getApplication().getString(R.string.loading_progress), true, true);

				dialog.setOnCancelListener(new OnCancelListener()
				{
					public void onCancel(DialogInterface _dialog)
					{
						finish();
					}
				});

			} catch (Exception e)
			{
				Toast.makeText(getApplication(), getApplication().getString(R.string.error_connecting_to_top_server), Toast.LENGTH_SHORT).show();
				finish();
				dialog = null;
			}
			break;
		default:
			dialog = null;
		}

		return dialog;
	}

	private void listTop()
	{
		mListingTop = true;

		if (Settings.PLAYER_NAME.equals("Bomber"))
		{
			showDialog(DIALOG_NAME);
			return;
		}

		// Verifica o username / mac
		NetUtils.getIMEI(this);

		new CheckUsername().execute();
	}

	private class CheckUsername extends AsyncTask<Void, Void, Boolean>
	{
		@Override
		protected void onPreExecute()
		{
			showDialog(DIALOG_PROGRESS);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean _result)
		{
			if (isCancelled())
			{
				Toast.makeText(getApplication(), getApplication().getString(R.string.error_serverconnection), Toast.LENGTH_SHORT).show();
				finish();
				return;
			}

			if (!_result)
			{
				Toast.makeText(getApplication(), getApplication().getString(R.string.error_select_different_username), Toast.LENGTH_SHORT).show();
				showDialog(DIALOG_NAME);
				return;
			}

			new ListTop().execute();
			super.onPostExecute(_result);
		}

		@Override
		protected Boolean doInBackground(Void... _params)
		{
			try
			{
				mAnswerString = NetUtils.getDBResult("check.php?name=" + Settings.PLAYER_NAME + "&mac=" + NetUtils.getIMEI(TopActivity.this));

				Game.LOGGER.log("Name allowed: " + mAnswerString);
				boolean allowed = Boolean.parseBoolean(mAnswerString);
				return allowed;

			} catch (IOException e)
			{
				e.printStackTrace();
				// Toast.makeText(getApplication(),
				// getApplication().getString(R.string.error_serverconnection),
				// Toast.LENGTH_LONG).show();
				cancel(true);
			}

			return false;
		}

	}

	private class ListTop extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute()
		{
			showDialog(DIALOG_PROGRESS);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void _result)
		{
			if (isCancelled())
			{
				finish();
				return;
			}

			new PresentPage().execute(mPlayerRank / mScoresPerPage);
			removeDialog(DIALOG_PROGRESS);
			super.onPostExecute(_result);
		}

		private String encryptData(String _name, String _mac, long _points)
		{
			StringBuilder sb = new StringBuilder();
			sb.append(_name.replace(";", ""));
			sb.append(";");
			sb.append(_points);
			sb.append(";");
			sb.append(_mac);

			String encoded = Base64Coder.encodeString(sb.toString());

			char char1 = encoded.charAt(0);
			char char2 = encoded.charAt(1);

			sb.setLength(0);
			sb.append(char2);
			sb.append(char1);
			sb.append(encoded.substring(2));
			sb.append('£');
			sb.append(_name.charAt(0));

			encoded = Base64Coder.encodeString(sb.toString());
			encoded = encoded.replace('=', '$');

			// System.out.println(encoded);
			return encoded;
		}

		@Override
		protected Void doInBackground(Void... _params)
		{
			try
			{
				NetUtils.getDBResult("insert15.php?data=" + encryptData(Settings.PLAYER_NAME, NetUtils.getIMEI(TopActivity.this), Settings.GAME_PREFS.getLong("totalPoints", 0)));

				mAnswerString = NetUtils.getDBResult("query.php?name=" + Settings.PLAYER_NAME);

				if (mAnswerString == null)
				{
					cancel(true);
					return null;
				} else
				{
					mSplitedData = mAnswerString.split(";");
					mNumberScoresInServer = Integer.valueOf(mSplitedData[1]);
					mMaxPage = mNumberScoresInServer / mScoresPerPage;
					if (mSplitedData[0].equals("N/A"))
					{
						new PresentPage().execute(0);
						return null;
					}

					// Calcula a página em que o player está e apresenta-a por
					// omissão
					mPlayerRank = Integer.valueOf(mSplitedData[0]);

				}

			} catch (Throwable e)
			{
				e.printStackTrace();
				cancel(true);
			}

			return null;
		}

		@Override
		protected void onCancelled()
		{
			removeDialog(DIALOG_PROGRESS);
			Toast.makeText(getApplication(), getApplication().getString(R.string.error_serverconnection), Toast.LENGTH_LONG).show();

			super.onCancelled();

			finish();
		}
	}

	private class PresentPage extends AsyncTask<Integer, Void, Integer>
	{
		@Override
		protected void onCancelled()
		{
			removeDialog(DIALOG_PROGRESS);
			Toast.makeText(getApplication(), getApplication().getString(R.string.error_serverconnection), Toast.LENGTH_LONG).show();
			super.onCancelled();

			finish();
		}

		@Override
		protected void onPostExecute(Integer _pageNumber)
		{
			if (isCancelled())
			{
				finish();
				return;
			}

			mLayout.removeAllViews();

			boolean isLocalPlayer = false;
			for (String s : mSplitedData)
			{
				try
				{
					isLocalPlayer = false;
					s.replace("\n", "");
					String[] entry = s.split(";");

					Game.LOGGER.log(s);

					View vi = mInflater.inflate(R.layout.top_item, null);
					if (Settings.PLAYER_NAME.equals(entry[2]))
						isLocalPlayer = true;

					TextView text = (TextView) vi.findViewById(R.id.tvTopCountry);
					text.setText(entry[1]);

					if (isLocalPlayer)
					{
						vi.setBackgroundColor(Color.GREEN);
						text.setTextColor(Color.BLACK);
						text.setTypeface(null, Typeface.BOLD);
					}

					text = (TextView) vi.findViewById(R.id.tvTopRank);
					text.setText(entry[0] + "º");
					if (isLocalPlayer)
					{
						text.setBackgroundColor(Color.GREEN);
						text.setTextColor(Color.BLACK);
						text.setTypeface(null, Typeface.BOLD);
					}

					text = (TextView) vi.findViewById(R.id.tvTopName);
					text.setText(entry[2]);
					if (isLocalPlayer)
					{
						text.setBackgroundColor(Color.GREEN);
						text.setTextColor(Color.BLACK);
						text.setTypeface(null, Typeface.BOLD);
					}

					text = (TextView) vi.findViewById(R.id.tvTopScore);
					text.setText(entry[3]);
					if (isLocalPlayer)
					{
						text.setBackgroundColor(Color.GREEN);
						text.setTextColor(Color.BLACK);
						text.setTypeface(null, Typeface.BOLD);
					}

					mLayout.addView(vi);
				} catch (Exception e)
				{
					e.printStackTrace();
					// Toast.makeText(getApplicationContext(),
					// getApplication().getString(R.string.error_connecting_to_top_server),
					// Toast.LENGTH_LONG).show();
					continue;
				}
			}

			mCurrentPage = _pageNumber;

			mLeftButton.setEnabled(!(_pageNumber == 0));
			mLeftButton.setVisibility(_pageNumber == 0 ? Button.INVISIBLE : Button.VISIBLE);

			mRightButton.setEnabled(_pageNumber < mMaxPage);
			mRightButton.setVisibility(_pageNumber == mMaxPage ? Button.INVISIBLE : Button.VISIBLE);

			mFirstPageButton.setEnabled(_pageNumber != 0);
			mFirstPageButton.setVisibility(_pageNumber != 0 ? Button.VISIBLE : Button.INVISIBLE);
			
			removeDialog(DIALOG_PROGRESS);

			super.onPostExecute(_pageNumber);
		}

		@Override
		protected void onPreExecute()
		{
			showDialog(DIALOG_PROGRESS);
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Integer... _params)
		{
			int startRank = _params[0] * mScoresPerPage;
			String query = "query_page.php?startRank=" + startRank + "&endRank=" + mScoresPerPage;
			try
			{
				mAnswerString = NetUtils.getDBResult(query);
				// Game.LOGGER.log("Query:" + query);
				// Game.LOGGER.log("Answer:" + mAnswerString);
				mSplitedData = mAnswerString.split(">");
			} catch (IOException e)
			{
				// Toast.makeText(getApplicationContext(),
				// getApplication().getString(R.string.error_serverconnection),
				// Toast.LENGTH_LONG).show();
				e.printStackTrace();
				cancel(true);
			}

			return _params[0];
		}
	}


	public void onLeftButton(View v)
	{
		new PresentPage().execute(mCurrentPage - 1);
	}

	public void onFirstPage(View v)
	{
		new PresentPage().execute(0);
	}
	public void onRightButton(View v)
	{
		new PresentPage().execute(mCurrentPage + 1);
	}
/*
	private boolean checkWifiConnection()
	{
		// http://stackoverflow.com/questions/1811852/android-if-wifi-is-enabled-and-active-launch-an-intent

		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = null;
		if (cm != null)
		{
			networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		}
		return networkInfo == null ? false : networkInfo.isConnected();
	}*/
	
	
}

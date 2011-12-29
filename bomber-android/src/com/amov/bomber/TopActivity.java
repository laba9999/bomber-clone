package com.amov.bomber;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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

import com.bomber.Settings;
import com.bomber.Game;

public class TopActivity extends GameActivity
{
	static final int DIALOG_NAME = 0;
	static final int DIALOG_PROGRESS = 1;

	private boolean mListingTop = false;
	private final static String mWebhost = "http://bbm.host22.com/";

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

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top);

		mEditUsername = new EditText(this);
		mEditUsername.setText(Settings.PLAYER_NAME);

		mLayout = (LinearLayout) findViewById(R.id.topItemsLayout);
		mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mLeftButton = (Button) findViewById(R.id.buttonTopLeft);
		mRightButton = (Button) findViewById(R.id.ButtonTopRight);

		registerReceiver(this.myWifiReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

		if (!checkWifiConnection())
		{
			if (!((WifiManager) getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(true))
			{
				Toast.makeText(this, this.getString(R.string.error_wificonnection), Toast.LENGTH_SHORT).show();
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

		super.onDestroy();
	}

	private BroadcastReceiver myWifiReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context arg0, Intent arg1)
		{
			NetworkInfo networkInfo = (NetworkInfo) arg1.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
			{
				if (networkInfo.isConnected())
				{
					TopActivity.this.unregisterReceiver(this);
					if (!mListingTop)
						listTop();

					myWifiReceiver = null;
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
			dialog = new AlertDialog.Builder(TopActivity.this).setMessage(R.string.username).setView(mEditUsername).setPositiveButton("Ok", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int whichButton)
				{
					String value = mEditUsername.getText().toString().replace(" ", "");
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
				}
			}).show();

			dialog.setCancelable(false);
			break;

		case DIALOG_PROGRESS:
			dialog = ProgressDialog.show(TopActivity.this, "", this.getString(R.string.loading_top), true, false);
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

		new ListTop().execute();
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
			new PresentPage().execute(mPlayerRank / mScoresPerPage);
			removeDialog(DIALOG_PROGRESS);
			super.onPostExecute(_result);
		}

		@Override
		protected Void doInBackground(Void... _params)
		{
			try
			{
				getDBResult("insert.php?name=" + Settings.PLAYER_NAME + "&highscore=" + Settings.GAME_PREFS.getLong("totalPoints", 0));

				mAnswerString = getDBResult("query.php?name=" + Settings.PLAYER_NAME);

				if (mAnswerString == null)
				{
					Toast.makeText(TopActivity.this, TopActivity.this.getString(R.string.error_serverconnection), Toast.LENGTH_SHORT).show();
					finish();
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
				Toast.makeText(TopActivity.this, TopActivity.this.getString(R.string.error_serverconnection), Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				finish();
			}

			return null;
		}
	}

	private class PresentPage extends AsyncTask<Integer, Void, Integer>
	{
		@Override
		protected void onPostExecute(Integer _pageNumber)
		{
			mLayout.removeAllViews();

			boolean isLocalPlayer = false;
			for (String s : mSplitedData)
			{
				isLocalPlayer = false;
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
			}

			mCurrentPage = _pageNumber;

			mLeftButton.setEnabled(!(_pageNumber == 0));
			mLeftButton.setVisibility(_pageNumber == 0 ? Button.INVISIBLE : Button.VISIBLE);

			mRightButton.setEnabled(_pageNumber < mMaxPage);
			mRightButton.setVisibility(_pageNumber == mMaxPage ? Button.INVISIBLE : Button.VISIBLE);

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
				mAnswerString = getDBResult(query);
				Game.LOGGER.log("Query:" + query);
				Game.LOGGER.log("Answer:" + mAnswerString);
				mSplitedData = mAnswerString.split(">");
			} catch (IOException e)
			{
				Toast.makeText(TopActivity.this, TopActivity.this.getString(R.string.error_serverconnection), Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				finish();
			}

			return _params[0];
		}
	}

	private static String getDBResult(String _url) throws IOException
	{
		// "http://bbm.host22.com/query_page.php?startRank=1&endRank=10"
		URL myURL = new URL(mWebhost + _url);
		Scanner scanner = new Scanner(new BufferedInputStream(myURL.openStream()));

		return scanner.nextLine();
	}

	public void onLeftButton(View v)
	{
		new PresentPage().execute(mCurrentPage - 1);
	}

	public void onRightButton(View v)
	{
		new PresentPage().execute(mCurrentPage + 1);
	}

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
	}
}

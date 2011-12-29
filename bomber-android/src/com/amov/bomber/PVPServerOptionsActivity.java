package com.amov.bomber;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bomber.Settings;
import com.bomber.Game;
import com.bomber.gametypes.GameTypeHandler;
import com.bomber.remote.Protocols;

/*
 * http://www.androidpeople.com/android-gallery-example
 * http://stackoverflow.com/questions/7797641/android-galleryview-recycling
 */

public class PVPServerOptionsActivity extends GameActivity
{
	private static int[] LEVEL_INDICATOR_RESOURCES = { R.id.indicator1, R.id.indicator2, R.id.indicator3, R.id.indicator4, R.id.indicator5, R.id.indicator6, R.id.indicator7, R.id.indicator8, R.id.indicator9 };

	Gallery gallery;
	Spinner mSpinnerNumberRounds;
	Spinner mSpinnerGameType;

	short[] valueGameType = { GameTypeHandler.DEADMATCH, GameTypeHandler.CTF, GameTypeHandler.TEAM_DEADMATCH, GameTypeHandler.TEAM_CTF };
	short[] valueNumberRounds = { 1, 3 };
	String[] valueLevels = { "levelISEC", "level1", "level2", "level3", "level4", "level5", "level6", "level7", "level8" };

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiplayer_server_options);

		gallery = (Gallery) findViewById(R.id.pvpLevelgallery);
		gallery.setAdapter(new ImageAdapter(this));
		gallery.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> _parent, View _v, int _position, long _id)
			{
				// PROXImA ACTIVITY
			}
		});

		gallery.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> _parent, View _arg1, int _position, long _id)
			{
				setIndicatorOn(_position);
			}

			public void onNothingSelected(AdapterView<?> _arg0)
			{

			}
		});

		mSpinnerNumberRounds = (Spinner) this.findViewById(R.id.spinnerNumberRounds);
		ArrayAdapter<String> adapterNumberRounds = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] { "1", "3" });
		adapterNumberRounds.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerNumberRounds.setAdapter(adapterNumberRounds);

		mSpinnerGameType = (Spinner) this.findViewById(R.id.spinnerGameType);
		ArrayAdapter<String> adapterGameType;
		if(Settings.REMOTE_PROTOCOL_IN_USE == Protocols.BLUETOOTH)
			adapterGameType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] { "DEADMATCH 1vs1", "CTF 1vs1" });
		else
			adapterGameType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] { "DEADMATCH 1vs1", "CTF 1vs1", "DEADMATCH 2vs2", "CTF 2vs2" });

		adapterGameType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerGameType.setAdapter(adapterGameType);
	}

	public void onContinueButton(View v)
	{
		Settings.LEVEL_TO_LOAD = valueLevels[gallery.getSelectedItemPosition()];
		Settings.GAME_ROUNDS = valueNumberRounds[mSpinnerNumberRounds.getSelectedItemPosition()];
		Settings.GAME_TYPE = valueGameType[mSpinnerGameType.getSelectedItemPosition()];
		Game.LOGGER.log("Starting game as: " + Settings.GAME_TYPE);

		Intent myIntent = new Intent(this, BuildActivity.class);
		myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(myIntent);
	}

	private void setIndicatorOn(int _position)
	{
		ImageView img;
		Bitmap on = BitmapFactory.decodeResource(getResources(), R.drawable.indicator_on);
		Bitmap off = BitmapFactory.decodeResource(getResources(), R.drawable.indicator_off);

		for (int i = 0; i < valueLevels.length; i++)
		{
			if (i != _position)
			{
				img = (ImageView) findViewById(LEVEL_INDICATOR_RESOURCES[i]);
				img.setImageBitmap(off);
			} else
			{
				img = (ImageView) findViewById(LEVEL_INDICATOR_RESOURCES[i]);
				img.setImageBitmap(on);
			}

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			Intent resultIntent = new Intent();
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
			// desactiva animação na transição entre activities
			overridePendingTransition(0, 0);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public class ImageAdapter extends BaseAdapter
	{
		int mGalleryItemBackground;
		private Context mContext;

		// Adding images.
		private Integer[] Imgid = { R.drawable.tumb_levelisec, R.drawable.tumb_level1, R.drawable.tumb_level2, R.drawable.tumb_level3, R.drawable.tumb_level4, R.drawable.tumb_level5, R.drawable.tumb_level6,
				R.drawable.tumb_level7, R.drawable.tumb_level8 };

		public ImageAdapter(Context c)
		{
			mContext = c;
			TypedArray typArray = obtainStyledAttributes(R.styleable.GalleryTheme);
			mGalleryItemBackground = typArray.getResourceId(R.styleable.GalleryTheme_android_galleryItemBackground, 0);
			typArray.recycle();
		}

		public int getCount()
		{
			return Imgid.length;
		}

		public Object getItem(int position)
		{
			return position;
		}

		public long getItemId(int position)
		{
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{

			if (!(convertView instanceof ImageView))
			{
				ImageView iv = new ImageView(mContext);
				// iv.setLayoutParams(new Gallery.LayoutParams(350, 350));
				 iv.setScaleType(ImageView.ScaleType.FIT_XY);

				iv.setBackgroundResource(mGalleryItemBackground);
				iv.setImageResource(Imgid[position]);
				return iv;
			}

			return convertView;

		}

	}

}

package com.amov.bomber;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.bomber.Settings;
import com.bomber.common.assets.GfxAssets;
import com.bomber.gametypes.GameTypeHandler;

/*
 * http://www.androidpeople.com/android-gallery-example
 * http://stackoverflow.com/questions/7797641/android-galleryview-recycling
 */

public class LevelChooserActivity extends GameActivity
{

	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data)
	{
		gallery.setAdapter(new ImageAdapter(this));
		super.onActivityResult(_requestCode, _resultCode, _data);
	}

	private static int[] LEVEL_INDICATOR_RESOURCES = { R.id.indicator1, R.id.indicator2, R.id.indicator3, R.id.indicator4, R.id.indicator5, R.id.indicator6, R.id.indicator7, R.id.indicator8 };

	Gallery gallery;
	String[] valueLevels = { "level1", "level2", "level3", "level4", "level5", "level6", "level7", "level8" };

	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.levels2);
		
		
		gallery = (Gallery) findViewById(R.id.levelgallery);

		gallery.setAdapter(new ImageAdapter(this));

		gallery.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> _parent, View _v, int _position, long _id)
			{
				if (_position > Settings.GAME_PREFS.getInt("campaignLevelCompleted", 0))
				{
					Toast.makeText(LevelChooserActivity.this, LevelChooserActivity.this.getString(R.string.error_inactive_level), Toast.LENGTH_SHORT).show();
					return;
				}

				// Prepara as settings para o jogo
				Settings.BLUETOOTH_ADAPTER = null;
				Settings.START_ANDROID_AS_SERVER = false;
				Settings.GAME_TYPE = GameTypeHandler.CAMPAIGN;
				Settings.LEVEL_TO_LOAD = valueLevels[_position];

				launchActivity(AndroidGame.class);
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

	}
	

	private void setIndicatorOn(int _position)
	{
		ImageView img;
		Bitmap on = BitmapFactory.decodeResource(getResources(), R.drawable.indicator_on);
		Bitmap off = BitmapFactory.decodeResource(getResources(), R.drawable.indicator_off);

		for (int i = 0; i < 8; i++)
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

	public class ImageAdapter extends BaseAdapter
	{
		int mGalleryItemBackground;
		private Context mContext;

		// Adding images.
		private Integer[] mImgIdActive = { R.drawable.tumb_level1, R.drawable.tumb_level2, R.drawable.tumb_level3, R.drawable.tumb_level4, R.drawable.tumb_level5, R.drawable.tumb_level6,
				R.drawable.tumb_level7, R.drawable.tumb_level8 };
		private Integer[] mImgIdInactive = { R.drawable.tumb_ina_level1, R.drawable.tumb_ina_level2, R.drawable.tumb_ina_level3, R.drawable.tumb_ina_level4, R.drawable.tumb_ina_level5,
				R.drawable.tumb_ina_level6, R.drawable.tumb_ina_level7, R.drawable.tumb_ina_level8 };

		public ImageAdapter(Context c)
		{
			mContext = c;
			TypedArray typArray = obtainStyledAttributes(R.styleable.GalleryTheme);
			mGalleryItemBackground = typArray.getResourceId(R.styleable.GalleryTheme_android_galleryItemBackground, 0);
			typArray.recycle();
		}

		public int getCount()
		{
			return mImgIdActive.length;
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
				iv.setBackgroundResource(mGalleryItemBackground);
				if (position <= Settings.GAME_PREFS.getInt("campaignLevelCompleted", 0))
					iv.setImageResource(mImgIdActive[position]);
				else
					iv.setImageResource(mImgIdInactive[position]);

				return iv;
			}

			return convertView;
		}
	}
}

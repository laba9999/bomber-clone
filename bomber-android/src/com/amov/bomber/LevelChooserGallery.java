package com.amov.bomber;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

/*
 * http://www.androidpeople.com/android-gallery-example
 * http://stackoverflow.com/questions/7797641/android-galleryview-recycling
 */

public class LevelChooserGallery extends Activity
{
	Gallery gallery;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.levels2);

		gallery = (Gallery) findViewById(R.id.levelgallery);

		gallery.onFling(null, null, 30, 0);

		gallery.setAdapter(new AddImgAdp(this));

		gallery.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView _parent, View _v, int _position, long _id)
			{
					//PROXImA ACTIVITY
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
		// Este método baseia-se no facto dos IDs dos elementos indicator
		// estarem definidos de forma seguida e ordenada na classe R

		ImageView img;
		Bitmap on = BitmapFactory.decodeResource(getResources(), R.drawable.indicator_on);
		Bitmap off = BitmapFactory.decodeResource(getResources(), R.drawable.indicator_off);

		for (int i = 0; i < 8; i++)
		{
			if (i != _position)
			{
				img = (ImageView) findViewById(R.id.indicator1 + i);
				img.setImageBitmap(off);
			} else
			{
				img = (ImageView) findViewById(R.id.indicator1 + i);
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

	public class AddImgAdp extends BaseAdapter
	{
		int mGalleryItemBackground;
		private Context mContext;

		// Adding images.
		private Integer[] Imgid = { R.drawable.placeholder, R.drawable.placeholder2, R.drawable.placeholder, R.drawable.placeholder2, R.drawable.placeholder, R.drawable.placeholder2,
				R.drawable.placeholder, R.drawable.placeholder2 };

		public AddImgAdp(Context c)
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
				// iv.setScaleType(ImageView.ScaleType.FIT_XY);
				// iv.setBackgroundResource(mGalleryItemBackground);
				iv.setImageResource(Imgid[position]);
				return iv;
			}

			return convertView;

		}

	}

}

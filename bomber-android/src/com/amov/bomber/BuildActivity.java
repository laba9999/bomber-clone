package com.amov.bomber;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BuildActivity extends Activity
{
	private static final int[] EXPLOSION_INDICATORS_RESOURCES = {R.id.imageBuildExplosion1,
																R.id.imageBuildExplosion2,
																R.id.imageBuildExplosion3};

	private static final int[] BOMBS_INDICATORS_RESOURCES = {R.id.imageBuildBombs1,
																R.id.imageBuildBombs2,
																R.id.imageBuildBombs3};

	private static final int[] SPEED_INDICATORS_RESOURCES = {R.id.imageBuildSpeed1,
																R.id.imageBuildSpeed2,
																R.id.imageBuildSpeed3};

	int mMaxPoints = 5;
	Integer mAvailablePoints = 5;
	int mExplosionPoints = 0;
	int mBombsPoints = 0;
	int mSpeedPoints = 0;
	
	TextView mTextAvailablePoints;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.build);		

		mTextAvailablePoints = (TextView) findViewById(R.id.textBuildAvailablePointsValue);
		mTextAvailablePoints.setText(mAvailablePoints.toString());			

	}
	
	public void onExplosionPlusButton(View v)
	{
		if(mAvailablePoints > 0 && mExplosionPoints < 3)
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
		if(mExplosionPoints > 0)
		{			
			mExplosionPoints --;			
			ImageView indicator = (ImageView) findViewById(EXPLOSION_INDICATORS_RESOURCES[mExplosionPoints]);
			
			Bitmap off = BitmapFactory.decodeResource(getResources(), R.drawable.indicator_off);
			indicator.setImageBitmap(off);				
			mAvailablePoints++;
			mTextAvailablePoints.setText(mAvailablePoints.toString());
			
		}
	}
	
	public void onBombsPlusButton(View v)
	{
		if(mAvailablePoints > 0 && mBombsPoints < 3)
		{			
			ImageView indicator = (ImageView) findViewById(BOMBS_INDICATORS_RESOURCES[mBombsPoints]);
			mBombsPoints++;
			
			Bitmap on = BitmapFactory.decodeResource(getResources(), R.drawable.indicator_on);
			indicator.setImageBitmap(on);					
			mAvailablePoints--;
			mTextAvailablePoints.setText(mAvailablePoints.toString());
		}
	}
	
	public void onBombsMinusButton(View v)
	{
		if(mBombsPoints > 0)
		{			
			mBombsPoints --;			
			ImageView indicator = (ImageView) findViewById(BOMBS_INDICATORS_RESOURCES[mBombsPoints]);
			
			Bitmap off = BitmapFactory.decodeResource(getResources(), R.drawable.indicator_off);
			indicator.setImageBitmap(off);							
			mAvailablePoints++;
			mTextAvailablePoints.setText(mAvailablePoints.toString());
			
		}
	}
	
	public void onSpeedPlusButton(View v)
	{
		if(mAvailablePoints > 0 && mSpeedPoints < 3)
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
		if(mSpeedPoints > 0)
		{			
			mSpeedPoints --;			
			ImageView indicator = (ImageView) findViewById(SPEED_INDICATORS_RESOURCES[mSpeedPoints]);
			
			Bitmap off = BitmapFactory.decodeResource(getResources(), R.drawable.indicator_off);
			indicator.setImageBitmap(off);							
			mAvailablePoints++;
			mTextAvailablePoints.setText(mAvailablePoints.toString());			
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
	
}

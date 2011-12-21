package com.amov.bomber;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity
{
	public static final int DIALOG_MULTIPLAYER = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	}

	public void onStartButton(View v)
	{
		Intent myIntent = new Intent(this, AndroidGame.class);
		startActivityForResult(myIntent, 0);
	}

	public void onArcadeButton(View v)
	{
		Intent myIntent = new Intent(this, LevelChooserActivity.class);
		// proibe a animação na transição entre activities
		myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(myIntent, 0);
	}

	public void onMultiplayerButton(View v)
	{
		Intent myIntent = new Intent(this, MultiplayerActivity.class);
		// proibe a animação na transição entre activities
		myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(myIntent, 0);	}
	
	public void onTopButton(View v)
	{
		Intent myIntent = new Intent(this, TopActivity.class);
		// proibe a animação na transição entre activities
		myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(myIntent, 0);
	}

	public void onAchievementsButton(View v)
	{
		Intent myIntent = new Intent(this, AchievementsActivity.class);
		// proibe a animação na transição entre activities
		myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(myIntent, 0);
	}

	
}

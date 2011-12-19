package com.amov.bomber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

	}

	public void onStartButton(View v) {
		Intent myIntent = new Intent(this, AndroidGame.class);
		startActivityForResult(myIntent, 0);
	}

	public void onArcadeButton(View v) {
		Intent myIntent = new Intent(this, LevelChooserGallery.class);
		//proibe a animação na transição entre activities
		myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(myIntent, 0);
	}
}

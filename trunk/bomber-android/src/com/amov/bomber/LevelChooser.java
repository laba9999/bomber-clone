package com.amov.bomber;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.HorizontalScrollView;

/*
 * http://www.androidpeople.com/android-gallery-example
 * http://stackoverflow.com/questions/7797641/android-galleryview-recycling
 */
public class LevelChooser extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.levels);

		HorizontalScrollView sv = (HorizontalScrollView) findViewById(R.id.levelsScrollView);

		sv.setBackgroundColor(Color.WHITE);


	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent resultIntent = new Intent();
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
			//desactiva animação na transição entre activities
			overridePendingTransition(0, 0);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

}

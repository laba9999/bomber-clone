package com.amov.bomber;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.bomber.common.Achievements;

public class AchievementsActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.achievements);
		
		Achievements.loadFile();
		
		//actualiza progressos
		TextView tv = (TextView) findViewById(R.id.textAchievementMonsterKillsProgress);
		tv.setText(new String(Achievements.mNumberMonsterKills + " / " + Achievements.MONSTER_KILLS_GOAL));
		
		tv = (TextView) findViewById(R.id.textAchievementPlayerKillsProgress);
		tv.setText(new String(Achievements.mNumberPlayersKills + " / " + Achievements.PLAYER_KILLS_GOAL));
		
		tv = (TextView) findViewById(R.id.textAchievementDMWinsProgress);
		tv.setText(new String(Achievements.mNumberDMWins + " / " + Achievements.DM_WINS_GOAL));
		
		tv = (TextView) findViewById(R.id.textAchievementCTFWinsProgress);
		tv.setText(new String(Achievements.mNumberCTFWins + " / " + Achievements.CTF_WINS_GOAL));
		
		tv = (TextView) findViewById(R.id.textAchievementTimePlayedProgress);
		
		Date actual = new Date(Achievements.mTotalTimePlayed);
		Date goal = new Date(Achievements.TIME_PLAYED_GOAL);
		tv.setText(new String(actual.getMinutes() + "m / " + goal.getMinutes() + "m"));
				
		tv = (TextView) findViewById(R.id.textAchievementCompletedCampaignProgress);
		if(Achievements.mHasCompletedCampaign)
			tv.setText(this.getString(R.string.yes));
		else
			tv.setText(this.getString(R.string.no));

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

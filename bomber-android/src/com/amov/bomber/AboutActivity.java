package com.amov.bomber;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends GameActivity
{
	TextView tvVersion;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if(mGoneBackToAssetsLoader)
		{
			finish();
			return;
		}
		setContentView(R.layout.about);
		
		tvVersion = (TextView)findViewById(R.id.textViewVersion);
		
		String version;
		try
		{
			version = getPackageManager().getPackageInfo("com.amov.bomber", 0).versionName;
		} catch (NameNotFoundException e)
		{
			version = "N/A";
			e.printStackTrace();
		}
		
		tvVersion.setText("v"+version);
	}
}

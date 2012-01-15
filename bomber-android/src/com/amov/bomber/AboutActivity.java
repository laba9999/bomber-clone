package com.amov.bomber;

import android.os.Bundle;

public class AboutActivity extends GameActivity
{
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
	}
}

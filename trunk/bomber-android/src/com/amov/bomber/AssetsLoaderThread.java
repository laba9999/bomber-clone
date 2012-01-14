package com.amov.bomber;

import com.bomber.common.assets.SoundAssets;

public class AssetsLoaderThread extends Thread
{

	@Override
	public void run()
	{
		SoundAssets.load();
		super.run();
	}
}

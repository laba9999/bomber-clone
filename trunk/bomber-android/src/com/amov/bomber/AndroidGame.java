package com.amov.bomber;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager.LayoutParams;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.bomber.AndroidBridge;
import com.bomber.DebugSettings;
import com.bomber.Game;
import com.bomber.gametypes.GameType;
import com.bomber.remote.RemoteConnections;

public class AndroidGame extends AndroidApplication implements AndroidBridge{


    @Override
    protected void onDestroy()
    {
            super.onDestroy();
            Game.mRemoteConnections.closeAll("Aplication exited!");
            
            // Para grandes males grandes remédios...
            //System.exit(1);
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);

		super.onCreate(savedInstanceState);
		initialize(new Game(this,RemoteConnections.create(GameType.CTF, DebugSettings.START_ANDROID_AS_SERVER, DebugSettings.REMOTE_SERVER_ADDRESS, DebugSettings.REMOTE_SERVER_PORT)), false);
	}	

	public void goBackToMenu() {
		finishActivity(0);
		
		this.exit();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	   if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	        // Tira o efeito ao botão back
	        return true;
	    }

	    return super.onKeyDown(keyCode, event);
	}
}
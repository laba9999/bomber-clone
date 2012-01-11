package com.bomber;

import java.io.File;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.bomber.gametypes.GameTypeHandler;
import com.bomber.remote.Protocols;

public class Settings {

	public static boolean LIMPAR_SARAMPO = false;
	// Remote
	public static String WEBHOST_ADDRESS = "http://bbm.host22.com/";
	public static boolean START_ANDROID_AS_SERVER = false;
	public static boolean START_DESKTOP_AS_SERVER = false;
	public static short REMOTE_PROTOCOL_IN_USE = Protocols.TCP;
	public static String REMOTE_SERVER_ADDRESS = "192.168.142.141:50005";


	// Alterar este valor não tem efeito nenhum
	public static String LOCAL_SERVER_ADDRESS = "192.168.1.110:50005";

	// Bluetooth
	public static BluetoothAdapter BLUETOOTH_ADAPTER;
	public static final UUID APP_UUID = new UUID(4587L, 0215L);

	// Game
	public static boolean STARTED_FROM_DESKTOP = true;
	public static String LEVEL_TO_LOAD = "level8";
	public static short GAME_ROUNDS = 1;
	public static final short GAME_COUNTDOWN_SECONDS = 5;
	public static short GAME_TYPE = GameTypeHandler.DEADMATCH;
	public static SharedPreferences GAME_PREFS = null;
	public static boolean PLAYING_ONLINE = false;
	
	// Players
	public static final boolean PLAYER_DIE_WITH_EXPLOSIONS = true;
	public static String PLAYER_NAME = "zezao1";

	// Monstros
	public static final boolean MONSTERS_KILL_PLAYERS = true;

	// Mapa
	public static final boolean MAP_LOAD_DESTROYABLE_TILES = true;

	// Mundo

	// UI
	public static final boolean UI_DRAW_FPS = true;
	public static final boolean UI_DRAW_INPUT_ZONES = false;

	public static void loadPreferences(SharedPreferences _prefs)
	{
		GAME_PREFS = _prefs;
				
		if (GAME_PREFS == null)
			throw new NullPointerException();		
		
		
		if (!GAME_PREFS.contains("soundEnabled"))
		{
			// Valores por defeito
			SharedPreferences.Editor editor = GAME_PREFS.edit();

			editor.putBoolean("soundEnabled", true);
			editor.putString("playerName", "Bomber");

			editor.putInt("campaignLevelCompleted", 0);

			editor.putLong("totalPoints", 0);
			
			editor.putInt("buildExplosionSize", 0);
			editor.putInt("buildBombCount", 0);
			editor.putInt("buildSpeed", 0);
			editor.commit();
		}
		
		int levelFoldersExisting = getNumberOfLevelsFolderExisting();
		
		if(GAME_PREFS.getInt("campaignLevelCompleted", 0) < levelFoldersExisting)
		{
			SharedPreferences.Editor editor = GAME_PREFS.edit();
			editor.putInt("campaignLevelCompleted", levelFoldersExisting);
			editor.commit();
			
		}
	
	}
	
	
	private static int getNumberOfLevelsFolderExisting()
	{
		//caso as shares prefs se tenham perdido
		//uma forma de recuperar os niveis é verificar as pastas que estão criadas

		int ret = 0;
		
		final String[] valueLevels = { "level1", "level2", "level3", "level4", "level5", "level6", "level7", "level8" };
		
		File externalStorage = Environment.getExternalStorageDirectory();		
		
		for(int i = 0;i < valueLevels.length; i++)
		{
			File f = new File(externalStorage + "/com.amov.bomber/levels/" + valueLevels[i]);
			
			if(f.exists() && f.isDirectory())
			{
				ret++;
			}
			else
			{
				break;
			}
		}
		
		return ret;
	}
	
	
	public static void addPlayerPoints(int _points)
	{
		if(STARTED_FROM_DESKTOP)
			return;
		
		long totalPoints = GAME_PREFS.getLong("totalPoints", 0);
		totalPoints += _points;
		
		SharedPreferences.Editor edit = GAME_PREFS.edit();
		edit.putLong("totalPoints", totalPoints);
		edit.commit();
	}
}

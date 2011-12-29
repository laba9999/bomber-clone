package com.bomber;

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.content.SharedPreferences;

import com.bomber.gametypes.GameTypeHandler;
import com.bomber.remote.Protocols;

public class DebugSettings {

	public static boolean LIMPAR_SARAMPO = false;
	// Remote
	
	public static boolean START_ANDROID_AS_SERVER = false;
	public static boolean START_DESKTOP_AS_SERVER = false;
	public static short REMOTE_PROTOCOL_IN_USE = Protocols.TCP;
	public static String REMOTE_SERVER_ADDRESS = "192.168.140.198:50005";


	// Alterar este valor não tem efeito nenhum
	public static String LOCAL_SERVER_ADDRESS = "192.168.1.110:50005";

	// Bluetooth
	public static BluetoothAdapter BLUETOOTH_ADAPTER;
	public static final UUID APP_UUID = new UUID(4587L, 0215L);

	// Game
	public static boolean STARTED_FROM_DESKTOP = true;
	public static String LEVEL_TO_LOAD = "level4";
	public static short GAME_ROUNDS = 1;
	public static final short GAME_COUNTDOWN_SECONDS = 5;
	public static short GAME_TYPE = GameTypeHandler.CAMPAIGN;
	public static SharedPreferences GAME_PREFS = null;
	// Players
	public static final boolean PLAYER_DIE_WITH_EXPLOSIONS = false;
	public static String PLAYER_NAME = "zezao1";

	// Monstros
	public static final boolean MONSTERS_KILL_PLAYERS = false;

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

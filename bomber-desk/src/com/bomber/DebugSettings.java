package com.bomber;

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;

import com.bomber.gametypes.GameTypeHandler;
import com.bomber.remote.Protocols;

public class DebugSettings {
	// Remote
	public static  boolean START_ANDROID_AS_SERVER = false;
	public static  boolean START_DESKTOP_AS_SERVER = false;
	public static  short REMOTE_PROTOCOL_IN_USE = Protocols.UDP;
	public static  String REMOTE_SERVER_ADDRESS = "192.168.1.110:50005";

	// Bluetooth
	public static BluetoothAdapter BLUETOOTH_ADAPTER;
	public static final UUID APP_UUID = new UUID(4587L, 0215L);
	
	// Game
	public static  String LEVEL_TO_LOAD = "level1";
	public static  short GAME_TYPE = GameTypeHandler.CAMPAIGN;
	public static  short GAME_ROUNDS = 3;
	public static final short GAME_COUNTDOWN_SECONDS = 5;
	
	// Players
	public static final boolean PLAYER_DIE_WITH_EXPLOSIONS = true;

	// Monstros
	public static final boolean MONSTERS_KILL_PLAYERS = false;

	// Mapa
	public static final boolean MAP_LOAD_DESTROYABLE_TILES = true;

	// Mundo

	// UI
	public static final boolean UI_DRAW_FPS = true;
	public static final boolean UI_DRAW_INPUT_ZONES = false;

}

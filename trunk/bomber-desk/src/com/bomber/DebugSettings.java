package com.bomber;

import com.bomber.gametypes.GameTypeHandler;
import com.bomber.remote.Protocols;

public class DebugSettings {

	// Remote
	public static final boolean START_ANDROID_AS_SERVER = true;
	public static final boolean START_DESKTOP_AS_SERVER = false;
	public static final short REMOTE_PROTOCOL_IN_USE = Protocols.TCP;
	public static final String REMOTE_SERVER_ADDRESS = "192.168.1.110:50005";

	// Game
	public static final String LEVEL_TO_LOAD = "level1";
	public static final short GAME_TYPE = GameTypeHandler.TEAM_CTF;
	public static final short GAME_COUNTDOWN_SECONDS = 5;
	
	// Players
	public static final boolean PLAYER_DIE_WITH_EXPLOSIONS = false;

	// Monstros
	public static final boolean MONSTERS_KILL_PLAYERS = false;

	// Mapa
	public static final boolean MAP_LOAD_DESTROYABLE_TILES = true;

	// Mundo

	// UI
	public static final boolean UI_DRAW_FPS = true;
	public static final boolean UI_DRAW_INPUT_ZONES = false;

}

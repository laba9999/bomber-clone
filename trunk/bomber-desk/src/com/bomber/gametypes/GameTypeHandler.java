package com.bomber.gametypes;

import com.bomber.gameobjects.Player;
import com.bomber.world.GameWorld;

public abstract class GameTypeHandler {	
	
	public GameWorld mGameWorld = null;
	public boolean mNeedsPortal = false;
	
	public GameTypeHandler() {

	}
	
	/*
	 * Usado para verificar se pode spawnar portal.
	 */
	public abstract boolean isObjectiveAcomplished();

	/*
	 * Usado para verificar se pode mudar para o estado Win
	 */
	public abstract boolean isOver();
	public abstract boolean isLost();
	public abstract boolean onPlayerKill(Player _player);
	
	/**
	 *  Quando este método é chamado o player já foi morto e as suas vidas foram colocadas a 0.
	 * @param _player
	 */
	public abstract void onPlayerDisconnect(Player _player);
	
	public static final short CAMPAIGN = 0;
	public static final short CTF = 1;
	public static final short DEADMATCH = 2;
	public static final short TEAM_CTF = 3;
	public static final short TEAM_DEADMATCH = 4;
}
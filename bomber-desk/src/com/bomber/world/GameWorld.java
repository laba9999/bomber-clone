package com.bomber.world;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.bomber.Team;
import com.bomber.common.ObjectsPool;
import com.bomber.gameobjects.Bomb;
import com.bomber.gameobjects.Drawable;
import com.bomber.gameobjects.Monster;
import com.bomber.gameobjects.Player;
import com.bomber.gameobjects.bonus.Bonus;
import com.bomber.remote.Message;
import com.bomber.remote.RemoteConnections;

public class GameWorld {
	private ObjectsPool<Monster> mMonsters;
	private ObjectsPool<Bonus> mSpawnedBonus;
	private ObjectsPool<Player> mPlayers;
	private ObjectsPool<Drawable> mExplosions;
	private short mGameType;
	private boolean mIsPVPGame;
	public GameMap mMap;
	public RemoteConnections mRemoteConns;
	public ArrayList<Team> mTeams = new ArrayList<Team>();
	public Clock mClock;

	/**
	 * Chamado pelas bombas. Obtem o tamanho que a explosão terá para cada uma
	 * das direcções baseada no mExplosionSize da bomba e na distância aos tiles
	 * do tipo COLLIDABLE ou DESTROYABLE obtida atraves do getDistanceToNext da
	 * classe GameMap.
	 * 
	 * Cria os objecto drawable baseado nos limites calculados, um por cada um
	 * dos componentes da explosão. Verifica se não há jogadores, monstros ou
	 * tiles afetcados pela explosão.
	 */
	public void createExplosion(Bomb _bomb)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Chamado pelos players Utiliza o getNearestMap da classe GameMap para
	 * obter o tile onde a bomba deverá ser colocada. Adiciona a bomba ao mapa.
	 */
	public void addBomb(short _bombPower, Vector2 _playerPosition,
			short _playerDirection)
	{
		throw new UnsupportedOperationException();
	}

	public void update()
	{
		throw new UnsupportedOperationException();
	}

	public void parseGameMessage(Message _msg)
	{
		throw new UnsupportedOperationException();
	}

	public void parsePlayerMessage(Message _msg)
	{
		throw new UnsupportedOperationException();
	}

	public void parseBombMessage(Message _msg)
	{
		throw new UnsupportedOperationException();
	}

	public void parseMonsterMessage(Message _msg)
	{
		throw new UnsupportedOperationException();
	}

	public void parseBonusMessage(Message _msg)
	{
		throw new UnsupportedOperationException();
	}

	public void parsePointsMessage(Message _msg)
	{
		throw new UnsupportedOperationException();
	}

	public void parseClockMessage(Message _msg)
	{
		throw new UnsupportedOperationException();
	}
}
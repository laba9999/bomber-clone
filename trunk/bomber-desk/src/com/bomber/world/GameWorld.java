package com.bomber.world;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bomber.GameType;
import com.bomber.Team;
import com.bomber.common.Assets;
import com.bomber.common.Directions;
import com.bomber.common.ObjectFactory;
import com.bomber.common.ObjectsPool;
import com.bomber.gameobjects.Bomb;
import com.bomber.gameobjects.Drawable;
import com.bomber.gameobjects.MovableObject;
import com.bomber.gameobjects.Player;
import com.bomber.gameobjects.Tile;
import com.bomber.gameobjects.bonus.Bonus;
import com.bomber.gameobjects.bonus.BonusTypes;
import com.bomber.gameobjects.monsters.Monster;
import com.bomber.gameobjects.monsters.MonsterInfo;
import com.bomber.remote.Message;
import com.bomber.remote.RemoteConnections;

public class GameWorld {
	public ObjectsPool<Monster> mMonsters;
	public ObjectsPool<Bonus> mSpawnedBonus;
	public ObjectsPool<Player> mPlayers;
	public ObjectsPool<Bomb> mBombs;
	public ObjectsPool<Drawable> mExplosions;

	private Player mLocalPlayer = null;

	private short mGameType;
	public boolean mIsPVPGame;
	public GameMap mMap;
	public RemoteConnections mRemoteConns;
	public ArrayList<Team> mTeams = new ArrayList<Team>();
	public Clock mClock;

	private String mCurrentLevelName;

	public GameWorld(short _gameType, String _starLevelName) {

		mGameType = _gameType;

		// Os bonus têm que ser adicionados manualmente porque existem vários
		// tipos
		mSpawnedBonus = new ObjectsPool<Bonus>((short) 0, null);

		// TODO : delete
		spawnBonus(BonusTypes.BOMB_POWER, (short) 5, (short) 6);
		spawnBonus(BonusTypes.BOMB_COUNT, (short) 6, (short) 6);
		spawnBonus(BonusTypes.DOUBLE_POINTS, (short) 7, (short) 6);
		spawnBonus(BonusTypes.LIFE, (short) 8, (short) 6);
		spawnBonus(BonusTypes.PUSH, (short) 9, (short) 6);
		spawnBonus(BonusTypes.SHIELD, (short) 10, (short) 6);
		spawnBonus(BonusTypes.SPEED, (short) 11, (short) 6);
		mMonsters = new ObjectsPool<Monster>((short) 5, new ObjectFactory.CreateMonster(this));

		// O numero de players vai variar consoante o tipo de jogo
		short nPlayers = 4;
		if (_gameType == GameType.CAMPAIGN)
			nPlayers = 1;
		else if (_gameType == GameType.CTF || _gameType == GameType.DEADMATCH)
			nPlayers = 2;

		mPlayers = new ObjectsPool<Player>((short) nPlayers, new ObjectFactory.CreatePlayer(this));

		// Assumimos que cada player vai poder colocar em termos médios 2 bombas
		// de cada vez
		short nBombs = (short) (2 * nPlayers);
		mBombs = new ObjectsPool<Bomb>(nBombs, new ObjectFactory.CreateBomb(this));

		// Assumimos que cada bomba vai ter um poder de explosão médio de 3,
		// como são 4 direcções possiveis temos 12 "explosões" por bomba.
		short nExplosions = (short) (12 * nBombs);

		mExplosions = new ObjectsPool<Drawable>(nExplosions, new ObjectFactory.CreateExplosion());

		mMap = new GameMap();

		// Lê o nivel
		mCurrentLevelName = _starLevelName;
		Level.loadLevel(_starLevelName, this, nPlayers);
	}

	public Player getLocalPlayer()
	{
		if (mLocalPlayer != null)
			return mLocalPlayer;

		for (Player p : mPlayers)
			if (p.mColor == Player.WHITE)
			{
				mLocalPlayer = p;
				break;
			}

		return mLocalPlayer;
	}

	public void reset()
	{
		mMonsters.clear();
		mSpawnedBonus.clear();
		mBombs.clear();
		mExplosions.clear();
	}

	public void spawnMonster(String _type, short _line, short _col)
	{
		Monster tmpMonster = mMonsters.getFreeObject();

		tmpMonster.setMovableAnimations(Assets.mMonsters.get(_type));

		tmpMonster.mPlayAnimation = true;

		tmpMonster.mPosition.x = _col * Tile.TILE_SIZE + Tile.TILE_SIZE_HALF;
		tmpMonster.mPosition.y = _line * Tile.TILE_SIZE + Tile.TILE_SIZE_HALF;

		tmpMonster.mInfo = MonsterInfo.getInfoFromType(_type);
	}

	public void spawnPlayer(String _type, short _line, short _col)
	{
		Player tmpPlayer = mPlayers.getFreeObject();

		tmpPlayer.setMovableAnimations(Assets.mPlayers.get(_type));

		tmpPlayer.mPosition.x = _col * Tile.TILE_SIZE + Tile.TILE_SIZE_HALF;
		tmpPlayer.mPosition.y = _line * Tile.TILE_SIZE + Tile.TILE_SIZE_HALF;
	}

	public void spawnBonus(short _type, short _line, short _col)
	{
		Bonus tmpBonus = ObjectFactory.CreateBonus.create(_type);
		String animationKey = BonusTypes.getAnimationKeyFromType(_type);
		Animation anim = Assets.mBonus.get(animationKey);

		mSpawnedBonus.addObject(tmpBonus);
		tmpBonus.setCurrentAnimation(anim, Bonus.NUMBER_OF_ANIMATION_FRAMES, true, true);
		tmpBonus.mPosition.x = _col * Tile.TILE_SIZE + Tile.TILE_SIZE_HALF;
		tmpBonus.mPosition.y = _line * Tile.TILE_SIZE + Tile.TILE_SIZE_HALF;

	}

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
		// Remove a bomba da pool de bombas activas
		mBombs.releaseObject(_bomb);

		// Indica que o tile já não contém uma bomba
		_bomb.mContainer.mContainsBomb = false;

		// Cria os desenhos da explosão
		createExplosionComponents(_bomb);

	}

	private void explodeObjects(Tile _tile)
	{
		int idx;

		// Verifica os monstros
		for (Monster m : mMonsters)
		{
			idx = mMap.calcTileIndex(m.mPosition);
			if (idx == _tile.mPositionInArray)
				m.kill();
		}

		// Verifica outras bombas
		for (Bomb b : mBombs)
			if (b.mContainer.mPositionInArray == _tile.mPositionInArray)
				b.kill();

		// TODO : descomentar
		// Verifica os players
//		for (Player p : mPlayers)
//		{
//			idx = mMap.calcTileIndex(p.mPosition);
//			if (idx == _tile.mPositionInArray)
//				p.kill();
//		}
	}

	private void createExplosionComponents(Bomb _bomb)
	{
		// Adiciona a explosão central
		Drawable tmpExplosion = mExplosions.getFreeObject();
		tmpExplosion.setCurrentAnimation(Assets.mExplosions.get("xplode_center"), (short) 4, true, false);

		Tile tmpTile = mMap.getTile(_bomb.mContainer.mPosition);
		tmpExplosion.mPosition.x = tmpTile.mPosition.x + Tile.TILE_SIZE_HALF;
		tmpExplosion.mPosition.y = tmpTile.mPosition.y + Tile.TILE_SIZE_HALF;

		// Limpa o sarampo ao que estiver neste tile
		explodeObjects(tmpTile);

		//
		// Para cada um dos lados, calcula o tamanho da explosão e adiciona
		int startIdx = _bomb.mContainer.mPositionInArray;
		for (short i = Directions.UP; i <= Directions.RIGHT; i++)
		{
			int distance = mMap.getDistanceToNext(_bomb.mBombPower, _bomb.mPosition, i, Tile.COLLIDABLE, Tile.DESTROYABLE);
			if (distance >= 0 && distance <= _bomb.mBombPower)
			{
				//
				// Foi encontrado um tile..

				// Obtem o tile
				tmpTile = mMap.getTile(startIdx, i, (short) distance);

				// Se o tile é destrutivel, explode-o
				if (tmpTile.mType == Tile.DESTROYABLE)
					mMap.explodeTile(tmpTile);

				// Cria os restantes componentes da explosão, não incluindo o
				// central e o tile encontrado
				for (short c = 1; c < distance; c++)
				{
					tmpTile = mMap.getTile(startIdx, i, c);
					tmpExplosion = mExplosions.getFreeObject();
					tmpExplosion.mPosition.x = tmpTile.mPosition.x + Tile.TILE_SIZE_HALF;
					tmpExplosion.mPosition.y = tmpTile.mPosition.y + Tile.TILE_SIZE_HALF;

					// Limpa o sarampo ao que estiver neste tile
					explodeObjects(tmpTile);

					if (i == Directions.LEFT || i == Directions.RIGHT)
						tmpExplosion.setCurrentAnimation(Assets.mExplosions.get("xplode_mid_hor"), (short) 4, true, false);
					else
						tmpExplosion.setCurrentAnimation(Assets.mExplosions.get("xplode_mid_ver"), (short) 4, true, false);

				}
			} else
			{
				//
				// Não foi encontrado um tile

				// Cria os restantes componentes da explosão, não incluindo o
				// central e a ponta
				for (short c = 1; c < _bomb.mBombPower; c++)
				{
					tmpTile = mMap.getTile(startIdx, i, c);
					tmpExplosion = mExplosions.getFreeObject();
					tmpExplosion.mPosition.x = tmpTile.mPosition.x + Tile.TILE_SIZE_HALF;
					tmpExplosion.mPosition.y = tmpTile.mPosition.y + Tile.TILE_SIZE_HALF;
					explodeObjects(tmpTile);

					if (i == Directions.LEFT || i == Directions.RIGHT)
						tmpExplosion.setCurrentAnimation(Assets.mExplosions.get("xplode_mid_hor"), (short) 4, true, false);
					else
						tmpExplosion.setCurrentAnimation(Assets.mExplosions.get("xplode_mid_ver"), (short) 4, true, false);

				}

				tmpTile = mMap.getTile(startIdx, i, _bomb.mBombPower);
				tmpExplosion = mExplosions.getFreeObject();
				tmpExplosion.mPosition.x = tmpTile.mPosition.x + Tile.TILE_SIZE_HALF;
				tmpExplosion.mPosition.y = tmpTile.mPosition.y + Tile.TILE_SIZE_HALF;

				// Limpa o sarampo ao que estiver neste tile
				explodeObjects(tmpTile);

				// Adiciona a ponta
				if (i == Directions.LEFT)
					tmpExplosion.setCurrentAnimation(Assets.mExplosions.get("xplode_tip_left"), (short) 4, true, false);
				else if (i == Directions.RIGHT)
					tmpExplosion.setCurrentAnimation(Assets.mExplosions.get("xplode_tip_right"), (short) 4, true, false);
				else if (i == Directions.UP)
					tmpExplosion.setCurrentAnimation(Assets.mExplosions.get("xplode_tip_up"), (short) 4, true, false);
				else if (i == Directions.DOWN)
					tmpExplosion.setCurrentAnimation(Assets.mExplosions.get("xplode_tip_down"), (short) 4, true, false);
			}
		}
	}

	/**
	 * Chamado pelos players Utiliza o getNearestMap da classe GameMap para
	 * obter o tile onde a bomba deverá ser colocada. Adiciona a bomba ao mapa.
	 */
	public void spawnBomb(short _bombPower, Vector2 _playerPosition)
	{
		Tile tmpTile = mMap.getTile(_playerPosition);
		if (tmpTile.mContainsBomb)
			return;

		Bomb tmpBomb = mBombs.getFreeObject();

		tmpTile.mContainsBomb = true;
		tmpBomb.mContainer = tmpTile;

		// Actualiza os atributos
		tmpBomb.mPosition.x = tmpTile.mPosition.x + Tile.TILE_SIZE_HALF;
		tmpBomb.mPosition.y = tmpTile.mPosition.y + Tile.TILE_SIZE_HALF;
		tmpBomb.mBombPower = _bombPower;
	}

	public boolean checkIfBombCollidingWithObject(MovableObject _obj, Vector2 _results)
	{
		_results.x = 0;
		_results.y = 0;

		Rectangle bbObj = _obj.getBoundingBox();
		for (Bomb b : mBombs)
		{
			Rectangle bbBomb = b.getBoundingBox();
			if (!bbObj.overlaps(bbBomb))
				continue;

			switch (_obj.mDirection)
			{
			case Directions.UP:
				_results.y = (bbBomb.y + Tile.TILE_SIZE) - bbObj.y;
				break;
			case Directions.DOWN:
				_results.y = bbBomb.y - bbObj.y;
				break;
			case Directions.LEFT:
				_results.x = (bbBomb.x + Tile.TILE_SIZE) - bbObj.x;
				break;
			case Directions.RIGHT:
				_results.x = bbBomb.x - bbObj.x;
				break;
			}
		}

		return !((_results.x == 0) && (_results.y == 0));
	}

	public void update()
	{
		mMap.update();

		updateBombs();
		updatePlayers();
		updateMonsters();
		updateExplosions();
		updateBonus();
	}

	public void updateMonsters()
	{
		for (Monster m : mMonsters)
			m.update();
	}

	public void updatePlayers()
	{
		for (Player p : mPlayers)
			p.update();
	}

	public void updateBombs()
	{
		for (Bomb b : mBombs)
			b.update();

	}

	public void updateExplosions()
	{
		for (Drawable ex : mExplosions)
		{
			ex.update();
			if (ex.mLooped)
				mExplosions.releaseObject(ex);
		}
	}

	public void updateBonus()
	{
		for (Bonus b : mSpawnedBonus)
			b.update();
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
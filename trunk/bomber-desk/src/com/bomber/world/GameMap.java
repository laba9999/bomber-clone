package com.bomber.world;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.bomber.Settings;
import com.bomber.Game;
import com.bomber.common.Collision;
import com.bomber.common.Directions;
import com.bomber.common.ObjectFactory;
import com.bomber.common.ObjectsPool;
import com.bomber.common.Utils;
import com.bomber.common.assets.GfxAssets;
import com.bomber.gameobjects.Bomb;
import com.bomber.gameobjects.Player;
import com.bomber.gameobjects.Tile;
import com.bomber.gameobjects.WorldMovableObject;
import com.bomber.gametypes.GameTypeHandler;

/**
 * Os Tiles vão ser lidos do nivel por camadas, imutáveis(mImutableTiles)e
 * destroyables(mDestroyableTiles).
 * 
 * Os tiles que são apresentados são os que estão na lista mTilesMap e que é
 * inicializado aquando do nivel pela ordem mImutableTiles > mDestroyableTiles.
 * 
 * Quando um Tile é destruído é actualizado a sua posição no atributo mTilesMap
 * com o Tile walkable respectivo.
 * 
 * @author sPeC!
 * 
 */
public class GameMap {

	private GameWorld mWorld;
	private ObjectsPool<Tile> mImutableTiles;
	public ObjectsPool<Tile> mDestroyableTiles;
	public ObjectsPool<Tile> mTilesBeingDestroyed;

	public Tile mPortal;
	public ArrayList<Tile> mTilesMap = new ArrayList<Tile>();
	public short mWidth, mHeight;
	public int mWidthPixels, mHeightPixels;

	public GameMap(GameWorld _gameWorld) {

		mWorld = _gameWorld;

		mImutableTiles = new ObjectsPool<Tile>((short) 10, new ObjectFactory.CreateTile(Tile.WALKABLE));
		mDestroyableTiles = new ObjectsPool<Tile>((short) 20, new ObjectFactory.CreateTile(Tile.DESTROYABLE));
		mTilesBeingDestroyed = new ObjectsPool<Tile>((short) 10, new ObjectFactory.CreateTile(Tile.WALKABLE));
		mPortal = null;
	}

	/**
	 * Adiciona um novo tile Destroyable ao mapa.
	 * 
	 * @param _line
	 *            Posição vertical no mapa.
	 * @param _col
	 *            Posição horizontal no mapa.
	 * @param _type
	 *            Tipo de tile a adicionar.
	 * @param _anim
	 *            Inclui o frame do tile normal e a sua destruição.
	 */
	public void addDestroyableTile(short _line, short _col, Animation _anim)
	{
		if (!Settings.MAP_LOAD_DESTROYABLE_TILES)
			return;

		Tile tmpTile = mDestroyableTiles.getFreeObject();

		tmpTile.mType = Tile.DESTROYABLE;
		tmpTile.setCurrentAnimation(_anim, (short) 8, false, false);
		tmpTile.mPositionInArray = (mHeight - (_line + 1)) * mWidth + _col;

		tmpTile.mPosition.set(_col * Tile.TILE_SIZE, _line * Tile.TILE_SIZE);
	}

	/**
	 * Adiciona um novo tile NonDestroyable ao mapa.
	 * 
	 * @param _line
	 *            Posição vertical no mapa.
	 * @param _col
	 *            Posição horizontal no mapa.
	 * @param _type
	 *            Tipo de tile a adicionar.
	 * @param _texture
	 *            TextureRegion da tile.
	 */
	public void addNonDestroyableTile(short _line, short _col, short _type, TextureRegion _texture)
	{
		Tile tmpTile = mImutableTiles.getFreeObject();
		tmpTile.mType = _type;
		tmpTile.mCurrentFrame = _texture;
		tmpTile.mPlayAnimation = false;
		tmpTile.mPositionInArray = (mHeight - (_line + 1)) * mWidth + _col;

		tmpTile.mPosition.set(_col * Tile.TILE_SIZE, _line * Tile.TILE_SIZE);

		if (tmpTile.mPositionInArray > mWidth * mHeight)
			throw new InvalidParameterException();
	}

	public void setupBonus(short _quantity)
	{
		Tile tileAtPosition = null;
		boolean positionIsAvailable;
		Random randomGenerator = Game.mRandomGenerator;

		for (int i = 0; i < _quantity; i++)
		{
			do
			{
				// gera posição aleatória
				short positionInArray = (short) randomGenerator.nextInt(mWidth * mHeight);

				// verifica se o tile na posição gerada é walkable
				tileAtPosition = mTilesMap.get(positionInArray);

				positionIsAvailable = tileAtPosition.mType == Tile.DESTROYABLE && !tileAtPosition.containsBonus() && !tileAtPosition.mIsPortal;
			} while (!positionIsAvailable);

			if (positionIsAvailable)
				tileAtPosition.mContainedBonusType = (short) randomGenerator.nextInt(7);
		}
	}

	/**
	 * A ser chamado de cada vez que é inicializado um novo nível.
	 * 
	 * @param _width
	 *            A largura em tiles do novo mapa.
	 */
	public void reset(short _width, short _height)
	{
		mWidth = _width;
		mHeight = _height;

		mWidthPixels = mWidth * Tile.TILE_SIZE;
		mHeightPixels = mHeight * Tile.TILE_SIZE;

		mImutableTiles.clear(true);
		mDestroyableTiles.clear();
		mTilesBeingDestroyed.clear();

		mPortal = null;
	}

	/**
	 * Sempre que forem adicionados tiles este método deve ser usado para que o
	 * array que vai ser apresentado seja actualizado.
	 */
	public void updateTilesForPresentation()
	{
		mTilesMap.clear();

		for (int i = 0; i < mWidth * mHeight; i++)
			mTilesMap.add(null);

		for (Tile tl : mImutableTiles)
			mTilesMap.set(tl.mPositionInArray, tl);

		for (Tile tl : mDestroyableTiles)
			mTilesMap.set(tl.mPositionInArray, tl);

		if (Game.mGameType != GameTypeHandler.CTF && Game.mGameType != GameTypeHandler.TEAM_CTF)
			return;
		// Coloca as bases das flags
		getTile(mWorld.mFlags[0].mPosition).setCurrentAnimation(GfxAssets.mPortal, (short) 2, false, false);
		getTile(mWorld.mFlags[1].mPosition).setCurrentAnimation(GfxAssets.mPortal, (short) 2, false, false);

	}

	public void explodeTile(Tile _tile, Bomb _bomb)
	{
		if (_tile.mType != Tile.DESTROYABLE)
			return;

		_tile.explode();

		// Prémio! :)
		for (Player p : mWorld.mPlayers)
		{
			if (p.mColor == _bomb.mDropedBy)
			{
				p.mPoints += Tile.POINTS;

				if (mWorld.getLocalPlayer().mColor == p.mColor)
					mWorld.spawnOverlayingPoints("+100", _tile.mPosition.x, _tile.mPosition.y + Tile.TILE_SIZE);
			}
		}

		// Verifica se tem bónus
		if (_tile.containsBonus())
			mWorld.spawnBonus(_tile);

		// Actualiza as pools
		mDestroyableTiles.releaseObject(_tile);

		Tile tmpTile = mTilesBeingDestroyed.getFreeObject();
		tmpTile.mPosition.set(_tile.mPosition);
		_tile.clone(tmpTile);

		// Verifica se é o portal
		if (!Game.mIsPVPGame && _tile.mIsPortal)
		{
			spawnPortal(_tile);
			return;
		}

		// O mapa vai apresentar a partir de agora o tile walkable que estava
		// por baixo deste
		for (Tile tl : mImutableTiles)
		{
			if (tl.mPositionInArray == _tile.mPositionInArray)
			{
				mTilesMap.set(tl.mPositionInArray, tl);
				break;
			}
		}
	}

	/**
	 * Verifica colisões entre um dado objecto e os tiles do mapa. Por forma a
	 * facilitar o controlo dos jogadores é verificado se o overlap existente é
	 * minimo e se sim ajusta as coordenadas por forma a ser possivel o jogador
	 * continuar caminho.
	 * 
	 * @param _obj
	 *            O Objecto a testar com colisões
	 * @param _results
	 *            As quantidades de overlap nos componentes (x,y)
	 * 
	 * @param _ignoreDestroyables
	 *            Se os tiles destroyable devem ser não incluidos na
	 *            verificação.
	 * @return True se for detectada uma colisão, False caso contrário.
	 */
	public void checkForCollisions(WorldMovableObject _obj, Collision _results, boolean _ignoreDestroyables)
	{
		_results.reset();

		int testIdx;
		int startIdx = calcTileIndex(_obj.mPosition);

		Rectangle bbObj = _obj.getBoundingBox();

		// if( _obj instanceof Player)
		// {
		// bbObj.y -= Tile.TILE_SIZE_HALF;
		// }

		switch (_obj.mDirection)
		{
		case Directions.UP:

			// Imediatamente acima
			testIdx = calcTileIndex(startIdx, Directions.UP, (short) 1);
			testCollision(startIdx, testIdx, _results, bbObj, Directions.UP, _ignoreDestroyables);

			if (_results.mType != Collision.NONE)
				break;

			// Acima esquerda
			testIdx = calcTileIndex(testIdx, Directions.LEFT, (short) 1);
			testCollision(startIdx, testIdx, _results, bbObj, Directions.UP, _ignoreDestroyables);

			if (_results.mType != Collision.NONE)
			{
				checkAllowedOverlapX(_results, bbObj, testIdx, Directions.LEFT);
				break;
			}

			// Acima direita
			testIdx = calcTileIndex(testIdx, Directions.RIGHT, (short) 2);
			testCollision(startIdx, testIdx, _results, bbObj, Directions.UP, _ignoreDestroyables);

			if (_results.mType != Collision.NONE)
			{
				checkAllowedOverlapX(_results, bbObj, testIdx, Directions.RIGHT);
				break;
			}
			break;

		case Directions.DOWN:

			// Imediatamente abaixo
			testIdx = calcTileIndex(startIdx, Directions.DOWN, (short) 1);
			testCollision(startIdx, testIdx, _results, bbObj, Directions.DOWN, _ignoreDestroyables);

			if (_results.mType != Collision.NONE)
			{
				// checkAllowedOverlapY(_results, bbObj, testIdx,
				// Directions.DOWN);
				break;
			}

			// Abaixo esquerda
			testIdx = calcTileIndex(testIdx, Directions.LEFT, (short) 1);
			testCollision(startIdx, testIdx, _results, bbObj, Directions.DOWN, _ignoreDestroyables);

			if (_results.mType != Collision.NONE)
			{
				checkAllowedOverlapX(_results, bbObj, testIdx, Directions.LEFT);
				break;
			}

			// Abaixo direita
			testIdx = calcTileIndex(testIdx, Directions.RIGHT, (short) 2);
			testCollision(startIdx, testIdx, _results, bbObj, Directions.DOWN, _ignoreDestroyables);

			if (_results.mType != Collision.NONE)
			{
				checkAllowedOverlapX(_results, bbObj, testIdx, Directions.RIGHT);
				break;
			}
			break;

		case Directions.LEFT:

			// Imediatamente esquerda
			testIdx = calcTileIndex(startIdx, Directions.LEFT, (short) 1);
			testCollision(startIdx, testIdx, _results, bbObj, Directions.LEFT, _ignoreDestroyables);

			if (_results.mType != Collision.NONE)
				break;

			// Esquerda acima
			testIdx = calcTileIndex(testIdx, Directions.UP, (short) 1);
			testCollision(startIdx, testIdx, _results, bbObj, Directions.LEFT, _ignoreDestroyables);

			if (_results.mType != Collision.NONE)
			{
				checkAllowedOverlapY(_results, bbObj, testIdx, Directions.UP);
				break;
			}

			// Esquerda abaixo
			testIdx = calcTileIndex(testIdx, Directions.DOWN, (short) 2);
			testCollision(startIdx, testIdx, _results, bbObj, Directions.LEFT, _ignoreDestroyables);

			if (_results.mType != Collision.NONE)
			{
				checkAllowedOverlapY(_results, bbObj, testIdx, Directions.DOWN);
				break;
			}
			break;

		case Directions.RIGHT:

			// Imediatamente direita
			testIdx = calcTileIndex(startIdx, Directions.RIGHT, (short) 1);
			testCollision(startIdx, testIdx, _results, bbObj, Directions.RIGHT, _ignoreDestroyables);

			if (_results.mType != Collision.NONE)
				break;

			// Direita acima
			testIdx = calcTileIndex(testIdx, Directions.UP, (short) 1);
			testCollision(startIdx, testIdx, _results, bbObj, Directions.RIGHT, _ignoreDestroyables);

			if (_results.mType != Collision.NONE)
			{
				checkAllowedOverlapY(_results, bbObj, testIdx, Directions.UP);
				break;
			}

			// Direita abaixo
			testIdx = calcTileIndex(testIdx, Directions.DOWN, (short) 2);
			testCollision(startIdx, testIdx, _results, bbObj, Directions.RIGHT, _ignoreDestroyables);

			if (_results.mType != Collision.NONE)
			{
				checkAllowedOverlapY(_results, bbObj, testIdx, Directions.DOWN);
				break;
			}
			break;
		}
	}

	private void checkAllowedOverlapX(Collision _result, Rectangle _objBB, int testIdx, short _direction)
	{
		if (_direction != Directions.LEFT && _direction != Directions.RIGHT)
			throw new InvalidParameterException("Esta direcção não deve ser passada a este método");

		// Verifica se está muito longe de poder passar
		Tile tmpTile = mTilesMap.get(testIdx);

		float valueOverlaped;
		if (_direction == Directions.LEFT)
			valueOverlaped = (tmpTile.mPosition.x + Tile.TILE_SIZE) - _objBB.x;
		else
			valueOverlaped = tmpTile.mPosition.x - (_objBB.x + Tile.TILE_SIZE);

		Game.LOGGER.log("Value overlapped X: " + valueOverlaped);
		if (Math.abs(valueOverlaped) < Collision.ALLOWED_OVERLAP)
			_result.mAmounts.x = valueOverlaped;
	}

	private void checkAllowedOverlapY(Collision _result, Rectangle _objBB, int testIdx, short _direction)
	{
		if (_direction != Directions.UP && _direction != Directions.DOWN)
			throw new InvalidParameterException("Esta direcção não deve ser passada a este método");

		// Verifica se está muito longe de poder passar
		Tile tmpTile = mTilesMap.get(testIdx);

		float valueOverlaped;
		if (_direction == Directions.UP)
			valueOverlaped = tmpTile.mPosition.y - (_objBB.y + Tile.TILE_SIZE);
		else
			valueOverlaped = (tmpTile.mPosition.y + Tile.TILE_SIZE) - _objBB.y;

		Game.LOGGER.log("Value overlapped Y: " + valueOverlaped);
		if (Math.abs(valueOverlaped) < Collision.ALLOWED_OVERLAP)
			_result.mAmounts.y = valueOverlaped;
	}

	private void testCollision(int _playerTileIdx, int _tileIdx, Collision _result, Rectangle _objBB, short _direction, boolean _ignoreDestroyables)
	{
		final Rectangle bbTile = new Rectangle(0, 0, Tile.TILE_SIZE, Tile.TILE_SIZE);

		if (_playerTileIdx == _tileIdx)
			return;

		Tile tmpTile = mTilesMap.get(_tileIdx);

		if ((tmpTile.mType == Tile.DESTROYABLE && _ignoreDestroyables))
			return;

		if ((tmpTile.mContainsBomb && _ignoreDestroyables))
			return;

		if (tmpTile.mType != Tile.COLLIDABLE && tmpTile.mType != Tile.DESTROYABLE && !tmpTile.mContainsBomb)
			return;

		// Actualiza a posição da bounding box do tile
		bbTile.x = tmpTile.mPosition.x;
		bbTile.y = tmpTile.mPosition.y;

		// Verifica se existe colisão
		if (!Utils.rectsOverlap(_objBB, bbTile))
			return;

		if (tmpTile.mType == Tile.COLLIDABLE || tmpTile.mType == Tile.DESTROYABLE)
			_result.mType = Collision.TILE;
		else if (tmpTile.mContainsBomb)
		{
			// De certeza?
			boolean found = false;
			for (Bomb b : mWorld.mBombs)
			{
				if (b.mContainer.mPositionInArray == tmpTile.mPositionInArray)
				{
					_result.mType = Collision.BOMB;
					found = true;
					break;
				}
			}

			if (!found)
			{
				tmpTile.mContainsBomb = false;
				return;
			}

		}

		// Devolve o valor overlapped baseado na direcção do objecto
		if (_direction == Directions.UP)
			_result.mAmounts.y = bbTile.y - (_objBB.y + Tile.TILE_SIZE);
		else if (_direction == Directions.DOWN)
			_result.mAmounts.y = (bbTile.y + Tile.TILE_SIZE) - _objBB.y;
		else if (_direction == Directions.LEFT)
			_result.mAmounts.x = (bbTile.x + Tile.TILE_SIZE) - _objBB.x;
		else if (_direction == Directions.RIGHT)
			_result.mAmounts.x = bbTile.x - (_objBB.x + Tile.TILE_SIZE);
	}

	public void update()
	{
		// Verifica se algum dos tiles que foram destruidos já terminaram a
		// animação
		for (Tile tmpTile : mTilesBeingDestroyed)
		{
			tmpTile.update();

			if (tmpTile.mLooped)
				mTilesBeingDestroyed.releaseObject(tmpTile);
		}

		if (mPortal != null)
			mPortal.update();

	}

	/**
	 * 
	 * @param _startPos
	 *            A posição (x,y) onde iniciar a procura.
	 * @param _direction
	 *            A direcção do tipo @link(Directions) onde a procura será
	 *            efectuada.
	 * @param _tileTypes
	 *            Os tipos @link(Tile) de tiles a procurar.
	 * @return Devolve a distância em número de tiles até ao primeiro
	 * @link(Tile) de um dos tipos passados como argumento.
	 */
	public int getDistanceToNext(Vector2 _startPos, short _direction, short _tileTypes)
	{
		return getDistanceToNext(-1, _startPos, _direction, _tileTypes);
	}

	/**
	 * 
	 * @param _maxSize
	 *            Número máximo de @link(Tile)'s a percorrer na direcção dada.
	 *            -1 significa sem limite.
	 * @param _startPos
	 *            A posição (x,y) onde iniciar a procura
	 * @param _direction
	 *            A direcção do tipo @link(Directions) onde a procura será
	 *            efectuada
	 * @param _tileTypes
	 *            Os tipos @link(Tile) de tiles a procurar.
	 * @return Devolve a distância em número de tiles até ao primeiro
	 * @link(Tile) de um dos tipos passados como argumento. Se nenhum
	 * @link(Tile) dos tipos pretendidos forem encontrados é devolvido -1.
	 */
	public int getDistanceToNext(int _maxSize, Vector2 _startPos, short _direction, short... _tileTypes)
	{
		boolean found = false;
		int lastIdx = -1;
		int idx = calcTileIndex(_startPos);
		for (int i = 0; i < _maxSize; i++)
		{
			// Anda um tile na direcção pretendida
			idx = calcTileIndex(idx, _direction, (short) 1);

			// Se estamos na mesma posição da iteração anterior é porque
			// atingimos uma border
			if (idx == lastIdx)
			{
				idx = -1;
				break;
			}

			// Verifica se o tile actual é do tipo de um dos tipos pretendidos
			Tile tmpTile = mTilesMap.get(idx);
			for (int c = 0; c < _tileTypes.length; c++)
			{
				if (tmpTile.mType == _tileTypes[c])
				{
					found = true;
					idx = i + 1;
					break;
				}
			}

			if (found)
				break;
		}

		if (!found)
			idx = -1;

		return idx;
	}

	/**
	 * Não está implementada porque o tile mais próximo vai ser sempre aquele
	 * que corresponde à posição passada como parâmetro
	 * 
	 * @param _position
	 *            A posição (x,y) onde iniciar a procura.
	 * @param _direction
	 *            A direcção do tipo @link(Directions) onde a procura será
	 *            efectuada.
	 * @return Devolve o @link(Tile) mais próximo baseado na posição e direccção
	 *         passados como parametros. Em caso de igualdade de distância é
	 *         devolvido o tile oposto à direcção.
	 */
	public Vector2 getNearestTilePosition(Vector2 _position, short _direction)
	{
		throw new UnsupportedOperationException();
	}

	public int calcTileIndex(Vector2 _position, short _direction, short _distance)
	{
		int idx = calcTileIndex(_position);
		idx = calcTileIndex(idx, _direction, _distance);

		return idx;
	}

	/**
	 * Tranforma uma posição 2D num index do array de tiles. Isto é feito
	 * baseado no tamanho do tile e na largura do mapa.
	 * 
	 * @param _position
	 *            A posição (x,y) a transformar.
	 * @return Posição no array local de tiles do @link(Tile) mais próximo.
	 */
	public int calcTileIndex(Vector2 _position)
	{
		int col = (int) (_position.x / Tile.TILE_SIZE);
		int line = (int) (_position.y / Tile.TILE_SIZE);

		int res = (mHeight - (line + 1)) * mWidth + col;

		if (res > mTilesMap.size())
			throw new InvalidParameterException();

		return res;
	}

	/**
	 * Soma uma _distance ao _startIndex baseado na _direction e devolve o index
	 * respectivo.
	 * 
	 * @param _startIndex
	 *            Indice do @link(Tile) no array local onde iniciar.
	 * @param _direction
	 *            A direcção do tipo @link(Directions) onde a _distance será
	 *            somada.
	 * @param _distance
	 *            A distância a somar ao _starIndex.
	 * @return Posição no array local de tiles do @link(Tile) calculado.
	 */
	public int calcTileIndex(int _startIndex, short _direction, short _distance)
	{
		int finalY;
		int currentY;

		int res = _startIndex;

		switch (_direction)
		{
		case Directions.DOWN:
			res += _distance * mWidth;

			if (res > mTilesMap.size() - 1)
				res -= _distance * mWidth;

			break;

		case Directions.UP:
			res -= _distance * mWidth;

			if (res < 0)
				res += _distance * mWidth;
			break;

		case Directions.LEFT:
			// Obtem a linha inicial
			currentY = _startIndex / mWidth;

			res -= _distance;

			// Verifica a linha após o movimento
			finalY = res / mWidth;
			if (currentY != finalY)
			{
				// clamp
				res = currentY * mWidth;
			}

			break;

		case Directions.RIGHT:
			// Obtem a coluna inicial
			currentY = _startIndex / mWidth;

			res += _distance;

			// Verifica a linha após o movimento
			finalY = res / mWidth;
			if (currentY != finalY)
			{
				// clamp
				res = currentY * mWidth + (mWidth - 1);
			}

		}

		return res;
	}

	/**
	 * Obtém o @link(Tile) mais próximo da posição providenciada.
	 * 
	 * @param _position
	 *            A posição (x,y) onde iniciar a procura.
	 * @return Devolve o tile mais próximo da _position.
	 */
	public Tile getTile(Vector2 _position)
	{
		int idx = calcTileIndex(_position);
		return mTilesMap.get(idx);
	}

	/**
	 * Obtém o @link(Tile) mais próximo da posição providenciada, tendo em conta
	 * uma direcção e uma distância.
	 * 
	 * @param _position
	 *            A posição (x,y) onde iniciar a procura.
	 * @param _direction
	 *            A direcção do tipo @link(Directions) onde a procura será
	 *            efectuada.
	 * @param _distance
	 *            A distância em número de @link(Tile)'s a somar ao à posição
	 *            inicial.
	 * @return Devolve o @link(Tile) calculado.
	 */
	public Tile getTile(Vector2 _position, short _direction, short _distance)
	{
		int idx = calcTileIndex(_position);
		idx = calcTileIndex(idx, _direction, _distance);

		return mTilesMap.get(idx);
	}

	/**
	 * Obtém o @link(Tile) mais próximo da posição providenciada, tendo em conta
	 * uma direcção e uma distância.
	 * 
	 * @param _starIdx
	 *            A posição no array onde iniciar a procura.
	 * @param _direction
	 *            A direcção do tipo @link(Directions) onde a procura será
	 *            efectuada.
	 * @param _distance
	 *            A distância em número de @link(Tile)'s a somar ao à posição
	 *            inicial.
	 * @return Devolve o @link(Tile) calculado.
	 */
	public Tile getTile(int _starIdx, short _direction, short _distance)
	{
		int idx = calcTileIndex(_starIdx, _direction, _distance);

		return mTilesMap.get(idx);
	}

	public Tile getRandomTileFromType(short _type)
	{

		if (_type > Tile.PORTAL || _type < Tile.WALKABLE)
		{
			throw new InvalidParameterException("Tipo inválido");
		}

		Random randomGenerator = Game.mRandomGenerator;
		short col;
		short lin;
		Tile tileAtPosition = null;
		boolean unwantedType = false;

		do
		{
			// gera posição aleatória
			col = (short) randomGenerator.nextInt(mWidth);
			lin = (short) randomGenerator.nextInt(mHeight);

			float colInPixels = col * Tile.TILE_SIZE;
			float linInPixels = lin * Tile.TILE_SIZE;

			// verifica se o tile na posição gerada é do tipo pretendido
			tileAtPosition = getTile(new Vector2(colInPixels, linInPixels));
			unwantedType = tileAtPosition.mType != _type;

		} while (unwantedType);

		return tileAtPosition;
	}

	public void placePortal()
	{
		Tile futurePortal = getRandomTileFromType(Tile.DESTROYABLE);
		futurePortal.mIsPortal = true;
	}

	public void spawnPortal(Tile _t)
	{
		mPortal = mImutableTiles.getFreeObject();

		mPortal.mType = Tile.WALKABLE;
		mPortal.mPosition.x = _t.mPosition.x;
		mPortal.mPosition.y = _t.mPosition.y;
		mPortal.mPositionInArray = _t.mPositionInArray;
		mTilesMap.set(_t.mPositionInArray, mPortal);

		if (!mWorld.mGameTypeHandler.isObjectiveAcomplished())
			mPortal.setCurrentAnimation(GfxAssets.mPortal, GfxAssets.PORTAL_FRAMES_COUNT, false, false);
		else
			mPortal.setCurrentAnimation(GfxAssets.mPortal, GfxAssets.PORTAL_FRAMES_COUNT, true, true);

	}

}
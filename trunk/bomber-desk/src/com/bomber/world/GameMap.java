package com.bomber.world;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.bomber.common.Directions;
import com.bomber.common.ObjectFactory;
import com.bomber.common.ObjectsPool;
import com.bomber.gameobjects.Tile;

public class GameMap {
	public ArrayList<Tile> mTiles = new ArrayList<Tile>();
	public ObjectsPool<Tile> mDestroyableTiles;
	public short mWidth;
	public short mHeight;

	public GameMap() {
		mDestroyableTiles = new ObjectsPool<Tile>((short) 20, new ObjectFactory.CreateTile());
	}

	public void update()
	{
		// TODO: Verifica os tiles que estão destroyed se a animação já
		// terminou, e se sim remove-o da pool
		
		for(Tile tmpTile : mDestroyableTiles)
		{
			if(!tmpTile.mIsDestroyed)
				continue;
			
			// Verifica se animação de explosão terminou
			if(tmpTile.mLooped)
				mDestroyableTiles.releaseObject(tmpTile);
		}
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
			Tile tmpTile = mTiles.get(idx);
			for (int c = 0; c < _tileTypes.length; c++)
			{
				if (tmpTile.mType == _tileTypes[c])
				{
					found = true;
					break;
				}
			}

			if (found)
				break;
		}

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
		int x = (int) (_position.x / Tile.TILE_SIZE);
		int y = (int) (_position.y / Tile.TILE_SIZE);

		return y * mWidth + x;
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

			if (res > mTiles.size() - 1)
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
			// Obtem a linha inicial
			currentY = _startIndex / mWidth;

			res -= _distance;

			// Verifica a linha após o movimento
			finalY = res / mWidth;
			if (currentY != finalY)
			{
				// clamp
				res = currentY * mWidth + (mWidth - 1);
			}

		}

		// Verifica se não é devolvido um valor inválido
		if (res < 0)
			res = 0;

		if (res > mTiles.size() - 1)
			res = mTiles.size() - 1;

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
		return mTiles.get(idx);
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

		return mTiles.get(idx);
	}
}
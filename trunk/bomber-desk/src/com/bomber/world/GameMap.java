package com.bomber.world;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.bomber.gameobjects.Tile;

public class GameMap {
	public ArrayList<Tile> mTiles;
	public short mWidth;
	public short mHeight;

	public Tile getTile(Vector2 _position)
	{
		throw new UnsupportedOperationException();
	}

	public Tile getTile(Vector2 _position, short _direction, short _distance)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Um wrapper para o metodo com o mesmo nome mas que pede tambem o parametro
	 * _maxSize. É devolvida uma chamada ao segundo método em que o primeiro
	 * parametro será -1 (significa sem limite).
	 */
	public int getDistanceToNext(Vector2 _startPos, short _direction,
			short _tileTypes)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Devolve a distancia em tiles desde a posição e na direcção passadas como
	 * argumento até um dos tipos de tile pretendidos. É devolvida a distância
	 * até ao tile mais próximo que corresponda a um dos tipos pretendidos
	 * (passados como varargs). A distância é limitada pelo parametro _maxSize,
	 * se _maxSize==-1 então não existe um limite.
	 */
	public int getDistanceToNext(short _maxSize, Vector2 _startPos,
			short _direction, short _tileTypes)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Obtem o tile mais proximo baseado na posição e direccção passados como
	 * parametros. Em caso de igualdade de distância é devolvido o tile oposto à
	 * direcção.
	 */
	public Vector2 getNearestTilePosition(Vector2 _position, short _direction)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Tranforma uma posição 2D num index do array de tiles. Isto é feito
	 * baseado no tamanho do tile e na altura/largura do mapa.
	 */
	public int calcTileIndex(Vector2 _position)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Soma uma _distance ao _startIndex baseado na _direction e devolve o index
	 * respectivo.
	 */
	public int calcTileIndex(int _startIndex, short _direction, short _distance)
	{
		throw new UnsupportedOperationException();
	}
}
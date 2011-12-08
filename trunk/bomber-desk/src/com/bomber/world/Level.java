package com.bomber.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.bomber.common.Assets;
import com.bomber.gameobjects.Tile;

public class Level
{

	/**
	 * Serve também para fazer o reset.
	 */

	public static short mNumberOfPlayers;

	public static String mName;
	public static short mRows;
	public static short mColumns;
	public static short[][] mWalkableIDs;
	public static short[][] mCollidableIDs;
	public static short[][] mDestroyableIDs;
	public static short[][] mSpawnIDs;

	public static HashMap<Short, ImageTile> mImageTiles = new HashMap<Short, ImageTile>();

	public static void main(String[] args)
	{ // TODO: delete this
		Level l = new Level();
		l.loadLevel("level1", null, (short) 1);
	}

	public static void loadLevel(String _levelID, GameWorld _world, short _howManyPlayers)
	{
		mNumberOfPlayers = _howManyPlayers;

		try
		{

			// setup parser
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader xmlReader = parser.getXMLReader();

			// parse level xml file
			LevelXMLHandler myLevelXMLHandler = new LevelXMLHandler();

			xmlReader.setContentHandler(myLevelXMLHandler);
			File file = new File(".\\levels\\" + _levelID + "\\" + _levelID + ".xml");
			InputStream inputStream = new FileInputStream(file);
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			xmlReader.parse(new InputSource(reader));

			// parse tileset xml file
			TilesetXMLHandler myTilesetXMLHandler = new TilesetXMLHandler();

			xmlReader.setContentHandler(myTilesetXMLHandler);
			file = new File(".\\levels\\" + _levelID + "\\tileset.xml");
			inputStream = new FileInputStream(file);
			reader = new InputStreamReader(inputStream, "UTF-8");
			xmlReader.parse(new InputSource(reader));

			setupLevel(_world);

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void setupLevel(GameWorld _world)
	{
		// setup do nivel
		_world.mMap.reset(mColumns, mRows);

		// carrega Assets
		for (short i = 0; i < mRows; i++)
		{
			for (short j = 0; j < mColumns; j++)
			{
				ImageTile temp;

				short id = mWalkableIDs[i][j];
				if (id != 0)
				{
					temp = mImageTiles.get(id);
					Assets.loadNonDestroyableTile(temp.mImageFile, temp.mFlipHorizontally, temp.mFlipVertically);
					_world.mMap.addNonDestroyableTile(j, i, Tile.WALKABLE, Assets.mNonDestroyableTiles.get(temp.mImageFile));
				}

				id = mCollidableIDs[i][j];
				if (id != 0)
				{
					temp = mImageTiles.get(id);
					Assets.loadNonDestroyableTile(temp.mImageFile, temp.mFlipHorizontally, temp.mFlipVertically);
					_world.mMap.addNonDestroyableTile(j, i, Tile.COLLIDABLE, Assets.mNonDestroyableTiles.get(temp.mImageFile));
				}

				id = mDestroyableIDs[i][j];
				if (id != 0)
				{
					temp = mImageTiles.get(id);
					Assets.loadDestroyableTile(temp.mImageFile);
					_world.mMap.addDestroyableTile(j, i, Tile.COLLIDABLE, Assets.mDestroyableTiles.get(temp.mImageFile));
				}

				id = mSpawnIDs[i][j];
				if (id != 0)
				{
					setupSpawn(id, j, i, _world);
				}

			}
		}

		
		_world.mMap.updateTilesForPresentation();
		
	}

	private static void setupSpawn(short _id, short _positionX, short _positionY, GameWorld _world)
	{
		String filename = mImageTiles.get(_id).mImageFile;

		// BOMBERMANS
		if (filename.equals("spawn_p1"))
		{
			_world.spawnPlayer("b_white", _positionY, _positionX);
		} else if (filename.equals("spawn_p2") && mNumberOfPlayers >= 2)
		{
			_world.spawnPlayer("b_red", _positionY, _positionX);
		} else if (filename.equals("spawn_p3") && mNumberOfPlayers >= 3)
		{
			_world.spawnPlayer("b_blue", _positionY, _positionX);
		} else if (filename.equals("spawn_p4") && mNumberOfPlayers == 4)
		{
			_world.spawnPlayer("b_green", _positionY, _positionX);
		} else
		{// MONSTROS
			String[] splitted = filename.split("_");
			// ex: m_generic1_walk_0 tem monsterID m_generic1
			String monsterID = splitted[0] + "_" + splitted[1];

			if (monsterID.contains("m_generic"))
			{ // se é um monstro genérico
				Assets.loadGenericMonster(monsterID);
			} else
			{
				Assets.loadNormalMonster(monsterID);
			}

			_world.spawnMonster(monsterID, _positionY, _positionX);

		}

	}

}
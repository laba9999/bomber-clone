package com.bomber.common.assets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Scanner;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.SharedPreferences;

import com.badlogic.gdx.Gdx;
import com.bomber.Settings;
import com.bomber.Game;
import com.bomber.gameobjects.Tile;
import com.bomber.world.GameWorld;
import com.bomber.world.LevelInfo;

public class Level {

	/**
	 * Serve também para fazer o reset.
	 */

	public static short mNumberOfPlayers;

	public static String mName;
	public static short mRows;
	public static short mColumns;
	public static short[][] mWalkableIDs;
	public static short[][] mCollidableIDs;
	public static short[][] mFlags;
	public static short[][] mDestroyableIDs;
	public static short[][] mSpawnIDs;

	public static HashMap<Short, String> mImageTiles = new HashMap<Short, String>();

	public static boolean mIsLoaded;

	public static LevelInfo mInfo = new LevelInfo();

	public static void loadLevel(String _levelID, GameWorld _world, short _howManyPlayers)
	{
		mIsLoaded = false;
		mNumberOfPlayers = _howManyPlayers;
		mInfo.mCurrentLevelName = _levelID;

		if (!Game.mIsPVPGame)
		{
			Integer id = Integer.valueOf(_levelID.substring(_levelID.length() - 1)) - 1;

			if ( !Settings.STARTED_FROM_DESKTOP && id > Settings.GAME_PREFS.getInt("campaignLevelCompleted", 0))
			{
				SharedPreferences.Editor edit = Settings.GAME_PREFS.edit();
				edit.putInt("campaignLevelCompleted", id);
				edit.commit();
			}
		}
		try
		{
			// setup parser
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader xmlReader = parser.getXMLReader();

			// parse level xml file
			LevelXMLHandler myLevelXMLHandler = new LevelXMLHandler();

			xmlReader.setContentHandler(myLevelXMLHandler);
			InputStream inputStream = Gdx.files.internal("levels/" + _levelID + "/" + _levelID + ".xml").read();

			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			xmlReader.parse(new InputSource(reader));

			// parse tileset xml file
			TilesetXMLHandler myTilesetXMLHandler = new TilesetXMLHandler();

			xmlReader.setContentHandler(myTilesetXMLHandler);

			inputStream = Gdx.files.internal("levels/" + _levelID + "/tileset.xml").read();
			reader = new InputStreamReader(inputStream, "UTF-8");
			xmlReader.parse(new InputSource(reader));

			flipMatrixVertically(mWalkableIDs);
			flipMatrixVertically(mCollidableIDs);
			flipMatrixVertically(mDestroyableIDs);
			flipMatrixVertically(mSpawnIDs);
			flipMatrixVertically(mFlags);

			loadLevelInfo(_world);
			setupLevel(_world);

			_world.mMap.setupBonus(mInfo.mNumberBonus);
			_world.mClock.reset(mInfo.mMinutes, mInfo.mSeconds);

			// Deixem o GC trabalhar
			mWalkableIDs = null;
			mCollidableIDs = null;
			mDestroyableIDs = null;
			mSpawnIDs = null;
			mFlags = null;

			mIsLoaded = true;
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private static void loadLevelInfo(GameWorld _world)
	{
		String[] levelInfo = new String[5];
		InputStream inputStream = Gdx.files.internal("levels/" + mInfo.mCurrentLevelName + "/info.txt").read();
		Scanner scanner = new Scanner(inputStream);
		try
		{
			short i = 0;
			while (scanner.hasNextLine())
			{
				String nextLine = scanner.nextLine();
				if (nextLine.length() > 0 && nextLine.charAt(0) != '#')
					levelInfo[i++] = nextLine;
			}
		} finally
		{
			scanner.close();

			try
			{
				inputStream.close();
			} catch (IOException e)
			{
			}

		}

		mInfo.set(levelInfo);
	}

	private static void flipMatrixVertically(short[][] _matrix)
	{
		int center = _matrix.length / 2;
		for (int i = 0; i < center; i++)
		{
			short[] aux = _matrix[i];
			_matrix[i] = _matrix[_matrix.length - i - 1];
			_matrix[_matrix.length - i - 1] = aux;
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
				String filename;

				short id = mWalkableIDs[i][j];
				if (id != 0)
				{
					filename = mImageTiles.get(id);
					GfxAssets.loadNonDestroyableTile(filename);
					_world.mMap.addNonDestroyableTile(i, j, Tile.WALKABLE, GfxAssets.mNonDestroyableTiles.get(filename));
				}

				id = mCollidableIDs[i][j];
				if (id != 0)
				{
					filename = mImageTiles.get(id);
					GfxAssets.loadNonDestroyableTile(filename);
					_world.mMap.addNonDestroyableTile(i, j, Tile.COLLIDABLE, GfxAssets.mNonDestroyableTiles.get(filename));
				}

				id = mDestroyableIDs[i][j];
				if (id != 0)
				{
					filename = mImageTiles.get(id);
					GfxAssets.loadDestroyableTile(filename);
					_world.mMap.addDestroyableTile(i, j, GfxAssets.mDestroyableTiles.get(filename));
				}

				id = mSpawnIDs[i][j];
				if (id != 0)
				{
					setupSpawn(id, j, i, _world);
				}

				id = mFlags[i][j];
				if (id != 0)
				{
					_world.spawnFlag(mImageTiles.get(id), i, j);
				}
			}
		}

		_world.mMap.updateTilesForPresentation();
	}

	private static void setupSpawn(short _id, short _positionX, short _positionY, GameWorld _world)
	{
		String filename = mImageTiles.get(_id);

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
		} else if (filename.contains("m_"))
		{
			// MONSTROS
			String[] splitted = filename.split("_");
			// ex: m_generic1_walk_0 tem monsterID m_generic1
			String monsterID = splitted[0] + "_" + splitted[1];

			if (monsterID.contains("m_generic"))
			{ // se é um monstro genérico
				GfxAssets.loadGenericMonster(monsterID);
			} else
			{
				GfxAssets.loadNormalMonster(monsterID);
			}

			_world.spawnMonster(monsterID, _positionY, _positionX);
		}
	}

}
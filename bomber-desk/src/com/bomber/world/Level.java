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
import com.bomber.common.Factory;
import com.bomber.common.ObjectFactory;
import com.bomber.gameobjects.Player;
import com.bomber.gameobjects.Tile;
import com.bomber.gameobjects.monsters.Monster;

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
		l.loadLevel("level1", null,(short) 1);
	}

	public static void loadLevel(String _levelID, GameWorld _world,
			short _howManyPlayers)
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
			File file = new File(".\\levels\\" + _levelID + "\\" + _levelID
					+ ".xml");
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
		_world.mMap.reset(mColumns);

		// carrega Assets das tiles
		for (short i = 0; i < mColumns; i++)
		{
			for (short j = 0; j < mRows; j++)
			{

				ImageTile temp;

				short id = mWalkableIDs[i][j];
				if (id != 0)
				{
					temp = mImageTiles.get(id);
					Assets.loadNonDestroyableTile(temp.mImageFile,
							temp.mFlipHorizontally, temp.mFlipVertically);
					_world.mMap.addNonDestroyableTile(j, i, Tile.WALKABLE,
							Assets.mNonDestroyableTiles.get(temp.mImageFile));
				}

				id = mCollidableIDs[i][j];
				if (id != 0)
				{
					temp = mImageTiles.get(id);
					Assets.loadNonDestroyableTile(temp.mImageFile,
							temp.mFlipHorizontally, temp.mFlipVertically);
					_world.mMap.addNonDestroyableTile(j, i, Tile.COLLIDABLE,
							Assets.mNonDestroyableTiles.get(temp.mImageFile));
				}

				id = mDestroyableIDs[i][j];
				if (id != 0)
				{
					temp = mImageTiles.get(id);
					Assets.loadDestroyableTile(temp.mImageFile);
					_world.mMap.addDestroyableTile(j, i, Tile.COLLIDABLE,
							Assets.mDestroyableTiles.get(temp.mImageFile));
				}

				id = mSpawnIDs[i][j];
				if (id != 0)
				{
					setupSpawns(id, j, i, _world);
				}

			}
		}

		// TODO : tratar mSpawns

	}

	private static void setupSpawns(short _id, short _positionX,
			short _positionY, GameWorld _world)
	{
		ImageTile temp = mImageTiles.get(_id);

		//BOMBERMANS
		if (temp.mImageFile.equals("spawn_p1"))
		{
			Assets.loadPlayer("white/b_white");
			Player p = _world.mPlayers.getFreeObject();
			p.mPosition.set(_positionX, _positionY);
			p.mAnimations = Assets.mPlayers.get("white/b_white");
		}
		else if (temp.mImageFile.equals("spawn_p2") && mNumberOfPlayers >= 2)
		{
			Assets.loadPlayer("red/b_red");
			Player p = _world.mPlayers.getFreeObject();
			p.mPosition.set(_positionX, _positionY);
			p.mAnimations = Assets.mPlayers.get("red/b_red");
		}
		else if (temp.mImageFile.equals("spawn_p3") && mNumberOfPlayers >= 3)
		{
			Assets.loadPlayer("blue/b_blue");
			Player p = _world.mPlayers.getFreeObject();
			p.mPosition.set(_positionX, _positionY);
			p.mAnimations = Assets.mPlayers.get("blue/b_blue");
		}
		else if (temp.mImageFile.equals("spawn_p4") && mNumberOfPlayers >= 4)
		{
			Assets.loadPlayer("green/b_green");
			Player p = _world.mPlayers.getFreeObject();
			p.mPosition.set(_positionX, _positionY);
			p.mAnimations = Assets.mPlayers.get("green/b_green");
		}
		//MONSTROS GENERICOS
		else if (temp.mImageFile.contains("generic1"))
		{
			Assets.loadGenericMonster("generic/1/m_generic1");
			Monster m = ObjectFactory.CreateMonster.create(Monster.GENERIC1, _world);
			m.mAnimations = Assets.mMonsters.get("generic/1/m_generic1");
			m.mPosition.set(_positionX, _positionY);
		}
		else if (temp.mImageFile.contains("generic2"))
		{
			Assets.loadGenericMonster("generic/2/m_generic2");
			Monster m = ObjectFactory.CreateMonster.create(Monster.GENERIC2, _world);
			m.mAnimations = Assets.mMonsters.get("generic/2/m_generic2");
			m.mPosition.set(_positionX, _positionY);
		}
		else if (temp.mImageFile.contains("generic3"))
		{
			Assets.loadGenericMonster("generic/3/m_generic3");
			Monster m = ObjectFactory.CreateMonster.create(Monster.GENERIC3, _world);
			m.mAnimations = Assets.mMonsters.get("generic/3/m_generic3");
			m.mPosition.set(_positionX, _positionY);
		}
		else if (temp.mImageFile.contains("generic4"))
		{
			Assets.loadGenericMonster("generic/4/m_generic4");
			Monster m = ObjectFactory.CreateMonster.create(Monster.GENERIC4, _world);
			m.mAnimations = Assets.mMonsters.get("generic/4/m_generic4");
			m.mPosition.set(_positionX, _positionY);
		}
		else if (temp.mImageFile.contains("generic5"))
		{
			Assets.loadGenericMonster("generic/5/m_generic5");
			Monster m = ObjectFactory.CreateMonster.create(Monster.GENERIC5, _world);
			m.mAnimations = Assets.mMonsters.get("generic/5/m_generic5");
			m.mPosition.set(_positionX, _positionY);
		}
		else if (temp.mImageFile.contains("generic6"))
		{
			Assets.loadGenericMonster("generic/6/m_generic6");
			Monster m = ObjectFactory.CreateMonster.create(Monster.GENERIC6, _world);
			m.mAnimations = Assets.mMonsters.get("generic/6/m_generic6");
			m.mPosition.set(_positionX, _positionY);
		}
		else if (temp.mImageFile.contains("generic7"))
		{
			Assets.loadGenericMonster("generic/7/m_generic7");
			Monster m = ObjectFactory.CreateMonster.create(Monster.GENERIC7, _world);
			m.mAnimations = Assets.mMonsters.get("generic/7/m_generic7");
			m.mPosition.set(_positionX, _positionY);
		}
		else if (temp.mImageFile.contains("generic8"))
		{
			Assets.loadGenericMonster("generic/8/m_generic8");
			Monster m = ObjectFactory.CreateMonster.create(Monster.GENERIC8, _world);
			m.mAnimations = Assets.mMonsters.get("generic/8/m_generic8");
			m.mPosition.set(_positionX, _positionY);
		}
		else if (temp.mImageFile.contains("generic9"))
		{
			Assets.loadGenericMonster("generic/9/m_generic9");
			Monster m = ObjectFactory.CreateMonster.create(Monster.GENERIC9, _world);
			m.mAnimations = Assets.mMonsters.get("generic/9/m_generic9");
			m.mPosition.set(_positionX, _positionY);
		}
		else if (temp.mImageFile.contains("generic10"))
		{
			Assets.loadGenericMonster("generic/10/m_generic10");
			Monster m = ObjectFactory.CreateMonster.create(Monster.GENERIC10, _world);
			m.mAnimations = Assets.mMonsters.get("generic/10/m_generic10");
			m.mPosition.set(_positionX, _positionY);
		}
		//MONSTROS NORMAIS
		else if (temp.mImageFile.contains("m_1"))
		{
			Assets.loadGenericMonster("normal/1/m_1");
			Monster m = ObjectFactory.CreateMonster.create(Monster.NORMAL1, _world);
			m.mAnimations = Assets.mMonsters.get("normal/1/m_1");
			m.mPosition.set(_positionX, _positionY);
		}
		else if (temp.mImageFile.contains("m_2"))
		{
			Assets.loadGenericMonster("normal/2/m_2");
			Monster m = ObjectFactory.CreateMonster.create(Monster.NORMAL2, _world);
			m.mAnimations = Assets.mMonsters.get("normal/2/m_2");
			m.mPosition.set(_positionX, _positionY);
		}
		else if (temp.mImageFile.contains("m_3"))
		{
			Assets.loadGenericMonster("normal/3/m_3");
			Monster m = ObjectFactory.CreateMonster.create(Monster.NORMAL3, _world);
			m.mAnimations = Assets.mMonsters.get("normal/3/m_3");
			m.mPosition.set(_positionX, _positionY);
		}
		else if (temp.mImageFile.contains("m_4"))
		{
			Assets.loadGenericMonster("normal/4/m_4");
			Monster m = ObjectFactory.CreateMonster.create(Monster.NORMAL4, _world);
			m.mAnimations = Assets.mMonsters.get("normal/4/m_4");
			m.mPosition.set(_positionX, _positionY);
		}
		else if (temp.mImageFile.contains("m_5"))
		{
			Assets.loadGenericMonster("normal/5/m_5");
			Monster m = ObjectFactory.CreateMonster.create(Monster.NORMAL5, _world);
			m.mAnimations = Assets.mMonsters.get("normal/5/m_5");
			m.mPosition.set(_positionX, _positionY);
		}
	}

}
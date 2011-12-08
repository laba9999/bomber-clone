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

	public String mName;
	public short mRows;
	public short mColumns;
	public short[][] mWalkableIDs;
	public short[][] mCollidableIDs;
	public short[][] mDestroyableIDs;
	public short[][] mSpawnIDs;

	public HashMap<Short, ImageTile> mImageTiles = new HashMap<Short, ImageTile>();

	public static void main(String[] args)
	{ // TODO: delete this
		Level l = new Level();
		l.loadLevel(1, null);
	}

	public void loadLevel(int _levelID, GameWorld _world)
	{
		// TODO: use _levelID

		try
		{

			// setup parser
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader xmlReader = parser.getXMLReader();

			// parse level xml file
			LevelXMLHandler myLevelXMLHandler = new LevelXMLHandler(this);

			xmlReader.setContentHandler(myLevelXMLHandler);
			File file = new File(".\\levels\\level1\\level1.xml");
			InputStream inputStream = new FileInputStream(file);
			Reader reader = new InputStreamReader(inputStream, "UTF-8");
			xmlReader.parse(new InputSource(reader));

			// parse tileset xml file
			TilesetXMLHandler myTilesetXMLHandler = new TilesetXMLHandler(this);

			xmlReader.setContentHandler(myTilesetXMLHandler);
			file = new File(".\\levels\\level1\\tileset.xml");
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

	private void setupLevel(GameWorld _world)
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

			}
		}
		
		// TODO : tratar mSpawns

		
	}

}
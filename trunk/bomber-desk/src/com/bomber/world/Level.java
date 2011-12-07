package com.bomber.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bomber.common.ObjectsPool;
import com.bomber.gameobjects.Monster;
import com.bomber.gameobjects.Tile;




public class Level {

	/**
	 * Serve também para fazer o reset.
	 */
	
	

	public static void main(String[] args) { //TODO: delete this
		Level l = new Level();
		l.loadLevel(1, null);
	}
	
	public void loadLevel(int _levelID, GameWorld _world)
	{
		//TODO: use _levelID
        
		try {
			
			//setup parser
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
	        XMLReader xmlReader = parser.getXMLReader();	  
	        
	        //parse level xml file
	        LevelXMLHandler myLevelXMLHandler = new LevelXMLHandler();
	        
	        xmlReader.setContentHandler(myLevelXMLHandler);   
	        File file = new File(".\\levels\\level1\\level1.xml");			
			InputStream inputStream= new FileInputStream(file);
			Reader reader = new InputStreamReader(inputStream,"UTF-8");
	        xmlReader.parse( new InputSource(reader));
	        
	        //parse tileset xml file
	        TilesetXMLHandler myTilesetXMLHandler = new TilesetXMLHandler();
	       
	        xmlReader.setContentHandler( myTilesetXMLHandler);  
			file = new File(".\\levels\\level1\\tileset.xml");			
			inputStream= new FileInputStream(file);
			reader = new InputStreamReader(inputStream,"UTF-8");
	        xmlReader.parse( new InputSource(reader));

	        //setup level
	       // _world.mMap.reset(myLevelXMLHandler.mRows,myLevelXMLHandler.mColumns);
	        

	        
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
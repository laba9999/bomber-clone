package com.bomber.common.assets;

import com.badlogic.gdx.utils.XmlReader;

public class LevelXMLHandler extends XmlReader {

	boolean mIsWalkables = false;
	boolean mIsDestroyables = false;
	boolean mIsCollidables = false;
	boolean mIsSpawns = false;
	boolean mIsFlags = false;


	@Override
	protected void attribute(String name, String value) {
		// TODO Auto-generated method stub
		super.attribute(name, value);
		System.out.println("attribute(name,value)" + name + "......" + value);
		if(name.equals("rows"))
		{
			Level.mRows = new Short(value).shortValue();
		}else if(name.equals("columns"))
		{
			Level.mColumns = new Short(value).shortValue();
		}else if(value.equals("walkables"))
		{
			mIsWalkables = true;
		}else if(value.equals("collidables"))
		{
			mIsCollidables = true;
		}else if(value.equals("destroyables"))
		{
			mIsDestroyables = true;
		}else if(value.equals("spawns"))
		{
			mIsSpawns = true;
		}else if(value.equals("flags"))
		{
			mIsFlags = true;
		}
	}

	@Override
	protected void text(String text) {
		// TODO Auto-generated method stub
		super.text(text);
		System.out.println("text(text) " + text);
		
		
		// separa dados através de separador não numérico
		String[] splitted = text.split("\\D");

		int i = 0;
		int j = 0;

		short[][] parsedValues = new short[Level.mRows][Level.mColumns];

		// converte array unidimensional de String para array bidimensional de
		// short
		for (int k = 0; k < splitted.length; k++)
		{
			if (!splitted[k].equals(""))
			{

				parsedValues[i][j] = Short.parseShort(splitted[k]);

				j++;
				if (j == Level.mColumns)
				{
					// terminou todas as colunas -> proxima linha
					j = 0;
					i++;
				}
			}
		}

		
		if(mIsWalkables){
			Level.mWalkableIDs = parsedValues;
			mIsWalkables = false;
		}else if(mIsDestroyables){
			Level.mDestroyableIDs = parsedValues;
			mIsDestroyables = false;
		}else if(mIsCollidables){
			Level.mCollidableIDs = parsedValues;
			mIsCollidables = false;
		}else if(mIsSpawns){
			Level.mSpawnIDs = parsedValues;
			mIsSpawns = false;
		}else if(mIsFlags){
			Level.mFlags = parsedValues;
			mIsFlags = false;
		}
	}

	
	
}

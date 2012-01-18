package com.bomber.common.assets;

import com.badlogic.gdx.utils.XmlReader;

public class TilesetXMLHandler extends XmlReader {
	private short mId;
	private String mFile;
	
	@Override
	protected void attribute(String name, String value) {
		// TODO Auto-generated method stub
		super.attribute(name, value);
		System.out.println("attribute(name,value)" + name + "......" + value);
		
		if(name.equals("id"))
		{
			mId = new Short(value).shortValue();
		}else if(name.equals("file"))
		{
			String[] splittedFileName = value.split(".png");
			// ignora ".png"
			mFile = splittedFileName[0];
			
			Level.mImageTiles.put(mId, mFile);
		}
	}

	@Override
	protected void text(String text) {

	}

}

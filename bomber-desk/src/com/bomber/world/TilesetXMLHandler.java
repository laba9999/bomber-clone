package com.bomber.world;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TilesetXMLHandler extends DefaultHandler
{
	// parsed values go to Level

	// parsing helpers
	private short mId;
	private String mFile;
	private boolean mFlipVertically;
	private boolean mFlipHorizontally;


	@Override
	public void endElement(String _uri, String _localName, String _qName)
			throws SAXException
	{

		if (_qName.equals("imageTile"))
		{

			ImageTile temp = new ImageTile(mFile, mFlipHorizontally,
					mFlipHorizontally);
			Level.mImageTiles.put(mId, temp);

		}

	}

	@Override
	public void startElement(String _uri, String _localName, String _qName,
			Attributes _attributes) throws SAXException
	{

		if (_qName.equals("imageTile"))
		{

			mId = Short.parseShort(_attributes.getValue("id"));

		} else if (_qName.equals("image"))
		{

			String[] splittedFileName = _attributes.getValue("file").split(".png");
			//ignora ".png"
			mFile = splittedFileName[0];

		} else if (_qName.equals("flip"))
		{

			mFlipVertically = Boolean.parseBoolean(_attributes
					.getValue("vertically"));
			mFlipHorizontally = Boolean.parseBoolean(_attributes
					.getValue("horizontally"));

		}

	}

	@Override
	public void endDocument() throws SAXException
	{
		// TODO Auto-generated method stub
		super.endDocument();
	}

}

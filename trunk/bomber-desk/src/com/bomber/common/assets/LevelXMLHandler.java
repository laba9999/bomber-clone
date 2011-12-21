package com.bomber.common.assets;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class LevelXMLHandler extends DefaultHandler
{

	// valores extraídos vão para o Level
	// auxiliares
	private String mCurrentLayer = null;
	private boolean mIsData = false;
	private StringBuilder mText = new StringBuilder();

	@Override
	public void startElement(String _uri, String _localName, String _qName, Attributes _attributes) throws SAXException
	{

		if (_qName.equals("tilemap"))
		{

			Level.mName = _attributes.getValue("name");
			Level.mRows = Short.parseShort(_attributes.getValue("rows"));
			Level.mColumns = Short.parseShort(_attributes.getValue("columns"));

		} else if (_qName.equals("layer"))
		{

			mCurrentLayer = _attributes.getValue("name");

		} else if (_qName.equals("data"))
		{

			mIsData = true;

		}
	}

	@Override
	public void endElement(String _uri, String _localName, String _qName) throws SAXException
	{

		if (_qName.equals("data"))
		{
			parseDataTagText();
			// clean up
			mText.delete(0, mText.length());
			mIsData = false;
		}
	}

	private void parseDataTagText()
	{
		// separa dados através de separados não numérico
		String[] splitted = mText.toString().split("\\D");

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

		if (mCurrentLayer.equals("walkables"))
		{
			Level.mWalkableIDs = parsedValues;
		} else if (mCurrentLayer.equals("destroyables"))
		{
			Level.mDestroyableIDs = parsedValues;
		} else if (mCurrentLayer.equals("spawns"))
		{
			Level.mSpawnIDs = parsedValues;
		} else if (mCurrentLayer.equals("collidables"))
		{
			Level.mCollidableIDs = parsedValues;
		}else if (mCurrentLayer.equals("flags"))
		{
			Level.mFlags = parsedValues;
		}

	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{

		if (mIsData)
		{
			// append o texto extraído para que possa ser tratado apenas no
			// endElement()
			mText.append(new String(ch, start, length));
		}

	}

}

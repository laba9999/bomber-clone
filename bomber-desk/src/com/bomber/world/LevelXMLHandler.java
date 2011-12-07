package com.bomber.world;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class LevelXMLHandler extends DefaultHandler {
	
	// parsed values go to Level
	private Level mLevel;


	// parsing helpers
	private String mCurrentLayer = null;
	private boolean mIsData = false;
	private StringBuilder mText = new StringBuilder();

	
	
	public LevelXMLHandler(Level _level) {
		mLevel = _level;
	}

	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
	}

	@Override
	public void startElement(String _uri, String _localName, String _qName,
			Attributes _attributes) throws SAXException {

		if (_qName.equals("tilemap")) {
			
			mLevel.mName = _attributes.getValue("name");
			mLevel.mRows = Short.parseShort(_attributes.getValue("rows"));
			mLevel.mColumns = Short.parseShort(_attributes.getValue("columns"));

		} else if (_qName.equals("layer")) {
			
			mCurrentLayer = _attributes.getValue("name");

		} else if (_qName.equals("data")) {

			mIsData = true;

		}
	}

	@Override
	public void endElement(String _uri, String _localName, String _qName)
			throws SAXException {

		if (_qName.equals("data")) {
			parseDataTagText();
			// cleanup
			mText.delete(0, mText.length());
			mIsData = false;
		}
	}

	private void parseDataTagText() {
		// split by non digits
		String[] splitted = mText.toString().split("\\D");

		int i = 0;
		int j = 0;

		short[][] parsedValues = new short[mLevel.mRows][mLevel.mColumns];

		// convert unidimensional String array to bidimensional short array
		for (int k = 0; k < splitted.length; k++) {
			if (!splitted[k].equals("")) {

				parsedValues[i][j] = Short.parseShort(splitted[k]);

				j++;
				if (j == mLevel.mColumns) {
					// finished all columns -> next row
					j = 0;
					i++;
				}
			}
		}

		if (mCurrentLayer.equals("walkable")) {
			mLevel.mWalkableIDs = parsedValues;
		} else if (mCurrentLayer.equals("destroyables")) {
			mLevel.mDestroyableIDs = parsedValues;
		} else if (mCurrentLayer.equals("spawns")) {
			mLevel.mSpawnIDs = parsedValues;
		} else if (mCurrentLayer.equals("collidables")) {
			mLevel.mCollidableIDs = parsedValues;
		}

	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		if (mIsData) {
			// append whole text so it can be handled at endElement
			mText.append(new String(ch, start, length));
		}

	}

}

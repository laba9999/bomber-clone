package com.bomber.world;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class LevelXMLHandler extends DefaultHandler {
	// parsed values
	public String mName;
	public short mRows;
	public short mColumns;
	public short[][] mWalkableIDs;
	public short[][] mCollidableIDs;
	public short[][] mDestroyableIDs;
	public short[][] mSpawnIDs;

	// parsing helpers
	private String mCurrentLayer = null;
	private boolean mIsData = false;
	private StringBuilder mText = new StringBuilder();

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
			
			mName = _attributes.getValue("name");
			mRows = Short.parseShort(_attributes.getValue("rows"));
			mColumns = Short.parseShort(_attributes.getValue("columns"));

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

		short[][] parsedValues = new short[mRows][mColumns];

		// convert unidimensional String array to bidimensional short array
		for (int k = 0; k < splitted.length; k++) {
			if (!splitted[k].equals("")) {

				parsedValues[i][j] = Short.parseShort(splitted[k]);

				j++;
				if (j == mColumns) {
					// finished all columns -> next row
					j = 0;
					i++;
				}
			}
		}

		if (mCurrentLayer.equals("walkable")) {
			mWalkableIDs = parsedValues;
		} else if (mCurrentLayer.equals("destroyables")) {
			mDestroyableIDs = parsedValues;
		} else if (mCurrentLayer.equals("spawns")) {
			mSpawnIDs = parsedValues;
		} else if (mCurrentLayer.equals("collidables")) {
			mCollidableIDs = parsedValues;
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

package com.bomber;

import com.bomber.gameobjects.UIMovableObject;

public class OverlayingText extends UIMovableObject {

	public String mText;

	public OverlayingText() {
		super(3, 0, 0, 0.02f, 0.08f, 100);
	}

	public void set(String _text, float _startX, float _startY)
	{
		mText = _text;
		mPosition.set(_startX, _startY);
	}
}

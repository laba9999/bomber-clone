package com.amov.bomber;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

/*
 * Custom Gallery que faz scroll de elemento em elemento.
 */
public class FlingGallery extends Gallery
{

	public FlingGallery(Context context)
	{
		super(context);
	}

	public FlingGallery(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public FlingGallery(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY)
	{
		return false;
	}

}

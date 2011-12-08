package com.bomber;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class BomberDesktop {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		new LwjglApplication(new GameScreen(), "Bomber", 800, 480, false);

	}

}

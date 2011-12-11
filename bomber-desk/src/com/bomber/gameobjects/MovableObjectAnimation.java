package com.bomber.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Pode ser usada com os players e os montros, os monstros genericos ficam com
 * todas as animações a apontar para a mesma referencia.
 */
public class MovableObjectAnimation {
	public short numberOfFramesPerWalk;
	public short numberOfFramesDying;
	
	public Animation walk[] = new Animation[4];
	
	public Animation die;
}
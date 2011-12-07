package com.bomber.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Pode ser usada com os players e os montros, os monstros genericos ficam com
 * todas as anima��es a apontar para a mesma referencia.
 */
public class MovableObjectAnimation {
	public Animation walkLeft;
	public Animation walkRight;
	public Animation walkUp;
	public Animation walkdown;
	public Animation die;
}
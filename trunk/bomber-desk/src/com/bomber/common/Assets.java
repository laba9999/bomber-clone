package com.bomber.common;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bomber.gameobjects.MovableObjectAnimation;

public class Assets {
	private Texture mAtlas;
	private HashMap<String, MovableObjectAnimation> mMonsters;
	private HashMap<String, MovableObjectAnimation> mPlayers;
	private HashMap<String, TextureRegion> mPlayersHeads;
	private HashMap<String, Animation> mBonus;
	private HashMap<String, TextureRegion> mNonDestroyableTiles;
	/**
	 * Posição 0 da animação é o tile antes de ser destruido. Daí para a frente
	 * é a destruição do tile.
	 */
	private HashMap<String, Animation> mDestroyableTiles;
	private Animation mBomb;
	private TextureRegion mMainScreen;
	private Animation mSoundButton;
	private HashMap<String, TextureRegion> mPauseButtons;
	private TextureRegion mControllerBar;
	/**
	 * Usado no pause.
	 */
	private TextureRegion mDarkGlass;
	/**
	 * Usado no pause.
	 */
	private TextureRegion mPauseScreen;
	private BitmapFont mFont;
}
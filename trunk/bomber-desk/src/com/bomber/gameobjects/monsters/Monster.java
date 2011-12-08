package com.bomber.gameobjects.monsters;

import java.util.HashMap;

import com.bomber.gameobjects.KillableObject;
import com.bomber.gameobjects.Player;
import com.bomber.world.GameWorld;

public abstract class Monster extends KillableObject {
	public static final HashMap<Short, String> mType = new HashMap<Short, String>();
	
	public static final short GENERIC1 = 0;
	public static final short GENERIC2 = 1;
	public static final short GENERIC3 = 2;
	public static final short GENERIC4 = 3;
	public static final short GENERIC5 = 4;
	public static final short GENERIC6 = 5;
	public static final short GENERIC7 = 6;
	public static final short GENERIC8 = 7;
	public static final short GENERIC9 = 8;
	public static final short GENERIC10 = 9;
	
	public static final short NORMAL1 = 10;
	public static final short NORMAL2 = 11;
	public static final short NORMAL3 = 12;
	public static final short NORMAL4 = 13;
	public static final short NORMAL5 = 14;
	
	private int mPointsValue;
	private short mMonsterType;

	Monster(GameWorld _world) {
		mWorld = _world;
	}

	@Override
	public void update()
	{
		super.update();

		// Verifica se o monstro está morto
		if (mIsDead && mLooped)
			mWorld.mMonsters.releaseObject(this);
	}

	public abstract void interactWithPlayer(Player _player);
	
	public static void setTypes()
	{
		
		mType.put(GENERIC1,"generic/m_generic1");
		mType.put(GENERIC2,"generic/m_generic2");
		mType.put(GENERIC3,"generic/m_generic3");
		mType.put(GENERIC4,"generic/m_generic4");
		mType.put(GENERIC5,"generic/m_generic5");
		mType.put(GENERIC6,"generic/m_generic6");
		mType.put(GENERIC7,"generic/m_generic7");
		mType.put(GENERIC8,"generic/m_generic8");
		mType.put(GENERIC9,"generic/m_generic9");
		mType.put(GENERIC10,"generic/m_generic10");
		mType.put(NORMAL1, "normal/m_1");
		mType.put(NORMAL2, "normal/m_2");
		mType.put(NORMAL3, "normal/m_3");
		mType.put(NORMAL4, "normal/m_4");
		mType.put(NORMAL5, "normal/m_5");
	}
}
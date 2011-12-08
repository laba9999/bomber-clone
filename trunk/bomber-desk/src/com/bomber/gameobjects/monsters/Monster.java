package com.bomber.gameobjects.monsters;

import java.util.HashMap;

import com.bomber.gameobjects.KillableObject;
import com.bomber.gameobjects.Player;
import com.bomber.world.GameWorld;

public abstract class Monster extends KillableObject {
	public static HashMap<String, Short> TYPE = null;
	
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
	
	public static short getTypeFromString(String _str)
	{
		if(null == TYPE) 
		{
			TYPE = new HashMap<String, Short>();
			TYPE.put("m_generic1", GENERIC1);
			TYPE.put("m_generic2", GENERIC2);
			TYPE.put("m_generic3", GENERIC3);
			TYPE.put("m_generic4", GENERIC4);
			TYPE.put("m_generic5", GENERIC5);
			TYPE.put("m_generic6", GENERIC6);
			TYPE.put("m_generic7", GENERIC7);
			TYPE.put("m_generic8", GENERIC8);
			TYPE.put("m_generic9", GENERIC9);
			TYPE.put("m_generic10", GENERIC10);
			TYPE.put("m_1", NORMAL1);
			TYPE.put("m_2", NORMAL2);
			TYPE.put("m_3", NORMAL3);
			TYPE.put("m_4", NORMAL4);
			TYPE.put("m_5", NORMAL5);
		}
		return TYPE.get(_str);
	}
		
	
		
}
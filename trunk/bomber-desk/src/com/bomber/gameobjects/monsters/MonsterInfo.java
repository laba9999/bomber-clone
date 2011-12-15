package com.bomber.gameobjects.monsters;

import java.util.HashMap;

public class MonsterInfo
{
	private static HashMap<String, MonsterInfo> TYPES = null;
	
	public int mPointsValue;
	public boolean mAbleToFly;
	public boolean mAbleToKill;
	public String mPointsValueString;
	
	public MonsterInfo(int _pointsValue, boolean _ableToKill, boolean _ableToFly)
	{
		mPointsValueString = "+" + String.valueOf(_pointsValue);
		mPointsValue = _pointsValue;
		mAbleToKill  = _ableToKill;
		mAbleToFly = _ableToFly;
	}
	
	public static MonsterInfo getInfoFromType(String _type)
	{
		if(TYPES == null)
		{
			TYPES = new HashMap<String, MonsterInfo>();
			
			// Não matam e não voam
			MonsterInfo tmpInfo = new MonsterInfo(100, false, false);
			
			TYPES.put("m_generic1", tmpInfo);
			TYPES.put("m_generic2", tmpInfo);
			TYPES.put("m_generic3", tmpInfo);
			TYPES.put("m_generic4", tmpInfo);
			TYPES.put("m_generic5", tmpInfo);
			TYPES.put("m_generic6", tmpInfo);
			TYPES.put("m_generic7", tmpInfo);
			TYPES.put("m_generic8", tmpInfo);
			TYPES.put("m_generic9", tmpInfo);
			TYPES.put("m_generic10", tmpInfo);
			
			
			// Matam mas não voam
			tmpInfo = new MonsterInfo(250, true, false);
			
			TYPES.put("m_2", tmpInfo);
			TYPES.put("m_3", tmpInfo);
			TYPES.put("m_4", tmpInfo);
			TYPES.put("m_5", tmpInfo);
			
			// Matam e voam
			tmpInfo = new MonsterInfo(500, true, true);
			
			TYPES.put("m_1", tmpInfo);
		}
		
		return TYPES.get(_type);
	}

}
package com.bomber.common;

import java.util.HashMap;

public class Strings {
	
	public static final String[] DEFAULT_STRINGS = new String[]{
		"Connect to - ",
		"Connecting... ",
		"Error connecting...",
		"PONTUAÇÃO FINAL",
		"MELHOR PONTUAÇÃO",
		"Loading...",
		"Lost client...",
		"Lost server...",
		"No suficient clients...",
		"Round ",
		"PONTOS: ",
		"Starts in ",
		"starts in ",
		"TEMPO",
		"Waiting for clients...",
		"YOU WON!!!",
		"YOU LOST..."
	};

	
	public static final String[] GAME_STRINGS_KEYS = new String[]{
		"connect_to",
		"connecting",
		"error_connecting",
		"final_score",
		"highscore",
		"loading",
		"lost_client",
		"lost_server",
		"no_suficient_clients",
		"round",
		"score",
		"starts_in",
		"starts_in_lowercase",
		"time",
		"waiting_clients",
		"won",
		"lost"
	};
	
	//public static final int NUMBER_GAME_STRINGS = 17;

	
	public static HashMap<String, String> mStrings;

	public static void loadForGdx()
	{
		
		if(Strings.mStrings != null)
			return;			
		mStrings = new HashMap<String, String>(GAME_STRINGS_KEYS.length);
		String str;
		
		for(int i = 0; i < GAME_STRINGS_KEYS.length; i++)
		{
			str = DEFAULT_STRINGS[i];
			Strings.mStrings.put(Strings.GAME_STRINGS_KEYS[i],str);
		}
	}
}

package com.bomber.common;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bomber.gameobjects.MovableObjectAnimation;

/**
 * @author Filipe
 * 
 */
public class Assets {
	private final float FRAME_DURATION = 0.3f;
	private final String ATLAS_FILE = "assets/atlas.txt";

	private TextureAtlas mAtlas;
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

	private void loadAssets() {
		loadAtlas();
		loadPlayerAnimations();
		loadMonsterAnimations();
		loadPlayersHeads();
		loadBonus();
		//TODO : load the rest...
	}

	private void loadAtlas() {
		mAtlas = new TextureAtlas(Gdx.files.internal(ATLAS_FILE));
	}

	private void loadPlayerAnimations() {

		short dieFrames = 10;
		short walkFrames = 3;

		MovableObjectAnimation temp = loadCompositeMovableObjectAnimation(
				"b_white", dieFrames, walkFrames);
		mPlayers.put("b_white", temp);

		temp = loadCompositeMovableObjectAnimation("b_red", dieFrames,
				walkFrames);
		mPlayers.put("b_red", temp);

		temp = loadCompositeMovableObjectAnimation("b_blue", dieFrames,
				walkFrames);
		mPlayers.put("b_blue", temp);

		temp = loadCompositeMovableObjectAnimation("b_green", dieFrames,
				walkFrames);
		mPlayers.put("b_green", temp);
	}

	/**
	 * Carrega animations de GameObjects com estados _die_ , _walk_up_ ,
	 * _walk_down_ , _walk_left e _walk_right Útil para Players e Monsters
	 */
	private MovableObjectAnimation loadCompositeMovableObjectAnimation(
			String _id, short _dieFrames, short _walkFrames) {

		MovableObjectAnimation movableAnimation = new MovableObjectAnimation();

		// load die animation
		movableAnimation.die = loadAnimation(_id + "_die_", _dieFrames);

		// load walkup animation
		movableAnimation.walkUp = loadAnimation(_id + "_walk_up_", _dieFrames);

		// load walkdown animation
		movableAnimation.walkUp = loadAnimation(_id + "_walk_down_", _dieFrames);

		// load walkleft animation
		movableAnimation.walkUp = loadAnimation(_id + "_walk_left_", _dieFrames);

		// load walkright animation
		movableAnimation.walkUp = loadAnimation(_id + "_walk_right_",
				_dieFrames);

		return movableAnimation;
	}

	/**
	 * Carrega animação identificada por _id com _howManyFrames frames Ex :
	 * loadAnimation("b_white_die_",10);
	 */
	private Animation loadAnimation(String _id, short _howManyFrames) {
		ArrayList<TextureRegion> regions = new ArrayList<TextureRegion>();

		for (int i = 0; i < _howManyFrames; i++) {
			TextureRegion r = mAtlas.findRegion(_id, i);
			regions.add(r);
		}
		
	
		return new Animation(FRAME_DURATION, regions);
		
	}

	private void loadMonsterAnimations() {

		// load normal monsters
		short dieFrames = 6;
		short walkFrames = 3;

		MovableObjectAnimation temp = loadCompositeMovableObjectAnimation(
				"m_1", dieFrames, walkFrames);
		mMonsters.put("m_1", temp);

		temp = loadCompositeMovableObjectAnimation("m_2", dieFrames, walkFrames);
		mMonsters.put("m_2", temp);

		temp = loadCompositeMovableObjectAnimation("m_3", dieFrames, walkFrames);
		mMonsters.put("m_3", temp);

		temp = loadCompositeMovableObjectAnimation("m_4blue", dieFrames,
				walkFrames);
		mMonsters.put("m_4blue", temp);

		temp = loadCompositeMovableObjectAnimation("m_4green", dieFrames,
				walkFrames);
		mMonsters.put("m_4green", temp);

		// load generic monsters
		temp = loadGenericMonsterMovableObjectAnimations("m_generic1");
		mMonsters.put("m_generic1", temp);
		temp = loadGenericMonsterMovableObjectAnimations("m_generic2");
		mMonsters.put("m_generic2", temp);
		temp = loadGenericMonsterMovableObjectAnimations("m_generic3");
		mMonsters.put("m_generic3", temp);
		temp = loadGenericMonsterMovableObjectAnimations("m_generic4");
		mMonsters.put("m_generic4", temp);
		temp = loadGenericMonsterMovableObjectAnimations("m_generic5");
		mMonsters.put("m_generic5", temp);
		temp = loadGenericMonsterMovableObjectAnimations("m_generic6");
		mMonsters.put("m_generic6", temp);

	}

	private MovableObjectAnimation loadGenericMonsterMovableObjectAnimations(
			String _id) {
		short dieFrames = 6;
		short walkFrames = 3;

		MovableObjectAnimation movableAnimation = new MovableObjectAnimation();
		movableAnimation.die = loadAnimation(_id + "_die_", dieFrames);
		movableAnimation.walkUp = loadAnimation(_id + "_walk_", walkFrames);
		movableAnimation.walkDown = movableAnimation.walkUp;
		movableAnimation.walkLeft = movableAnimation.walkUp;
		movableAnimation.walkRight = movableAnimation.walkUp;

		return movableAnimation;
	}
	
	
	private void loadPlayersHeads() {
		
		TextureRegion r = mAtlas.findRegion("face_white");
		mPlayersHeads.put("face_white", r);
		
		r = mAtlas.findRegion("face_green");		
		mPlayersHeads.put("face_green", r);
		
		r = mAtlas.findRegion("face_red");
		mPlayersHeads.put("face_red", r);
		
		r = mAtlas.findRegion("face_blue");		
		mPlayersHeads.put("face_blue", r);
		
	}
	
	private void loadBonus() {
		short frames = 6;
		
		mBonus.put("bonus_bomb_", loadAnimation("bonus_bomb_", frames));
		mBonus.put("bonus_hand_", loadAnimation("bonus_hand_", frames));
		mBonus.put("bonus_life_", loadAnimation("bonus_life_", frames));
		mBonus.put("bonus_potion_", loadAnimation("bonus_potion_", frames));
		mBonus.put("bonus_shield_", loadAnimation("bonus_shield_", frames));
		mBonus.put("bonus_speed_", loadAnimation("bonus_speed_", frames));
		mBonus.put("bonus_start_", loadAnimation("bonus_start_", frames));
	}
	
	

}
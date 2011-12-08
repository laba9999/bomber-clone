package com.bomber.common;

import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bomber.gameobjects.MovableObjectAnimation;

/**
 * @author Filipe
 * 
 */
public class Assets
{
	private static final float FRAME_DURATION = 0.3f;
	private static final String ATLAS_FILE = "assets/atlas.txt";

	public static TextureAtlas mAtlas;
	public static HashMap<String, MovableObjectAnimation> mMonsters;
	public static HashMap<String, MovableObjectAnimation> mPlayers;
	public static HashMap<String, TextureRegion> mPlayersHeads;
	public static HashMap<String, Animation> mBonus;
	public static HashMap<String, TextureRegion> mNonDestroyableTiles;

	/**
	 * Posição 0 da animação é o tile antes de ser destruido. Daí para a frente
	 * é a destruição do tile.
	 */
	public static HashMap<String, Animation> mDestroyableTiles;
	public static HashMap<String, Animation> mExplosions;

	public static Animation mBomb;
	public static TextureRegion mMainScreen;
	public static Animation mSoundButton;
	public static HashMap<String, TextureRegion> mPauseButtons;
	public static TextureRegion mControllerBar;
	/**
	 * Usado no pause.
	 */
	public static TextureRegion mDarkGlass;
	/**
	 * Usado no pause.
	 */
	public static TextureRegion mPauseScreen;
	public static BitmapFont mFont;

	public static void loadAssets()
	{
		loadAtlas();
		loadPlayerAnimations();
		loadPlayersHeads();
		loadExplosions();
		loadBonus();
		loadBomb();
		// loadUI();
	}

	private static void loadAtlas()
	{
		mAtlas = new TextureAtlas(Gdx.files.internal(ATLAS_FILE));
	}

	private static void loadPlayerAnimations()
	{
		short dieFrames = 10;
		short walkFrames = 3;

		MovableObjectAnimation temp = loadCompositeMovableObjectAnimation("b_white", dieFrames, walkFrames);
		mPlayers.put("b_white", temp);

		temp = loadCompositeMovableObjectAnimation("b_red", dieFrames, walkFrames);
		mPlayers.put("b_red", temp);

		temp = loadCompositeMovableObjectAnimation("b_blue", dieFrames, walkFrames);
		mPlayers.put("b_blue", temp);

		temp = loadCompositeMovableObjectAnimation("b_green", dieFrames, walkFrames);
		mPlayers.put("b_green", temp);
	}

	/**
	 * Cria MovableObjectAnimation com Animations de GameObjects com estados
	 * _die_ , _walk_up_ , _walk_down_ , _walk_left e _walk_right. Útil para
	 * Players e Monsters
	 */
	private static MovableObjectAnimation loadCompositeMovableObjectAnimation(String _id, short _dieFrames, short _walkFrames)
	{

		MovableObjectAnimation movableAnimation = new MovableObjectAnimation();

		// load die animation
		movableAnimation.die = loadAnimation(_id + "_die_");

		// load walkup animation
		movableAnimation.walkUp = loadAnimation(_id + "_walk_up_");

		// load walkdown animation
		movableAnimation.walkUp = loadAnimation(_id + "_walk_down_");

		// load walkleft animation
		movableAnimation.walkUp = loadAnimation(_id + "_walk_left_");

		// load walkright animation
		movableAnimation.walkUp = loadAnimation(_id + "_walk_right_");

		movableAnimation.numberOfFramesDying = _dieFrames;
		movableAnimation.numberOfFramesPerWalk = _walkFrames;

		return movableAnimation;
	}

	/**
	 * Carrega animação identificada por _id Ex : loadAnimation("b_white_die_");
	 */
	private static Animation loadAnimation(String _id)
	{
		List<AtlasRegion> regions = mAtlas.findRegions(_id);

		return new Animation(FRAME_DURATION, regions);

	}

	public static void loadGenericMonster(String _id)
	{
		MovableObjectAnimation temp = loadGenericMonsterMovableObjectAnimations(_id);
		mMonsters.put(_id, temp);
	}

	public static void loadNormalMonster(String _id)
	{
		short dieFrames = 6;
		short walkFrames = 3;

		MovableObjectAnimation temp = loadCompositeMovableObjectAnimation(_id, dieFrames, walkFrames);
		mMonsters.put(_id, temp);
	}

	/**
	 * Cria MovableObjectAnimation de monstros genéricos com Animations _die_ e
	 * _walk_.
	 */
	private static MovableObjectAnimation loadGenericMonsterMovableObjectAnimations(String _id)
	{
		short dieFrames = 6;
		short walkFrames = 3;

		MovableObjectAnimation movableAnimation = new MovableObjectAnimation();
		movableAnimation.die = loadAnimation(_id + "_die_");
		movableAnimation.walkUp = loadAnimation(_id + "_walk_");
		movableAnimation.walkDown = movableAnimation.walkUp;
		movableAnimation.walkLeft = movableAnimation.walkUp;
		movableAnimation.walkRight = movableAnimation.walkUp;

		movableAnimation.numberOfFramesDying = dieFrames;
		movableAnimation.numberOfFramesPerWalk = walkFrames;

		return movableAnimation;
	}

	private static void loadPlayersHeads()
	{

		TextureRegion r = mAtlas.findRegion("face_white");
		mPlayersHeads.put("face_white", r);

		r = mAtlas.findRegion("face_green");
		mPlayersHeads.put("face_green", r);

		r = mAtlas.findRegion("face_red");
		mPlayersHeads.put("face_red", r);

		r = mAtlas.findRegion("face_blue");
		mPlayersHeads.put("face_blue", r);

	}

	private static void loadBonus()
	{

		mBonus.put("bonus_bomb", loadAnimation("bonus_bomb_"));
		mBonus.put("bonus_hand", loadAnimation("bonus_hand_"));
		mBonus.put("bonus_life", loadAnimation("bonus_life_"));
		mBonus.put("bonus_potion", loadAnimation("bonus_potion_"));
		mBonus.put("bonus_shield", loadAnimation("bonus_shield_"));
		mBonus.put("bonus_speed", loadAnimation("bonus_speed_"));
		mBonus.put("bonus_star", loadAnimation("bonus_star_"));
	}

	public static void loadDestroyableTile(String _id)
	{
		mDestroyableTiles.put(_id, loadAnimation(_id + "_destroy_"));
	}

	public static void loadNonDestroyableTile(String _id, boolean _flipHorizontally, boolean _flipVertically)
	{
		// _id = "tiles_2" : ignorando 6 chars, temos o index
		int tileIndex = Integer.parseInt(_id.substring(6));

		TextureRegion r = mAtlas.findRegion("tiles_", tileIndex);
		// TODO : verificar se não há engano nestes parametros
		r.flip(_flipVertically, _flipHorizontally);
		mNonDestroyableTiles.put(_id, r);
	}

	private static void loadExplosions()
	{
		mExplosions.put("xplode_center", loadAnimation("xplode_center_"));
		mExplosions.put("xplode_mid_hor", loadAnimation("xplode_mid_hor_"));
		mExplosions.put("xplode_mid_ver", loadAnimation("xplode_mid_ver_"));
		mExplosions.put("xplode_tip_down", loadAnimation("xplode_tip_down_"));
		mExplosions.put("xplode_tip_left", loadAnimation("xplode_tip_left_"));
		mExplosions.put("xplode_tip_right", loadAnimation("xplode_tip_right_"));
		mExplosions.put("xplode_tip_up", loadAnimation("xplode_tip_up_"));
	}

	private static void loadBomb()
	{
		mBomb = loadAnimation("bomb_");
	}

	private static void loadUI()
	{
		// TODO : definir o IDs para estes componentes
		mMainScreen = mAtlas.findRegion("TO BE DEFINED");
		mSoundButton = loadAnimation("TO BE DEFINED");
		mPauseButtons.put("TO BE DEFINED", mAtlas.findRegion("TO BE DEFINED"));
		mControllerBar = mAtlas.findRegion("TO BE DEFINED");
		mDarkGlass = mAtlas.findRegion("TO BE DEFINED");
		mPauseScreen = mAtlas.findRegion("TO BE DEFINED");
		mFont = new BitmapFont(Gdx.files.internal("TO BE DEFINED"), false);

	}

}
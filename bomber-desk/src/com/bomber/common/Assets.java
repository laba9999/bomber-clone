package com.bomber.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bomber.gameobjects.MovableObjectAnimation;
import com.bomber.gameobjects.bonus.Bonus;

/**
 * @author Filipe
 * 
 */
public class Assets {
	private static final float PLAYER_WALK_FRAME_DURATION = 15f;
	private static final float PLAYER_DIE_FRAME_DURATION = 50f;
	private static final float PLAYER_SHIELD_FRAME_DURATION = 20f;

	private static final float BONUS_FRAME_DURATION = 10f;
	private static final float BOMB_EXPLOSIONS_FRAME_DURATION = 10f;
	private static final float BOMB_FRAME_DURATION = 50f;
	private static final float TILE_EXPLOSION_FRAME_DURATION = 5;
	private static final float N_MONSTER_WALK_FRAME_DURATION = 30f;
	private static final float N_MONSTER_DIE_WALK_FRAME_DURATION = 30f;
	private static final float G_MONSTER_WALK_FRAME_DURATION = 30f;
	private static final float G_MONSTER_DIE_FRAME_DURATION = 30f;

	private static final String ATLAS_FILE = "atlas.txt";

	public static TextureAtlas mAtlas;
	public static HashMap<String, MovableObjectAnimation> mMonsters;
	public static HashMap<String, MovableObjectAnimation> mPlayers;
	public static HashMap<String, TextureRegion> mPlayersHeads;
	public static HashMap<String, Animation> mBonusAnimations;
	public static HashMap<String, TextureRegion> mBonusIcons;
	public static HashMap<String, Animation> mPlayerEffects;
	public static HashMap<String, TextureRegion> mNonDestroyableTiles;

	/**
	 * Posição 0 da animação é o tile antes de ser destruido. Daí para a frente
	 * é a destruição do tile.
	 */
	public static HashMap<String, Animation> mDestroyableTiles;
	public static HashMap<String, Animation> mExplosions;

	public static MovableObjectAnimation mBomb;
	public static TextureRegion mMainScreen;
	public static Animation mSoundButton;
	public static HashMap<String, TextureRegion> mPauseButtons;
	public static TextureRegion mControllerBar;
	/**
	 * Usados no pause.
	 */
	public static TextureRegion mPauseScreen;
	public static TextureRegion mLevelCompletedScreen;
	public static BitmapFont mFont;

	public static void loadAssets()
	{
		mPlayers = new HashMap<String, MovableObjectAnimation>();
		mPlayerEffects = new HashMap<String, Animation>();
		mPlayersHeads = new HashMap<String, TextureRegion>();
		mBonusAnimations = new HashMap<String, Animation>();
		mBonusIcons = new HashMap<String, TextureRegion>();
		mExplosions = new HashMap<String, Animation>();
		mMonsters = new HashMap<String, MovableObjectAnimation>();
		mNonDestroyableTiles = new HashMap<String, TextureRegion>();
		mDestroyableTiles = new HashMap<String, Animation>();

		loadAtlas();
		loadPlayerAnimations();
		loadPlayersHeads();
		loadPlayerEffects();
		loadExplosions();
		loadBonus();
		loadBomb();
		loadUI();
	}

	private static void loadAtlas()
	{
		mAtlas = new TextureAtlas(Gdx.files.internal(ATLAS_FILE));
	}

	private static void loadPlayerAnimations()
	{

		MovableObjectAnimation temp = loadPlayerMovableObjectAnimation("b_white");
		mPlayers.put("b_white", temp);

		temp = loadPlayerMovableObjectAnimation("b_red");
		mPlayers.put("b_red", temp);

		temp = loadPlayerMovableObjectAnimation("b_blue");
		mPlayers.put("b_blue", temp);

		temp = loadPlayerMovableObjectAnimation("b_green");
		mPlayers.put("b_green", temp);
	}

	private static void loadPlayerEffects()
	{
		Animation shield = loadAnimation("shield_", PLAYER_SHIELD_FRAME_DURATION);
		mPlayerEffects.put("shield", shield);

		// TODO : ler water splash
	}

	/**
	 * Cria MovableObjectAnimation com Animations de Players
	 */
	private static MovableObjectAnimation loadPlayerMovableObjectAnimation(String _id)
	{
		short dieFrames = 8;
		short walkFrames = 4;

		MovableObjectAnimation movableAnimation = new MovableObjectAnimation();

		// load die animation
		movableAnimation.die = loadAnimation(_id + "_die_", PLAYER_DIE_FRAME_DURATION);

		// load walkup animation
		movableAnimation.walk[Directions.UP] = loadAnimation(_id + "_walk_up_", PLAYER_WALK_FRAME_DURATION);

		// load walkdown animation
		movableAnimation.walk[Directions.DOWN] = loadAnimation(_id + "_walk_down_", PLAYER_WALK_FRAME_DURATION);

		// load walkleft animation
		movableAnimation.walk[Directions.LEFT] = loadAnimation(_id + "_walk_left_", PLAYER_WALK_FRAME_DURATION);

		// load walkright animation
		movableAnimation.walk[Directions.RIGHT] = loadAnimation(_id + "_walk_right_", PLAYER_WALK_FRAME_DURATION);

		movableAnimation.numberOfFramesDying = dieFrames;
		movableAnimation.numberOfFramesPerWalk = walkFrames;

		return movableAnimation;
	}

	/**
	 * Carrega animação identificada por _id Ex : loadAnimation("b_white_die_");
	 */
	private static Animation loadAnimation(String _id, float _frameDuration)
	{
		List<AtlasRegion> regions = mAtlas.findRegions(_id);

		return new Animation(_frameDuration, regions);

	}

	public static void loadNormalMonster(String _id)
	{
		MovableObjectAnimation temp = loadNormalMonsterMovableObjectAnimation(_id);
		mMonsters.put(_id, temp);
	}

	/**
	 * Cria MovableObjectAnimation com Animations de Monsters
	 */
	private static MovableObjectAnimation loadNormalMonsterMovableObjectAnimation(String _id)
	{
		short dieFrames = 6;
		short walkFrames = 3;

		MovableObjectAnimation movableAnimation = new MovableObjectAnimation();

		// load die animation
		movableAnimation.die = loadAnimation(_id + "_die_", N_MONSTER_DIE_WALK_FRAME_DURATION);

		// load walkup animation
		movableAnimation.walk[Directions.UP] = loadBackloopingAnimation(_id + "_walk_up_", walkFrames, N_MONSTER_WALK_FRAME_DURATION);

		// load walkdown animation
		movableAnimation.walk[Directions.DOWN] = loadBackloopingAnimation(_id + "_walk_down_", walkFrames, N_MONSTER_WALK_FRAME_DURATION);

		// load walkleft animation
		movableAnimation.walk[Directions.LEFT] = loadBackloopingAnimation(_id + "_walk_left_", walkFrames, N_MONSTER_WALK_FRAME_DURATION);

		// load walkright animation
		movableAnimation.walk[Directions.RIGHT] = loadBackloopingAnimation(_id + "_walk_right_", walkFrames, N_MONSTER_WALK_FRAME_DURATION);

		movableAnimation.numberOfFramesDying = dieFrames;
		movableAnimation.numberOfFramesPerWalk = walkFrames;

		return movableAnimation;
	}

	public static void loadGenericMonster(String _id)
	{
		MovableObjectAnimation temp = loadGenericMonsterMovableObjectAnimations(_id);
		mMonsters.put(_id, temp);
	}

	/**
	 * Cria MovableObjectAnimation de monstros genéricos
	 * 
	 */
	private static MovableObjectAnimation loadGenericMonsterMovableObjectAnimations(String _id)
	{
		short dieFrames = 6;
		short walkFrames = 3;

		MovableObjectAnimation movableAnimation = new MovableObjectAnimation();
		movableAnimation.die = loadAnimation(_id + "_die_", G_MONSTER_DIE_FRAME_DURATION);
		movableAnimation.walk[Directions.UP] = loadBackloopingAnimation(_id + "_walk_", walkFrames, G_MONSTER_WALK_FRAME_DURATION);
		movableAnimation.walk[Directions.DOWN] = movableAnimation.walk[Directions.UP];
		movableAnimation.walk[Directions.LEFT] = movableAnimation.walk[Directions.UP];
		movableAnimation.walk[Directions.RIGHT] = movableAnimation.walk[Directions.UP];

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
		mBonusAnimations.put("bonus_bomb", loadBackloopingAnimation("bonus_bomb_", Bonus.NUMBER_OF_ANIMATION_FRAMES, BONUS_FRAME_DURATION));
		mBonusAnimations.put("bonus_hand", loadBackloopingAnimation("bonus_hand_", Bonus.NUMBER_OF_ANIMATION_FRAMES, BONUS_FRAME_DURATION));
		mBonusAnimations.put("bonus_life", loadBackloopingAnimation("bonus_life_", Bonus.NUMBER_OF_ANIMATION_FRAMES, BONUS_FRAME_DURATION));
		mBonusAnimations.put("bonus_potion", loadBackloopingAnimation("bonus_potion_", Bonus.NUMBER_OF_ANIMATION_FRAMES, BONUS_FRAME_DURATION));
		mBonusAnimations.put("bonus_shield", loadBackloopingAnimation("bonus_shield_", Bonus.NUMBER_OF_ANIMATION_FRAMES, BONUS_FRAME_DURATION));
		mBonusAnimations.put("bonus_speed", loadBackloopingAnimation("bonus_speed_", Bonus.NUMBER_OF_ANIMATION_FRAMES, BONUS_FRAME_DURATION));
		mBonusAnimations.put("bonus_star", loadBackloopingAnimation("bonus_star_", Bonus.NUMBER_OF_ANIMATION_FRAMES, BONUS_FRAME_DURATION));

		mBonusIcons.put("shield", mAtlas.findRegion("bonus_shield"));
		mBonusIcons.put("hand", mAtlas.findRegion("bonus_throw"));
		// TODO: ajustar o tamanho
		mBonusIcons.put("star", mAtlas.findRegion("bonus_invencibility"));

	}

	private static Animation loadBackloopingAnimation(String _id, short _howManyFrames, float _frameDuration)
	{
		ArrayList<TextureRegion> regions = new ArrayList<TextureRegion>();

		for (int i = 0; i < _howManyFrames; i++)
			regions.add(mAtlas.findRegion(_id, i));

		for (int i = _howManyFrames - 2; i > 0; i--)
			regions.add(mAtlas.findRegion(_id, i));

		return new Animation(_frameDuration, regions);
	}

	public static void loadDestroyableTile(String _id)
	{
		short destroyingFrames = 7;
		ArrayList<TextureRegion> regions = new ArrayList<TextureRegion>();

		// parte "tiles_2" em "tiles" e "2"
		String[] splittedId = _id.split("_");
		short tileNumber = Short.parseShort(splittedId[1]);

		TextureRegion r = mAtlas.findRegion("tiles_", tileNumber);
		regions.add(r);

		String destroyName = _id + "_destroy_";
		for (int i = 0; i < destroyingFrames; i++)
		{
			r = mAtlas.findRegion(destroyName, i);
			regions.add(r);
		}

		mDestroyableTiles.put(_id, new Animation(TILE_EXPLOSION_FRAME_DURATION, regions));
	}

	public static void loadNonDestroyableTile(String _id)
	{
		String[] splitted = _id.split("_");
		int tileIndex = Integer.parseInt(splitted[1]);
		TextureRegion r = mAtlas.findRegion("tiles_", tileIndex);
		mNonDestroyableTiles.put(_id, r);
	}

	private static void loadExplosions()
	{
		mExplosions.put("xplode_center", loadAnimation("xplode_center_", BOMB_EXPLOSIONS_FRAME_DURATION));
		mExplosions.put("xplode_mid_hor", loadAnimation("xplode_hor_", BOMB_EXPLOSIONS_FRAME_DURATION));
		mExplosions.put("xplode_mid_ver", loadAnimation("xplode_vert_", BOMB_EXPLOSIONS_FRAME_DURATION));
		mExplosions.put("xplode_tip_down", loadAnimation("xplode_tip_down_", BOMB_EXPLOSIONS_FRAME_DURATION));
		mExplosions.put("xplode_tip_left", loadAnimation("xplode_tip_left_", BOMB_EXPLOSIONS_FRAME_DURATION));
		mExplosions.put("xplode_tip_right", loadAnimation("xplode_tip_right_", BOMB_EXPLOSIONS_FRAME_DURATION));
		mExplosions.put("xplode_tip_up", loadAnimation("xplode_tip_up_", BOMB_EXPLOSIONS_FRAME_DURATION));
	}

	private static void loadBomb()
	{

		MovableObjectAnimation movableAnimation = new MovableObjectAnimation();
		// a animação die é mesmo necessária porque bombas matam bombas
		movableAnimation.die = loadAnimation("bomb_orig_", BOMB_FRAME_DURATION);
		movableAnimation.walk[Directions.UP] = loadAnimation("bomb_orig_", BOMB_FRAME_DURATION);
		movableAnimation.walk[Directions.DOWN] = loadAnimation("bomb_orig_", BOMB_FRAME_DURATION);
		movableAnimation.walk[Directions.LEFT] = loadAnimation("bomb_orig_", BOMB_FRAME_DURATION);
		movableAnimation.walk[Directions.RIGHT] = loadAnimation("bomb_orig_", BOMB_FRAME_DURATION);
		movableAnimation.numberOfFramesPerWalk = 4;
		mBomb = movableAnimation;
	}

	private static void loadUI()
	{
		// TODO : definir o IDs para estes componentes
		// mMainScreen = mAtlas.findRegion("TO BE DEFINED");
		// mSoundButton = loadAnimation("TO BE DEFINED");
		// mPauseButtons.put("TO BE DEFINED",
		// mAtlas.findRegion("TO BE DEFINED"));
		Texture t = new Texture(Gdx.files.internal("controller.png"));
		t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		mControllerBar = new TextureRegion(t);

		//mPauseScreen = mAtlas.findRegion("pause_screen");
		t = new Texture(Gdx.files.internal("pause_screen.png"));
		t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		mPauseScreen = new TextureRegion(t);
		
		t = new Texture(Gdx.files.internal("level_completed.png"));
		t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		mLevelCompletedScreen = new TextureRegion(t);
		
		mFont = new BitmapFont(Gdx.files.internal("white_22.fnt"), false);
		mFont.setColor(Color.WHITE);
		mFont.setScale(1);

	}

	public static class DarkGlass {
		private static Pixmap mPixmap = null;
		private static Texture mDarkGlass = null;

		public static void dispose()
		{
			if( mPixmap == null)
				return;
			
			mPixmap.dispose();
			mDarkGlass.dispose();

			mPixmap = null;
			mDarkGlass = null;
		}

		public static Texture get()
		{
			if (mDarkGlass != null)
				return mDarkGlass;

			create();
			
			return mDarkGlass;
		}

		private static void create()
		{
			mPixmap = new Pixmap(1024, 512, Pixmap.Format.RGBA4444);

			// Cria o vidro escuro
			mPixmap.setColor(0, 0, 0, 0.8f);
			mPixmap.fill();

			mDarkGlass = new Texture(mPixmap);
			mDarkGlass.draw(mPixmap, 0, 0);
			mDarkGlass.bind();
		}
	}
}
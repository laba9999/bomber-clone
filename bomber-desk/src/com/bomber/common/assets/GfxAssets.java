package com.bomber.common.assets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bomber.common.Directions;
import com.bomber.gameobjects.MovableObjectAnimation;
import com.bomber.gameobjects.bonus.Bonus;

/**
 * @author Filipe
 * 
 */
public class GfxAssets {
	private static final float PLAYER_WALK_FRAME_DURATION = 15f;
	private static final float PLAYER_DIE_FRAME_DURATION = 15f;
	private static final float PLAYER_SHIELD_FRAME_DURATION = 20f;
	private static final short PLAYER_DIE_FRAMES_COUNT = 8;
	private static final short PLAYER_WALK_FRAMES_COUNT = 4;

	public static final short PORTAL_FRAMES_COUNT = 2;
	public static final short PORTAL_FRAME_DURATION = 50;

	private static final float BONUS_FRAME_DURATION = 10f;
	private static final float BOMB_EXPLOSIONS_FRAME_DURATION = 10f;
	private static final float BOMB_FRAME_DURATION = 50f;

	private static final float TILE_EXPLOSION_FRAME_DURATION = 5;
	private static final short TILE_EXPLOSION_FRAME_COUNT = 7;

	private static final float N_MONSTER_WALK_FRAME_DURATION = 75f;
	private static final float N_MONSTER_DIE_WALK_FRAME_DURATION = 30f;
	private static final short N_MONSTER_DIE_FRAME_COUNT = 6;
	private static final short N_MONSTER_WALK_FRAME_COUNT = 3;

	private static final float G_MONSTER_WALK_FRAME_DURATION = 30f;
	private static final float G_MONSTER_DIE_FRAME_DURATION = 30f;
	private static final short G_MONSTER_DIE_FRAME_COUNT = 6;
	private static final short G_MONSTER_WALK_FRAME_COUNT = 3;
	
	private static final float WAITING_FRAME_DURATION = 8f;
	
	private static final String ATLAS_FILE = "atlas.txt";
	private static final String ATLAS_HD_FILE = "atlas_hd.txt";

	public static TextureAtlas mAtlas;
	public static HashMap<String, MovableObjectAnimation> mMonsters;
	public static HashMap<String, MovableObjectAnimation> mPlayers;
	public static HashMap<String, TextureRegion> mPlayersHeads;
	public static HashMap<String, TextureRegion> mPlayersSad;
	public static HashMap<String, TextureRegion> mPlayersHappy;
	public static HashMap<String, Animation> mBonusAnimations;
	public static HashMap<String, TextureRegion> mBonusIcons;
	public static HashMap<String, Animation> mPlayerEffects;
	public static HashMap<String, TextureRegion> mNonDestroyableTiles;

	public static TextureRegion mControlPad;
	public static TextureRegion mButtonPause;
	public static TextureRegion mButtonBomb;
	public static TextureRegion mClockBar;
	public static TextureRegion mBonusBar;

	/**
	 * Posição 0 da animação é o tile antes de ser destruido. Daí para a frente
	 * é a destruição do tile.
	 */
	public static HashMap<String, Animation> mDestroyableTiles;
	public static HashMap<String, Animation> mExplosions;

	public static MovableObjectAnimation mBomb;

	public static HashMap<String, TextureRegion> mScreens = new HashMap<String, TextureRegion>();

	public static Animation mSoundButton;
	public static Animation mPortal;

	public static TextureRegion[] mTrophy = new TextureRegion[3];

	public static HashMap<String, TextureRegion> mFlags;

	public static HashMap<String, TextureRegion> mPauseButtons;

	public static BitmapFont mGenericFont;
	public static BitmapFont mNamesFont;
	public static BitmapFont mBigFont;

	public static Animation mWaitingAnimation;
	

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
		mPlayersSad = new HashMap<String, TextureRegion>();
		mPlayersHappy = new HashMap<String, TextureRegion>();

		mFlags = new HashMap<String, TextureRegion>();

		loadAtlas();
		loadPortal();
		loadPlayerAnimations();
		loadPlayersHeads();
		loadPlayersFaceExpressions();
		loadPlayerEffects();
		loadExplosions();
		loadBonus();
		loadBomb();
		loadFlags();
		loadUI();
	}

	private static void loadAtlas()
	{
		mAtlas = new TextureAtlas(Gdx.files.internal(ATLAS_FILE));
	}

	private static void loadFlags()
	{
		// Com o varão
		mFlags.put("flag_pole_team1", mAtlas.findRegion("flag_pole_team", 1));
		mFlags.put("flag_pole_team2", mAtlas.findRegion("flag_pole_team", 2));

		// Transportáveis
		mFlags.put("flag_transport_team1", mAtlas.findRegion("flag_transport_team", 1));
		mFlags.put("flag_transport_team2", mAtlas.findRegion("flag_transport_team", 2));
		mFlags.put("flag_transport_left_team1", mAtlas.findRegion("flag_transport_left_team", 1));
		mFlags.put("flag_transport_left_team2", mAtlas.findRegion("flag_transport_left_team", 2));

	}

	private static void loadPortal()
	{
		mPortal = loadAnimation("portal_", PORTAL_FRAME_DURATION);
	}

	private static void loadPlayersFaceExpressions()
	{
		String[] players = { "b_white", "b_blue", "b_green", "b_red" };

		// Tristes
		List<AtlasRegion> regions = mAtlas.findRegions("b_sad_");
		for (int i = 0; i < regions.size(); i++)
			mPlayersSad.put(players[i], regions.get(i));

		// Contentes
		regions = mAtlas.findRegions("b_happy_");
		for (int i = 0; i < regions.size(); i++)
			mPlayersHappy.put(players[i], regions.get(i));
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

		movableAnimation.numberOfFramesDying = PLAYER_DIE_FRAMES_COUNT;
		movableAnimation.numberOfFramesPerWalk = PLAYER_WALK_FRAMES_COUNT;

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
		MovableObjectAnimation movableAnimation = new MovableObjectAnimation();

		// load die animation
		movableAnimation.die = loadAnimation(_id + "_die_", N_MONSTER_DIE_WALK_FRAME_DURATION);

		// load walkup animation
		movableAnimation.walk[Directions.UP] = loadBackloopingAnimation(_id + "_walk_up_", N_MONSTER_WALK_FRAME_COUNT, N_MONSTER_WALK_FRAME_DURATION);

		// load walkdown animation
		movableAnimation.walk[Directions.DOWN] = loadBackloopingAnimation(_id + "_walk_down_", N_MONSTER_WALK_FRAME_COUNT, N_MONSTER_WALK_FRAME_DURATION);

		// load walkleft animation
		movableAnimation.walk[Directions.LEFT] = loadBackloopingAnimation(_id + "_walk_left_", N_MONSTER_WALK_FRAME_COUNT, N_MONSTER_WALK_FRAME_DURATION);

		// load walkright animation
		movableAnimation.walk[Directions.RIGHT] = loadBackloopingAnimation(_id + "_walk_right_", N_MONSTER_WALK_FRAME_COUNT, N_MONSTER_WALK_FRAME_DURATION);

		movableAnimation.numberOfFramesDying = N_MONSTER_DIE_FRAME_COUNT;
		movableAnimation.numberOfFramesPerWalk = N_MONSTER_WALK_FRAME_COUNT;

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

		MovableObjectAnimation movableAnimation = new MovableObjectAnimation();
		movableAnimation.die = loadAnimation(_id + "_die_", G_MONSTER_DIE_FRAME_DURATION);
		movableAnimation.walk[Directions.UP] = loadBackloopingAnimation(_id + "_walk_", G_MONSTER_WALK_FRAME_COUNT, G_MONSTER_WALK_FRAME_DURATION);
		movableAnimation.walk[Directions.DOWN] = movableAnimation.walk[Directions.UP];
		movableAnimation.walk[Directions.LEFT] = movableAnimation.walk[Directions.UP];
		movableAnimation.walk[Directions.RIGHT] = movableAnimation.walk[Directions.UP];

		movableAnimation.numberOfFramesDying = G_MONSTER_DIE_FRAME_COUNT;
		movableAnimation.numberOfFramesPerWalk = G_MONSTER_WALK_FRAME_COUNT;

		return movableAnimation;
	}

	private static void loadPlayersHeads()
	{
		TextureRegion r = mAtlas.findRegion("face_white");
		mPlayersHeads.put("b_white", r);

		r = mAtlas.findRegion("face_green");
		mPlayersHeads.put("b_green", r);

		r = mAtlas.findRegion("face_red");
		mPlayersHeads.put("b_red", r);

		r = mAtlas.findRegion("face_blue");
		mPlayersHeads.put("b_blue", r);

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

		ArrayList<TextureRegion> regions = new ArrayList<TextureRegion>();

		// parte "tiles_2" em "tiles" e "2"
		String[] splittedId = _id.split("_");
		short tileNumber = Short.parseShort(splittedId[1]);

		TextureRegion r = mAtlas.findRegion("tiles_", tileNumber);
		regions.add(r);

		String destroyName = _id + "_destroy_";
		for (int i = 0; i < TILE_EXPLOSION_FRAME_COUNT; i++)
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
		TextureAtlas atlasHD = new TextureAtlas(Gdx.files.internal(ATLAS_HD_FILE));

		mControlPad = atlasHD.findRegion("d-pad");
		mButtonPause = atlasHD.findRegion("btn_pause");
		mButtonBomb = atlasHD.findRegion("btn_bomb");
		mClockBar = atlasHD.findRegion("clock_bar");
		mBonusBar = atlasHD.findRegion("bonus_bar");
		

		mScreens.put("pause", atlasHD.findRegion("pause_screen"));
		mScreens.put("levelcompleted", atlasHD.findRegion("level_completed"));
		mScreens.put("gameover", atlasHD.findRegion("gameover"));

		mScreens.put("background_gradient_red", atlasHD.findRegion("background_gradient_red"));
		mScreens.put("background_gradient_green", atlasHD.findRegion("background_gradient_green"));
		mScreens.put("background_gradient_grey", atlasHD.findRegion("background_gradient_grey"));
		
		mSoundButton = new Animation(1, atlasHD.findRegions("sound_"));

		mTrophy[0] = atlasHD.findRegion("trophy");
		mTrophy[1] = mAtlas.findRegion("trophyBig");
		mTrophy[2] = mAtlas.findRegion("trophySmall");

		mGenericFont = new BitmapFont(Gdx.files.internal("teste_22.fnt"), false);
		mNamesFont = new BitmapFont(Gdx.files.internal("name_font.fnt"), false);
		mBigFont = new BitmapFont(Gdx.files.internal("font_28.fnt"), false);

		
		mWaitingAnimation = new Animation(WAITING_FRAME_DURATION, atlasHD.findRegions("waiting_animation_"));

	}

	public static TextureRegion getSoundButtonTexture()
	{
		if (SoundAssets.mIsSoundActive)
			return mSoundButton.getKeyFrame(1, false);
		else
			return mSoundButton.getKeyFrame(0, false);

	}

	public static void reset()
	{
		mMonsters.clear();
		mNonDestroyableTiles.clear();
		mDestroyableTiles.clear();
	}

	public static class Pixmaps {
		private static Pixmap mPixmapDarkGlass = null;
		private static Pixmap mPixmapNamePlate = null;
		private static Texture mDarkGlass = null;
		private static Texture mNamePlate = null;

		public static void dispose()
		{
			if (mPixmapDarkGlass == null)
				return;

			mPixmapDarkGlass.dispose();
			mPixmapNamePlate.dispose();
			mDarkGlass.dispose();
			mNamePlate.dispose();

			mPixmapDarkGlass = null;
			mPixmapNamePlate = null;
			mDarkGlass = null;
			mNamePlate = null;
		}

		public static Texture getDarkGlass()
		{
			if (mDarkGlass != null)
				return mDarkGlass;

			create();

			return mDarkGlass;
		}

		public static Texture getNamePlate()
		{
			if (mNamePlate != null)
				return mNamePlate;

			create();

			return mNamePlate;
		}
		
		private static void create()
		{
			mPixmapDarkGlass = new Pixmap(1024, 512, Pixmap.Format.RGBA4444);

			// Cria o vidro escuro
			mPixmapDarkGlass.setColor(0, 0, 0, 0.8f);
			mPixmapDarkGlass.fill();

			mDarkGlass = new Texture(mPixmapDarkGlass);
			mDarkGlass.draw(mPixmapDarkGlass, 0, 0);
			mDarkGlass.bind();
			
			
			// Cria os nameplates
			mPixmapNamePlate= new Pixmap(256, 16, Pixmap.Format.RGBA4444);
			mPixmapNamePlate.setColor(0, 0, 0, 0.8f);
			mPixmapNamePlate.fill();

			mNamePlate = new Texture(mPixmapNamePlate);
			mNamePlate.draw(mPixmapNamePlate, 0, 0);
			mNamePlate.bind();
		}
	}
}
package com.badlogic.gdx.helloworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;



public class MainMenuScreen extends Screen {
	SpriteBatch spriteBatch;
	TextureRegion novoBackgroundRegion;
	TextureRegion backgroundRegion;
	TextureAtlas atlas;
	TextureRegion boneco;
	Sprite sprite;
	BitmapFont font;
	Vector2 textPosition = new Vector2(100, 100);
	Vector2 textDirection = new Vector2(1, 1);
	OrthographicCamera guiCam;
	
	float bonecoWidth;
	float bonecoHeight;
	float positionX;
	float positionY;
	
	
	Animation bonecoAnim;
	float stateTime;
	Integer fps;
	
	public MainMenuScreen(Game game) {
		super(game);
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		
		float ratio =  width/height;
		//guiCam = new OrthographicCamera(2*ratio,2f);
		guiCam = new OrthographicCamera(800, 480);		
		guiCam.position.set(800/2,480/2, 0);
		
		font = new BitmapFont();
		font.setColor(Color.RED);
		atlas = new TextureAtlas(Gdx.files.internal("data/back.txt"));
		novoBackgroundRegion = new TextureAtlas(Gdx.files.internal("data/newlayout.txt")).findRegion("mockup_new");
		backgroundRegion = atlas.findRegion("mockup");
		bonecoAnim = new Animation(0.01f,atlas.findRegion("b_white_walk_right_", 1),atlas.findRegion("b_white_walk_right_", 0),atlas.findRegion("b_white_walk_right_", 2),atlas.findRegion("b_white_walk_right_", 0));
		boneco = atlas.findRegion("b_white_die_", 0);
		spriteBatch = new SpriteBatch();
		
		bonecoWidth =(float)boneco.getRegionWidth();
		bonecoHeight = (float)boneco.getRegionHeight();
		
		positionX = (5*34+1) + 34/2 ;
		positionY = (480-(2*34));
		stateTime=0;

	}


	@Override
	public void update(float deltaTime) {
		textPosition.x += textDirection.x * deltaTime * 60;
		textPosition.y += textDirection.y * deltaTime * 60;

		if (textPosition.x < 0) {
			textDirection.x = -textDirection.x;
			textPosition.x = 0;
		}
		if (textPosition.x > Gdx.graphics.getWidth()) {
			textDirection.x = -textDirection.x;
			textPosition.x = Gdx.graphics.getWidth();
		}
		if (textPosition.y < 0) {
			textDirection.y = -textDirection.y;
			textPosition.y = 0;
		}
		if (textPosition.y > Gdx.graphics.getHeight()) {
			textDirection.y = -textDirection.y;
			textPosition.y = Gdx.graphics.getHeight();
		}
		positionX += 100*deltaTime;
	}

	@Override
	public void present(float deltaTime) {
		GLCommon gl = Gdx.gl;
		gl.glClearColor(1, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		spriteBatch.setProjectionMatrix(guiCam.combined);

		spriteBatch.disableBlending();
		spriteBatch.begin();
		//spriteBatch.draw(backgroundRegion, 0, 0);	
		spriteBatch.draw(novoBackgroundRegion,0,0);
		spriteBatch.enableBlending();
		TextureRegion keyFrame = bonecoAnim.getKeyFrame(stateTime, Animation.ANIMATION_LOOPING);		
		spriteBatch.draw(keyFrame, positionX - bonecoWidth/2 ,positionY,bonecoWidth,bonecoHeight);
		
		fps = Gdx.graphics.getFramesPerSecond();
		font.draw(spriteBatch, fps.toString(), (int)textPosition.x, (int)textPosition.y);
		spriteBatch.end();
		stateTime += deltaTime;
	
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
}

package com.amov.bomber;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpActivity extends GameActivity
{
	private static final int[] IMAGES_RESOURCES = new int[] { R.drawable.help_move, R.drawable.help_bomb, R.drawable.help_bonus,R.drawable.help_bonus_actions, R.drawable.help_campaign, R.drawable.help_ctf, R.drawable.help_dm, R.drawable.help_build };

	private static final int[] SUBTITLES_RESOURCES = new int[] { R.string.help_subtitle_moving, R.string.help_subtitle_bombs, R.string.help_subtitle_bonus, R.string.help_subtitle_bonus_actions, R.string.help_subtitle_campaign,
			R.string.help_subtitle_ctf, R.string.help_subtitle_dm, R.string.help_subtitle_build };

	private static final int[] DESCRIPTIONS_RESOURCES = new int[] { R.string.help_description_moving, R.string.help_description_bombs, R.string.help_description_bonus,
			R.string.help_description_bonus_actions, R.string.help_description_campaign, R.string.help_description_ctf, R.string.help_description_dm, R.string.help_description_build };
	private static final int MAX_POSITION = 7;

	private int mPosition = 0;

	private ImageView mImage;
	private TextView mSubtitle;
	private TextView mDescription;
	private Button mButtonLeft;
	private Button mButtonRight;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if(mGoneBackToAssetsLoader)
		{
			finish();
			return;
		}
		setContentView(R.layout.help);

		mImage = (ImageView) findViewById(R.id.imageHelp);

		mSubtitle = (TextView) findViewById(R.id.textHelpSubtitle);
		mDescription = (TextView) findViewById(R.id.textHelpDescription);
		mButtonLeft = (Button) findViewById(R.id.buttonHelpLeft);
		mButtonRight = (Button) findViewById(R.id.ButtonHelpRight);
		
		mButtonLeft.setEnabled(false);
		mButtonLeft.setVisibility(Button.INVISIBLE);
	}

	public void onLeftButton(View v)
	{
		if (mPosition > 0)
		{
			mPosition--;
			Bitmap b = BitmapFactory.decodeResource(getResources(), IMAGES_RESOURCES[mPosition]);
			mImage.setImageBitmap(b);

			mSubtitle.setText(getResources().getString(SUBTITLES_RESOURCES[mPosition]));
			mDescription.setText(getResources().getString(DESCRIPTIONS_RESOURCES[mPosition]));
		}

		if (mPosition == 0)
		{
			mButtonLeft.setEnabled(false);
			mButtonLeft.setVisibility(Button.INVISIBLE);
		}

		mButtonRight.setEnabled(true);
		mButtonRight.setVisibility(Button.VISIBLE);
	}

	public void onRightButton(View v)
	{
		if (mPosition < MAX_POSITION)
		{
			mPosition++;
			Bitmap b = BitmapFactory.decodeResource(getResources(), IMAGES_RESOURCES[mPosition]);
			mImage.setImageBitmap(b);
			mSubtitle.setText(getResources().getString(SUBTITLES_RESOURCES[mPosition]));
			mDescription.setText(getResources().getString(DESCRIPTIONS_RESOURCES[mPosition]));
		}
		
		if (mPosition == MAX_POSITION)
		{
			mButtonRight.setEnabled(false);
			mButtonRight.setVisibility(Button.INVISIBLE);
		}

		mButtonLeft.setEnabled(true);
		mButtonLeft.setVisibility(Button.VISIBLE);
	}
}

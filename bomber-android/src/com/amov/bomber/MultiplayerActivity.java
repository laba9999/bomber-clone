package com.amov.bomber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;

public class MultiplayerActivity extends Activity
{
	
	public static final int DIALOG_MULTIPLAYER = 0;
	RadioButton mRadioWifi;
	RadioButton mRadioClient;
	TableRow mTableRowIpPort;
	EditText mEditIp;
	TextView mTextIp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiplayer);
		
		mTableRowIpPort = (TableRow) findViewById(R.id.tableRowIpPort);
		mEditIp = (EditText) findViewById(R.id.editIP);
		mTextIp = (TextView) findViewById(R.id.textIp);
		mRadioWifi = (RadioButton) findViewById(R.id.radioWifi);
		mRadioClient = (RadioButton) findViewById(R.id.radioClient);
	
		//Listeners dos radio buttons
		mRadioWifi.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{			
			public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked)
			{
				if(_isChecked && mRadioClient.isChecked())
				{
					mTableRowIpPort.setVisibility(RadioButton.VISIBLE);
					mEditIp.setVisibility(EditText.VISIBLE);
					mTextIp.setVisibility(TextView.VISIBLE);
				}
				else if(_isChecked && !mRadioClient.isChecked())
				{
					mTableRowIpPort.setVisibility(RadioButton.VISIBLE);
					mEditIp.setVisibility(EditText.GONE);
					mTextIp.setVisibility(TextView.GONE);
				}
				else
				{
					mTableRowIpPort.setVisibility(RadioButton.GONE);
				}
			}
		});
		
		mRadioClient.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{			
			public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked)
			{
				if(_isChecked && mRadioWifi.isChecked())
				{
					mTableRowIpPort.setVisibility(RadioButton.VISIBLE);
					mEditIp.setVisibility(EditText.VISIBLE);
					mTextIp.setVisibility(TextView.VISIBLE);
				}
				else if(_isChecked && !mRadioWifi.isChecked())
				{
					mTableRowIpPort.setVisibility(RadioButton.GONE);
				}
				else if(!_isChecked && mRadioWifi.isChecked())
				{
					mEditIp.setVisibility(EditText.GONE);
					mTextIp.setVisibility(TextView.GONE);
				}
				else
				{
					mTableRowIpPort.setVisibility(RadioButton.GONE);
				}
			}
		});
		


	}
	
	public void onStartButton(View v)
	{
		Intent myIntent = new Intent(this, AndroidGame.class);
		startActivityForResult(myIntent, 0);
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			Intent resultIntent = new Intent();
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
			// desactiva animação na transição entre activities
			overridePendingTransition(0, 0);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

}

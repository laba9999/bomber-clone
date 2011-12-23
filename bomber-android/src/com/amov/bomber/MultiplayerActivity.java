package com.amov.bomber;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MultiplayerActivity extends Activity
{

	public static final int DIALOG_MULTIPLAYER = 0;
	RadioButton mRadioWifi;
	RadioButton mRadioClient;
	TableRow mTableRowIpPort;
	EditText mEditIp;
	TextView mTextIp;
	EditText mEditPort;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiplayer);

		mTableRowIpPort = (TableRow) findViewById(R.id.tableRowIpPort);
		mEditIp = (EditText) findViewById(R.id.editIP);
		mTextIp = (TextView) findViewById(R.id.textIp);
		mEditPort = (EditText) findViewById(R.id.editPort);
		mRadioWifi = (RadioButton) findViewById(R.id.radioWifi);
		mRadioClient = (RadioButton) findViewById(R.id.radioClient);

		// Listeners dos radio buttons
		mRadioWifi.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked)
			{
				if (_isChecked && mRadioClient.isChecked())
				{
					mTableRowIpPort.setVisibility(RadioButton.VISIBLE);
					mEditIp.setVisibility(EditText.VISIBLE);
					mTextIp.setVisibility(TextView.VISIBLE);
				} else if (_isChecked && !mRadioClient.isChecked())
				{
					mTableRowIpPort.setVisibility(RadioButton.VISIBLE);
					mEditIp.setVisibility(EditText.GONE);
					mTextIp.setVisibility(TextView.GONE);
				} else
				{
					mTableRowIpPort.setVisibility(RadioButton.GONE);
				}
			}
		});

		mRadioClient.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked)
			{
				if (_isChecked && mRadioWifi.isChecked())
				{
					mTableRowIpPort.setVisibility(RadioButton.VISIBLE);
					mEditIp.setVisibility(EditText.VISIBLE);
					mTextIp.setVisibility(TextView.VISIBLE);
				} else if (_isChecked && !mRadioWifi.isChecked())
				{
					mTableRowIpPort.setVisibility(RadioButton.GONE);
				} else if (!_isChecked && mRadioWifi.isChecked())
				{
					mEditIp.setVisibility(EditText.GONE);
					mTextIp.setVisibility(TextView.GONE);
				} else
				{
					mTableRowIpPort.setVisibility(RadioButton.GONE);
				}
			}
		});

	}


	
	public void onStartButton(View v)
	{
		boolean connected = false;
		
		if(mRadioWifi.isChecked())
		{
			connected = checkWifiConnection();
			
			if(!connected)
			{
				Toast t = Toast.makeText(this,this.getString(R.string.error_wificonnection), Toast.LENGTH_SHORT);
				t.show();
			}

		}
		else
		{
			connected = checkBluetoothConnection();
			if(!connected)
			{
				Toast t = Toast.makeText(this, this.getString(R.string.error_bluetoothconnection), Toast.LENGTH_SHORT);
				t.show();
			}
		}
		
		if(connected)
		{
			Intent myIntent = new Intent(this, AndroidGame.class);
			startActivityForResult(myIntent, 0);
		}

	}

	
	private boolean checkWifiConnection()
	{
		//http://stackoverflow.com/questions/1811852/android-if-wifi-is-enabled-and-active-launch-an-intent
		
	    ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (cm != null) {
            networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        }
        return networkInfo == null ? false : networkInfo.isConnected();
	}
	
	private boolean checkBluetoothConnection()
	{//http://stackoverflow.com/questions/7672334/how-to-check-if-bluetooth-is-enabled-programmatically
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null)
		{
		    // dispositivo nao suporta bluetooth
			return false;
		}
		
	    if (!mBluetoothAdapter.isEnabled())
	    {
	        // Bluetooth não está activo
	    	return false;
	    }
	    
	    return true;
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

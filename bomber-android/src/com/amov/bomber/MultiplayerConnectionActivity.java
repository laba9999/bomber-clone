package com.amov.bomber;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Set;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bomber.DebugSettings;
import com.bomber.Game;
import com.bomber.remote.Protocols;

public class MultiplayerConnectionActivity extends GameActivity
{

	public final int REQUEST_ENABLE_BT = 0;
	public final int REQUEST_DISCOVERABLE_BT = 1;

	public static final int DIALOG_MULTIPLAYER = 0;

	RadioButton mRadioWifi;
	RadioButton mRadioBluetooth;
	RadioButton mRadioClient;
	RadioButton mRadioServer;
	RadioButton mRadioTCP;
	RadioButton mRadioUDP;

	Button mButtonStart;

	TableRow mTableRowIpPort;
	TableRow mTableRowProtocols;
	TableRow mTableRowBluetoothDevices;
	EditText mEditIp;
	TextView mTextIp;
	EditText mEditPort;
	Spinner mSpinnerBTDevices;

	ArrayAdapter<String> mBTArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiplayer_connection);

		mBTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);

		mTableRowIpPort = (TableRow) findViewById(R.id.tableRowIpPort);
		mTableRowProtocols = (TableRow) findViewById(R.id.tableRowProtocols);
		mTableRowBluetoothDevices = (TableRow) findViewById(R.id.tableRowBluetoothDevices);
		mSpinnerBTDevices = (Spinner) this.findViewById(R.id.spinnerBluetoothDevices);

		mTableRowProtocols.setVisibility(RadioGroup.GONE);
		mTableRowIpPort.setVisibility(RadioGroup.GONE);
		mTableRowBluetoothDevices.setVisibility(RadioGroup.GONE);

		mEditIp = (EditText) findViewById(R.id.editIP);
		mTextIp = (TextView) findViewById(R.id.textIp);
		mEditPort = (EditText) findViewById(R.id.editPort);

		mRadioWifi = (RadioButton) findViewById(R.id.radioWifi);
		mRadioBluetooth = (RadioButton) findViewById(R.id.radioBluetooth);
		mRadioClient = (RadioButton) findViewById(R.id.radioClient);
		mRadioServer = (RadioButton) findViewById(R.id.radioServer);
		mRadioTCP = (RadioButton) findViewById(R.id.radioTCP);
		mRadioUDP = (RadioButton) findViewById(R.id.radioUDP);

		mButtonStart = (Button) findViewById(R.id.buttonOkDialogMultiplayer);
		mButtonStart.setVisibility(Button.INVISIBLE);

		if (BluetoothAdapter.getDefaultAdapter() == null)
		{
			mTableRowProtocols.setVisibility(RadioGroup.VISIBLE);
			mRadioBluetooth.setVisibility(RadioButton.INVISIBLE);
			mRadioWifi.setChecked(true);
		}

		// Listeners dos radio buttons
		mRadioWifi.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked)
			{
				if (mRadioWifi.isChecked())
					mTableRowBluetoothDevices.setVisibility(RadioGroup.INVISIBLE);

				if (_isChecked && !checkWifiConnection())
				{
					((WifiManager) getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(true);
				}

				mRadioClient.setChecked(false);
				mRadioServer.setChecked(false);

				mTableRowProtocols.setVisibility(_isChecked ? RadioGroup.VISIBLE : RadioGroup.GONE);
				mTableRowIpPort.setVisibility(_isChecked && (mRadioClient.isChecked() || mRadioServer.isChecked()) ? RadioGroup.VISIBLE : RadioGroup.GONE);

				if (_isChecked && mRadioClient.isChecked())
				{
					mTableRowIpPort.setVisibility(RadioButton.VISIBLE);
					mEditIp.setVisibility(EditText.VISIBLE);
					mTextIp.setVisibility(TextView.VISIBLE);
				} else if (_isChecked && mRadioServer.isChecked())
				{
					mTableRowIpPort.setVisibility(RadioButton.VISIBLE);
					mEditIp.setVisibility(EditText.INVISIBLE);
					mTextIp.setVisibility(TextView.INVISIBLE);
				}
			}
		});

		mRadioClient.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked)
			{
				mButtonStart.setVisibility(mRadioServer.isChecked() && mRadioWifi.isChecked() || mRadioClient.isChecked() && mRadioWifi.isChecked() ? RadioGroup.VISIBLE : RadioGroup.INVISIBLE);

				if (_isChecked && mRadioWifi.isChecked())
				{
					mTableRowIpPort.setVisibility(RadioButton.VISIBLE);
					mEditIp.setVisibility(EditText.VISIBLE);
					mTextIp.setVisibility(TextView.VISIBLE);
				} else if (_isChecked && mRadioBluetooth.isChecked())
				{
					if (!checkBluetoothConnection())
					{
						Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
						startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
					} else
						listAvailableBTDevices();
				}
			}
		});

		mRadioServer.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked)
			{
				mButtonStart.setVisibility(mRadioServer.isChecked() && mRadioWifi.isChecked() || mRadioClient.isChecked() && mRadioWifi.isChecked() ? RadioGroup.VISIBLE : RadioGroup.INVISIBLE);
				mTableRowBluetoothDevices.setVisibility(RadioGroup.INVISIBLE);

				if (_isChecked && mRadioWifi.isChecked())
				{
					mTableRowIpPort.setVisibility(RadioButton.VISIBLE);
					mEditIp.setVisibility(EditText.INVISIBLE);
					mTextIp.setVisibility(TextView.INVISIBLE);
				} else if (_isChecked && mRadioBluetooth.isChecked())
				{
					if (!checkBluetoothConnection())
					{
						Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
						discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120);
						startActivityForResult(discoverableIntent, REQUEST_DISCOVERABLE_BT);
					} else
						startGameAsBluetoothServer();
				}
			}
		});

		mSpinnerBTDevices.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				mButtonStart.setVisibility(Button.VISIBLE);

				String[] device = ((String) mSpinnerBTDevices.getItemAtPosition(position)).split("\n");
				DebugSettings.REMOTE_SERVER_ADDRESS = device[1];
				System.out.println("seleccionado bt: " + DebugSettings.REMOTE_SERVER_ADDRESS);
			}

			public void onNothingSelected(AdapterView<?> _arg0)
			{

			}
		});

		// Regista o BroadcastReceiver para dispositivos encontrados
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter);
	}

	@Override
	protected void onDestroy()
	{
		unregisterReceiver(mReceiver);

		super.onDestroy();
	}

	private void listAvailableBTDevices()
	{
		mTableRowBluetoothDevices.setVisibility(RadioGroup.VISIBLE);

		BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

		if (!btAdapter.startDiscovery())
			Game.LOGGER.log("Falhada o inicio de descoberta.");

		// Apresenta toast a indicar que estamos à procura de devices
		Toast.makeText(this, this.getString(R.string.searching_bt_devices), Toast.LENGTH_SHORT).show();

		mBTArrayAdapter.clear();
		mBTArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerBTDevices.setAdapter(mBTArrayAdapter);

		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();

		// Verifica se já existem dispositivos emparelhados
		if (pairedDevices.size() > 0)
			for (BluetoothDevice device : pairedDevices)
				mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());

		mSpinnerBTDevices.performClick();
		mSpinnerBTDevices.refreshDrawableState();
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver()
	{
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action))
			{
				// Obtém o BluetoothDevice do Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
			}
		}
	};

	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data)
	{
		switch (_requestCode)
		{
		case REQUEST_ENABLE_BT:
			if (_resultCode == RESULT_CANCELED)
			{
				mRadioBluetooth.setChecked(false);
				Toast t = Toast.makeText(this, this.getString(R.string.error_bluetoothconnection), Toast.LENGTH_SHORT);
				t.show();
			} else
				listAvailableBTDevices();

			break;

		case REQUEST_DISCOVERABLE_BT:
			if (_resultCode == RESULT_CANCELED)
			{
				mRadioBluetooth.setChecked(false);
				Toast.makeText(this, this.getString(R.string.error_bluetoothconnection), Toast.LENGTH_SHORT).show();

			} else
				startGameAsBluetoothServer();
			break;
		}

		super.onActivityResult(_requestCode, _resultCode, _data);
	}

	private void startGameAsBluetoothServer()
	{
		// Prepara as settings para o jogo
		DebugSettings.BLUETOOTH_ADAPTER = BluetoothAdapter.getDefaultAdapter();
		DebugSettings.START_ANDROID_AS_SERVER = true;
		DebugSettings.REMOTE_PROTOCOL_IN_USE = Protocols.BLUETOOTH;

		launchActivity(BuildActivity.class);
	}

	private void startGameAsBluetoothClient()
	{
		// Prepara as settings para o jogo
		DebugSettings.BLUETOOTH_ADAPTER = BluetoothAdapter.getDefaultAdapter();
		DebugSettings.START_ANDROID_AS_SERVER = false;
		DebugSettings.REMOTE_PROTOCOL_IN_USE = Protocols.BLUETOOTH;

		launchActivity(BuildActivity.class);
	}

	public void onContinueButton(View v)
	{
		boolean connected = false;

		if (mRadioWifi.isChecked())
		{
			connected = checkWifiConnection();

			String localIp = getLocalIpAddress();

			if (!connected && null == localIp)
				Toast.makeText(this, this.getString(R.string.error_wificonnection), Toast.LENGTH_SHORT).show();
			else if(null != localIp)
			{				
				//quando está a correr um hotspot, connected = false mas localIp != nul
						
				AlertDialog alertDialog = new AlertDialog.Builder(this).create();
				alertDialog.setTitle(getResources().getString(R.string.dialog_multiplayer_title));
				alertDialog.setMessage(getResources().getString(R.string.dialog_multiplayer_text));
				
				alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int which) {
					   dialog.dismiss();
				   }
				});
				
				alertDialog.show();
				
				
				DebugSettings.LOCAL_SERVER_ADDRESS = localIp + ":" + mEditPort.getText().toString();
				DebugSettings.START_ANDROID_AS_SERVER = mRadioServer.isChecked();
				DebugSettings.REMOTE_PROTOCOL_IN_USE = mRadioTCP.isChecked() ? Protocols.TCP : Protocols.UDP;

				if (mRadioServer.isChecked())
					DebugSettings.REMOTE_SERVER_ADDRESS = "localhost:" + mEditPort.getText().toString();
				else
					DebugSettings.REMOTE_SERVER_ADDRESS = mEditIp.getText().toString() + ":" + mEditPort.getText().toString();
			
				connected = true;
			}
		} else
		{
			connected = checkBluetoothConnection();
			if (!connected)
				Toast.makeText(this, this.getString(R.string.error_bluetoothconnection), Toast.LENGTH_SHORT).show();
			else
				startGameAsBluetoothClient();
		}

		if (connected)
			launchActivity(mRadioServer.isChecked() ? PVPServerOptionsActivity.class : BuildActivity.class);

	}

	// http://www.droidnova.com/get-the-ip-address-of-your-device,304.html
	public String getLocalIpAddress()
	{
		try
		{
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
			{
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
				{
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress())
					{
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex)
		{
			Log.e("Oops..", ex.toString());
		}
		return null;
	}

	private boolean checkWifiConnection()
	{
		// http://stackoverflow.com/questions/1811852/android-if-wifi-is-enabled-and-active-launch-an-intent

		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = null;
		if (cm != null)
		{
			networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		}
		return networkInfo == null ? false : networkInfo.isConnected();
	}

	private boolean checkBluetoothConnection()
	{// http://stackoverflow.com/questions/7672334/how-to-check-if-bluetooth-is-enabled-programmatically
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
	
	
}

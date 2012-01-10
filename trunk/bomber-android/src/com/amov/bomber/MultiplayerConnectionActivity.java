package com.amov.bomber;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ToggleButton;

import com.bomber.Settings;
import com.bomber.remote.Protocols;

public class MultiplayerConnectionActivity extends GameActivity
{

	public final int REQUEST_ENABLE_BT_CLIENT = 0;
	public final int REQUEST_ENABLE_BT_SERVER = 1;

	public static final int DIALOG_MULTIPLAYER = 0;

	ToggleButton mRadioWifi;
	ToggleButton mRadioBluetooth;
	ToggleButton mRadioClient;
	ToggleButton mRadioServer;
	ToggleButton mRadioTCP;
	ToggleButton mRadioUDP;

	Button mButtonStart;

	TableRow mTableRowIpPort;
	TableRow mTableRowRoles;
	TableRow mTableRowProtocols;
	TableRow mTableRowBluetoothDevices;
	EditText mEditIp;
	TextView mTextIp;
	EditText mEditPort;
	Spinner mSpinnerBTDevices;

	ArrayAdapter<String> mBTArrayAdapter;

	static final int DIALOG_PROGRESS = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multiplayer_connection);

		mBTArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);

		mTableRowRoles = (TableRow) findViewById(R.id.tableRowRoles);
		mTableRowIpPort = (TableRow) findViewById(R.id.tableRowIpPort);
		mTableRowProtocols = (TableRow) findViewById(R.id.tableRowProtocols);
		mTableRowBluetoothDevices = (TableRow) findViewById(R.id.tableRowBluetoothDevices);
		mSpinnerBTDevices = (Spinner) this.findViewById(R.id.spinnerBluetoothDevices);

		mTableRowProtocols.setVisibility(RadioGroup.GONE);
		mTableRowIpPort.setVisibility(RadioGroup.GONE);
		mTableRowBluetoothDevices.setVisibility(RadioGroup.GONE);
		mTableRowRoles.setVisibility(RadioGroup.GONE);

		mEditIp = (EditText) findViewById(R.id.editIP);
		mTextIp = (TextView) findViewById(R.id.textIp);
		mEditPort = (EditText) findViewById(R.id.editPort);

		mRadioWifi = (ToggleButton) findViewById(R.id.radioWifi);
		mRadioBluetooth = (ToggleButton) findViewById(R.id.radioBluetooth);
		mRadioClient = (ToggleButton) findViewById(R.id.radioClient);
		mRadioServer = (ToggleButton) findViewById(R.id.radioServer);
		mRadioTCP = (ToggleButton) findViewById(R.id.radioTCP);
		mRadioUDP = (ToggleButton) findViewById(R.id.radioUDP);

		mButtonStart = (Button) findViewById(R.id.buttonOkDialogMultiplayer);
		mButtonStart.setVisibility(Button.INVISIBLE);

		if (BluetoothAdapter.getDefaultAdapter() == null)
		{
			mTableRowProtocols.setVisibility(RadioGroup.VISIBLE);
			mRadioBluetooth.setVisibility(RadioButton.INVISIBLE);
			mRadioWifi.setChecked(true);
		}

		// Listeners dos radio buttons/toggle buttons

		mRadioTCP.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked)
			{
				if (!mRadioTCP.isClickable())
					return;

				mRadioTCP.setClickable(false);
				mRadioUDP.setChecked(false);
				mRadioUDP.setClickable(true);

				mTableRowRoles.setVisibility(RadioButton.VISIBLE);
			}
		});

		mRadioUDP.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked)
			{
				if (!mRadioUDP.isClickable())
					return;

				mRadioUDP.setClickable(false);
				mRadioTCP.setChecked(false);
				mRadioTCP.setClickable(true);

				mTableRowRoles.setVisibility(RadioButton.VISIBLE);
			}
		});

		mRadioBluetooth.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked)
			{
				hideAllRows();
				if (_isChecked)
				{
					mRadioWifi.setChecked(false);
					mRadioBluetooth.setChecked(true);

					mTableRowRoles.setVisibility(RadioButton.VISIBLE);
				}
			}

		});

		mRadioWifi.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked)
			{

				hideAllRows();
				if (_isChecked)
				{
					mRadioWifi.setChecked(true);
					mRadioBluetooth.setChecked(false);
				}

				if (_isChecked && !checkWifiConnection() && NetUtils.getLocalIpAddress()==null)
				{
					((WifiManager) getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(true);
				}

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
				if (!mRadioClient.isClickable())
					return;

				mRadioClient.setClickable(false);
				mRadioServer.setChecked(false);
				mRadioServer.setClickable(true);

				mButtonStart.setVisibility(mRadioServer.isChecked() && mRadioWifi.isChecked() || _isChecked && mRadioWifi.isChecked() ? RadioGroup.VISIBLE : RadioGroup.INVISIBLE);

				if (_isChecked && mRadioWifi.isChecked())
				{

					
					AlertDialog alertDialog = new AlertDialog.Builder(MultiplayerConnectionActivity.this).create();
					alertDialog.setMessage(getString(R.string.dialog_ask_client_public_text));


					alertDialog.setButton(getString(R.string.public_text), new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							// Publico
							dialog.dismiss();
							
							setupSettings(NetUtils.getLocalIpAddress());
							Settings.PLAYING_ONLINE = true;
							Settings.REMOTE_PROTOCOL_IN_USE = Protocols.TCP;
							
							launchActivity(BuildActivity.class);
						}
					});
					
					alertDialog.setButton2(getString(R.string.private_text), new DialogInterface.OnClickListener()
					{
						
						public void onClick(DialogInterface _dialog, int _which)
						{
							// Privado
							_dialog.dismiss();
							
							mTableRowIpPort.setVisibility(RadioButton.VISIBLE);
							mEditIp.setVisibility(EditText.VISIBLE);
							mTextIp.setVisibility(TextView.VISIBLE);
						}
					});

					alertDialog.show();
					
				} else if (_isChecked && mRadioBluetooth.isChecked())
				{
					if (!checkBluetoothConnection())
					{
						Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
						startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT_CLIENT);
					} else
						listAvailableBTDevices();
				}
			}
		});

		mRadioServer.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked)
			{
				if (!mRadioServer.isClickable())
					return;

				mRadioServer.setClickable(false);
				mRadioClient.setChecked(false);
				mRadioClient.setClickable(true);

				mButtonStart.setVisibility(_isChecked && mRadioWifi.isChecked() || mRadioClient.isChecked() && mRadioWifi.isChecked() ? RadioGroup.VISIBLE : RadioGroup.INVISIBLE);
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
						Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
						startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT_SERVER);
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
				Settings.REMOTE_SERVER_ADDRESS = device[1];
				System.out.println("seleccionado bt: " + Settings.REMOTE_SERVER_ADDRESS);
			}

			public void onNothingSelected(AdapterView<?> _arg0)
			{

			}
		});

		// Regista o BroadcastReceiver para dispositivos encontrados
		// IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		// registerReceiver(mReceiver, filter);
	}

	private void hideAllRows()
	{
		mTableRowProtocols.setVisibility(RadioGroup.GONE);
		mTableRowIpPort.setVisibility(RadioGroup.GONE);
		mTableRowBluetoothDevices.setVisibility(RadioGroup.GONE);
		mTableRowRoles.setVisibility(RadioGroup.GONE);

		mRadioClient.setChecked(false);
		mRadioClient.setClickable(true);
		mRadioServer.setChecked(false);
		mRadioServer.setClickable(true);
		mRadioTCP.setChecked(false);
		mRadioUDP.setChecked(false);

		mButtonStart = (Button) findViewById(R.id.buttonOkDialogMultiplayer);
		mButtonStart.setVisibility(Button.INVISIBLE);

	}

	@Override
	protected void onDestroy()
	{
		// unregisterReceiver(mReceiver);

		removeDialog(DIALOG_PROGRESS);
		super.onDestroy();
	}

	private void listAvailableBTDevices()
	{
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(getResources().getString(R.string.warn_bluetooth_title));
		alertDialog.setMessage(getResources().getString(R.string.warn_bluetooth_text));

		alertDialog.setButton("OK", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();

				mTableRowBluetoothDevices.setVisibility(RadioGroup.VISIBLE);

				BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
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
		});

		alertDialog.show();
	}

	// private final BroadcastReceiver mReceiver = new BroadcastReceiver()
	// {
	// public void onReceive(Context context, Intent intent)
	// {
	// String action = intent.getAction();
	// if (BluetoothDevice.ACTION_FOUND.equals(action))
	// {
	// // Obtém o BluetoothDevice do Intent
	// BluetoothDevice device =
	// intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	// mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	// }
	// }
	// };

	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data)
	{
		switch (_requestCode)
		{
		case REQUEST_ENABLE_BT_CLIENT:
			if (_resultCode == RESULT_CANCELED)
			{
				mRadioBluetooth.setChecked(false);
				Toast t = Toast.makeText(this, this.getString(R.string.error_bluetoothconnection), Toast.LENGTH_SHORT);
				t.show();
			} else
				listAvailableBTDevices();

			break;

		case REQUEST_ENABLE_BT_SERVER:
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
		Settings.BLUETOOTH_ADAPTER = BluetoothAdapter.getDefaultAdapter();
		Settings.START_ANDROID_AS_SERVER = true;
		Settings.REMOTE_PROTOCOL_IN_USE = Protocols.BLUETOOTH;

		launchActivity(PVPServerOptionsActivity.class);
	}

	private void startGameAsBluetoothClient()
	{
		// Prepara as settings para o jogo
		Settings.BLUETOOTH_ADAPTER = BluetoothAdapter.getDefaultAdapter();
		Settings.START_ANDROID_AS_SERVER = false;
		Settings.REMOTE_PROTOCOL_IN_USE = Protocols.BLUETOOTH;

		launchActivity(BuildActivity.class);
	}

	public void onContinueButton(View v)
	{
		final AtomicBoolean connected = new AtomicBoolean(false);

		if (mRadioWifi.isChecked())
		{// WIFI
			connected.set(checkWifiConnection());

			final String localIp = NetUtils.getLocalIpAddress();

			if (!connected.get() && null == localIp)
				Toast.makeText(this, this.getString(R.string.error_wificonnection), Toast.LENGTH_SHORT).show();
			
			else
			{
				// quando está a correr um hotspot, connected = false mas
				// localIp != null

//				AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//				alertDialog.setTitle(getResources().getString(R.string.dialog_multiplayer_title));
//				alertDialog.setMessage(getResources().getString(R.string.dialog_multiplayer_text));
//
//				alertDialog.setButton("OK", new DialogInterface.OnClickListener()
//				{
//					public void onClick(DialogInterface dialog, int which)
//					{
//						dialog.dismiss();
//						setupSettings(localIp);
//						launchActivity(mRadioServer.isChecked() ? PVPServerOptionsActivity.class : BuildActivity.class);
//					}
//				});
//
//				alertDialog.show();
				
				if (mRadioServer.isChecked()){
					askPublicOrPrivateServer();
					return;
				}
				else{
					setupSettings(localIp);
					launchActivity(BuildActivity.class);
				}
			}
		} else
		{// BLUETOOTH
			connected.set(checkBluetoothConnection());
			if (!connected.get())
				Toast.makeText(this, this.getString(R.string.error_bluetoothconnection), Toast.LENGTH_SHORT).show();
			else
				startGameAsBluetoothClient();
		}

		if (connected.get())
			launchActivity(mRadioServer.isChecked() ? PVPServerOptionsActivity.class : BuildActivity.class);

	}

	private void askPublicOrPrivateServer()
	{
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage(getString(R.string.dialog_ask_server_public_text));


		alertDialog.setButton(getString(R.string.public_text), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				// Publico
				dialog.dismiss();
				
				new PrepareOnlineServer().execute();
			}
		});
		
		alertDialog.setButton2(getString(R.string.private_text), new DialogInterface.OnClickListener()
		{
			
			public void onClick(DialogInterface _dialog, int _which)
			{
				// Privado
				_dialog.dismiss();
				
				setupSettings(NetUtils.getLocalIpAddress());
				launchActivity(PVPServerOptionsActivity.class);
			}
		});

		alertDialog.show();
	}

	
	private void setupSettings(String _localIp)
	{
		Settings.PLAYING_ONLINE = false;
		Settings.LOCAL_SERVER_ADDRESS = _localIp + ":" + mEditPort.getText().toString();
		Settings.START_ANDROID_AS_SERVER = mRadioServer.isChecked();
		Settings.REMOTE_PROTOCOL_IN_USE = mRadioTCP.isChecked() ? Protocols.TCP : Protocols.UDP;

		if (mRadioServer.isChecked())
			Settings.REMOTE_SERVER_ADDRESS = "localhost:" + mEditPort.getText().toString();
		else
			Settings.REMOTE_SERVER_ADDRESS = mEditIp.getText().toString() + ":" + mEditPort.getText().toString();

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

	private class PrepareOnlineServer extends AsyncTask<Void, Void, String>
	{
		@Override
		protected void onPreExecute()
		{
			showDialog(DIALOG_PROGRESS);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String _result)
		{
			
			removeDialog(DIALOG_PROGRESS);
			
			if (isCancelled())
			{
				Toast.makeText(getApplication(), getApplication().getString(R.string.error_serverconnection), Toast.LENGTH_SHORT).show();
				return;
			}
			
			String localIp = NetUtils.getLocalIpAddress();
			if( !localIp.equals(_result) )
			{
				AlertDialog alertDialog = new AlertDialog.Builder(MultiplayerConnectionActivity.this).create();
				alertDialog.setTitle(getString(R.string.warning_router_tilte));
				alertDialog.setMessage(getString(R.string.warning_router));

				alertDialog.setButton(getString(R.string.yes), new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						// Publico
						dialog.dismiss();
						
						setupSettings(NetUtils.getLocalIpAddress());
						Settings.REMOTE_PROTOCOL_IN_USE = Protocols.TCP;
						Settings.PLAYING_ONLINE = true;
						launchActivity(PVPServerOptionsActivity.class);
					}
				});
				
				alertDialog.setButton2(getString(R.string.no), new DialogInterface.OnClickListener()
				{
					
					public void onClick(DialogInterface _dialog, int _which)
					{
						// Desistiu
						_dialog.dismiss();
					}
				});

				alertDialog.show();
			}
			
			super.onPostExecute(_result);
		}

		@Override
		protected String doInBackground(Void... _params)
		{
			try
			{
				return NetUtils.getDBResult("get_public_ip.php");

			} catch (IOException e)
			{
				e.printStackTrace();
				cancel(true);
			}

			return "";
		}

	}
	

	
	protected Dialog onCreateDialog(int id)
	{
		Dialog dialog;
		switch (id)
		{
		case DIALOG_PROGRESS:
			try
			{
				removeDialog(DIALOG_PROGRESS);
				dialog = ProgressDialog.show(MultiplayerConnectionActivity.this, "", getApplication().getString(R.string.loading_progress), true, false);

				dialog.setOnCancelListener(new OnCancelListener()
				{
					public void onCancel(DialogInterface _dialog)
					{
						finish();
					}
				});

			} catch (Exception e)
			{
				Toast.makeText(getApplication(), getApplication().getString(R.string.error_connecting_to_top_server), Toast.LENGTH_SHORT).show();
				finish();
				dialog = null;
			}
			break;
		default:
			dialog = null;
		}

		return dialog;
	}
}

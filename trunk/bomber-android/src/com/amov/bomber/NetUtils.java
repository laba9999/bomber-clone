package com.amov.bomber;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.bomber.Settings;

public class NetUtils
{
	public static String getDBResult(String _url) throws IOException
	{
		URL myURL = new URL(Settings.WEBHOST_ADDRESS + _url);

		// Utils.LOG(mWebhost + _url);
		Scanner scanner = new Scanner(new BufferedInputStream(myURL.openStream()));

		StringBuilder sb = new StringBuilder();

		while (scanner.hasNextLine())
			sb.append(scanner.nextLine());

		scanner.close();

		String res = sb.toString().replace("\n", "");

		// remove comentários do webhosting
		if (res.indexOf("<!--") != -1)
			return res.substring(0, res.indexOf("<!--"));
		else
			return res;
	}

	// http://www.droidnova.com/get-the-ip-address-of-your-device,304.html
	public static String getLocalIpAddress()
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
	
	public static String getMacAddress(Activity a)
	{
		WifiManager wifiMan = (WifiManager)a.getSystemService(Context.WIFI_SERVICE);
		
		if (NetUtils.getLocalIpAddress() == null)
			wifiMan.setWifiEnabled(true);

		WifiInfo wifiInf = wifiMan.getConnectionInfo();
		return wifiInf.getMacAddress();
	}
	
	public static String getIMEI(Activity a)
	{
		TelephonyManager idMan = (TelephonyManager)a.getSystemService(Context.TELEPHONY_SERVICE);
		
		String imei = idMan.getDeviceId();
		if(imei == null)
			return getMacAddress(a);
		else return imei;
		
	}
}

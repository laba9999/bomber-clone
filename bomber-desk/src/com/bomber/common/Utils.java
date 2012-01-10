package com.bomber.common;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Scanner;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.badlogic.gdx.math.Rectangle;
import com.bomber.Settings;

public class Utils {
	private static int mUUID = 0;

	public static int getNextUUID()
	{
		return ++mUUID;
	}

	public static void resetUUID()
	{
		mUUID = 0;
	}

	public static boolean rectsOverlap(Rectangle r1, Rectangle r2)
	{
		if (r2.x >= r1.x + r1.width)
			return false;
		if (r2.x + r2.width <= r1.x)
			return false;
		if (r2.y >= r1.y + r1.height)
			return false;
		if (r2.y + r2.height <= r1.y)
			return false;

		return true;
	}

	public static String filterName(String _name)
	{
		String result = _name.replace("\n", "").replace(";", "").replace(">", "").replace(" ", "");

		if (result.length() > 15)
			return result.substring(0, 15);
		else
			return result;
	}

	
}

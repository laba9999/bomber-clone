package com.bomber.remote;

public class RemoteEventType {
	public static final short CREATE = 0;
	public static final short DESTROY = 1;
	public static final short MOVE = 2;
	public static final short STOP = 3;
	public static final short START = 4;
	public static final short SYNC = 5;
	
	// short contém o remoteid da ligação que corresponde ao mRemoteId da classe Player
	// string contém a razão
	public static final short DISCONNECT = 6;
	public static final short JOINED = 7;
	public static final short SELECTED_TEAM = 8;
}
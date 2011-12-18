package com.bomber.remote;

public class EventType {
	public static final short CREATE = 0;
	public static final short DESTROY = 1;
	public static final short MOVE = 2;
	public static final short STOP = 3;
	public static final short START = 4;
	public static final short SYNC = 5;
	public static final short SELECTED_TEAM = 6;

	
	// short contém o remoteid da ligação que corresponde ao mRemoteId do Player
	// string contém a razão
	public static final short DISCONNECT = 7;
	
	// Indica ao cliente que deve aguardar ligações
	
	public static final short LISTEN = 8;
	public static final short PING = 9;
	public static final short PONG = 10;
	public static final short SET_ID = 11;
}
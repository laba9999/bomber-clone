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
	public static final short DISCONNECTED = 7;
	
	// Indica ao cliente para se ligar a outro
	// string contém os dados da ligação p.e "ip:porto"
	public static final short CONNECT_TO = 8;
	public static final short PING = 9;
	public static final short PONG = 10;
	
	// short contém o id
	public static final short SET_ID = 11;
	public static final short LOCAL_SERVER_PORT = 12;
	public static final short STOP_LOCAL_SERVER = 13;
	
	// Indica os segundos até o jogo iniciar
	// o valor dos segundos vem no valShort
	public static final short COUNTDOWN = 14;
	public static final short RANDOM_SEED = 15;
	
	// int indica o id da team
	// short a cor do player
	public static final short JOINED_TEAM = 16;
	public static final short LEFT_TEAM = 17;
	
	public static final short UDP_RESEND = 18;
	public static final short UDP_ACK = 19;
	
	public static final short NAME = 20;
	
	// short tem o tipo de jogo
	// int tem o numero de rounds
	// string tem o nivel a ler
	public static final short INFO = 21;
}
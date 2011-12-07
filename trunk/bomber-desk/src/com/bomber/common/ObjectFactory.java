package com.bomber.common;

import com.bomber.gameobjects.Bomb;
import com.bomber.gameobjects.Drawable;
import com.bomber.gameobjects.Monster;
import com.bomber.gameobjects.Player;
import com.bomber.gameobjects.Tile;
import com.bomber.remote.Message;

public class ObjectFactory {
	public static class CreatePlayer extends Factory<Player> {

		public Player onCreate()
		{
			return new Player();
		}
	}

	public static class CreateTile extends Factory<Tile> {

		short mType;

		public CreateTile(short _type) {
			mType = _type;
		}

		public Tile onCreate()
		{
			return new Tile(mType);
		}
	}

	public static class CreateExplosion extends Factory<Drawable> {

		public Drawable onCreate()
		{
			return new Drawable();
		}
	}

	public static class CreateBomb extends Factory<Bomb> {

		public Bomb onCreate()
		{
			return new Bomb();
		}
	}

	public static class CreateMonter {

		public static Monster create(short _monsterType)
		{
			Monster tmpMonster = null;
			switch (_monsterType)
			{
			// TODO: Adicionar a criação dos vários tipos de monstro...
			// default:
			// throw new
			// InvalidParameterException("Tipo de monstro desconhecido");
			}

			if (null != tmpMonster)
				tmpMonster.mUUID = Utils.getNextUUID();

			return tmpMonster;
		}
	}

	/**
	 * O contructor tem de receber um ID que será colocado no atributo senderID
	 * da mensagem. Este é o mesmo valor que está na class RemoteConnections
	 * (mLocalID).
	 */
	public class CreateMessage extends Factory<Message> {
		short mLocalID;

		public CreateMessage(short _localID) {
			mLocalID = _localID;
		}

		public Message onCreate()
		{
			Message tmpMsg = new Message();
			tmpMsg.senderID = mLocalID;

			return tmpMsg;
		}
	}
}
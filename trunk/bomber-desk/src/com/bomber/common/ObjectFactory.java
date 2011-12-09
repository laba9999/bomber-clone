package com.bomber.common;

import com.bomber.gameobjects.Bomb;
import com.bomber.gameobjects.Drawable;
import com.bomber.gameobjects.Player;
import com.bomber.gameobjects.Tile;
import com.bomber.gameobjects.bonus.Bonus;
import com.bomber.gameobjects.monsters.Monster;
import com.bomber.remote.Message;
import com.bomber.world.GameWorld;

public class ObjectFactory {
	public static class CreatePlayer extends Factory<Player> {

		GameWorld mWorld;

		public CreatePlayer(GameWorld _world) {
			mWorld = _world;
		}

		public Player onCreate()
		{
			return new Player(mWorld);
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

		GameWorld mWorld;

		public CreateBomb(GameWorld _world) {
			mWorld = _world;
		}

		public Bomb onCreate()
		{
			return new Bomb(mWorld);
		}
	}

	public static class CreateMonster extends Factory<Monster> {
		GameWorld mWorld;

		public CreateMonster(GameWorld _world) {
			mWorld = _world;
		}

		public Monster onCreate()
		{
			return new Monster(mWorld);
		}
	}

	public static class CreateBonus {

		public static Bonus create(short _bonusType, GameWorld _world)
		{
			Bonus tmpBonus = null;
			switch (_bonusType)
			{
			// TODO: Adicionar a cria��o dos v�rios tipos de bonus...
			// default:
			// throw new
			// InvalidParameterException("Tipo de bonus desconhecido");
			}

			if (null != tmpBonus)
				tmpBonus.mUUID = Utils.getNextUUID();

			return tmpBonus;
		}
	}

	/**
	 * O contructor tem de receber um ID que ser� colocado no atributo senderID
	 * da mensagem. Este � o mesmo valor que est� na class RemoteConnections
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
package com.bomber.common;

import java.io.IOException;
import java.security.InvalidParameterException;

import com.bomber.OverlayingText;
import com.bomber.gameobjects.Bomb;
import com.bomber.gameobjects.Drawable;
import com.bomber.gameobjects.Player;
import com.bomber.gameobjects.Tile;
import com.bomber.gameobjects.bonus.Bonus;
import com.bomber.gameobjects.bonus.BonusBombCount;
import com.bomber.gameobjects.bonus.BonusDoublePoints;
import com.bomber.gameobjects.bonus.BonusExplosionSize;
import com.bomber.gameobjects.bonus.BonusNewLife;
import com.bomber.gameobjects.bonus.BonusPush;
import com.bomber.gameobjects.bonus.BonusShield;
import com.bomber.gameobjects.bonus.BonusSpeed;
import com.bomber.gameobjects.monsters.Monster;
import com.bomber.gametypes.GameTypeCTF;
import com.bomber.gametypes.GameTypeCampaign;
import com.bomber.gametypes.GameTypeDeathmatch;
import com.bomber.gametypes.GameTypeHandler;
import com.bomber.remote.Connection;
import com.bomber.remote.Message;
import com.bomber.remote.MessageContainer;
import com.bomber.remote.Protocols;
import com.bomber.remote.bluetooth.BluetoothMessageSocketIO;
import com.bomber.remote.tcp.TCPMessageSocketIO;
import com.bomber.remote.udp.UDPMessage;
import com.bomber.remote.udp.UDPMessageSocketIO;
import com.bomber.world.GameWorld;

public final class ObjectFactory {

	public static class CreateConnection {

		public static Connection Create(short _protocol, String _connectionString, MessageContainer _msgContainer) throws NumberFormatException, IOException
		{
			String[] data = _connectionString.split(":");
			switch (_protocol)
			{
			case Protocols.TCP:
				return new Connection(new TCPMessageSocketIO(data[0], Integer.valueOf(data[1])), _msgContainer);
			case Protocols.UDP:
				return new Connection(new UDPMessageSocketIO(data[0], Integer.valueOf(data[1])), _msgContainer);
			case Protocols.BLUETOOTH:
				return new Connection(new BluetoothMessageSocketIO(_connectionString), _msgContainer);
			default:
				throw new InvalidParameterException("Protocolo não implementado!");
			}
		}
	}

	public static class CreateGameTypeHandler {

		public static GameTypeHandler Create(short _gameType)
		{
			switch (_gameType)
			{
			case GameTypeHandler.CAMPAIGN:
				return new GameTypeCampaign();

			case GameTypeHandler.DEADMATCH:
			case GameTypeHandler.TEAM_DEADMATCH:
				return new GameTypeDeathmatch();

			case GameTypeHandler.CTF:
			case GameTypeHandler.TEAM_CTF:
				return new GameTypeCTF();

			default:
				throw new InvalidParameterException("Tipo de jogo não implementado!");
			}
		}
	}

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

	public static class CreateUDPMessage extends Factory<UDPMessage> {
		public UDPMessage onCreate()
		{
			return new UDPMessage();
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

	public static class CreateOverlayingText extends Factory<OverlayingText> {
		@Override
		public OverlayingText onCreate()
		{
			return new OverlayingText();
		}
	}

	public static class CreateBonus {

		public static Bonus create(short _bonusType)
		{
			Bonus tmpBonus = null;
			switch (_bonusType)
			{
			case 0:
				tmpBonus = new BonusDoublePoints();
				break;
			case 1:
				tmpBonus = new BonusNewLife();
				break;
			case 2:
				tmpBonus = new BonusExplosionSize();
				break;
			case 3:
				tmpBonus = new BonusBombCount();
				break;
			case 4:
				tmpBonus = new BonusShield();
				break;
			case 5:
				tmpBonus = new BonusSpeed();
				break;
			case 6:
				tmpBonus = new BonusPush();
				break;
			default:
				throw new InvalidParameterException("Tipo de bonus desconhecido");
			}

			return tmpBonus;
		}
	}

	/**
	 * O contructor tem de receber um ID que será colocado no atributo senderID
	 * da mensagem. Este é o mesmo valor que está na class RemoteConnections
	 * (mLocalID).
	 */
	public static class CreateMessage extends Factory<Message> {

		public Message onCreate()
		{
			return new Message();
		}
	}
}
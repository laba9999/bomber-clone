package com.bomber.common;

public class ObjectFactory {
	public static class CreatePlayer extends Factory {

		public Object onCreate()
		{
			throw new UnsupportedOperationException();
		}
	}

	public static class CreateExplosion extends Factory {

		public Object onCreate()
		{
			throw new UnsupportedOperationException();
		}
	}

	public static class CreateMonster extends Factory {

		public Object onCreate()
		{
			throw new UnsupportedOperationException();
		}
	}

	public static class CreateBomb extends Factory {

		public Object onCreate()
		{
			throw new UnsupportedOperationException();
		}
	}

	public static class CreateBonus extends Factory {

		public Object onCreate()
		{
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * O contructor tem de receber um ID que será colocado no atributo senderID
	 * da mensagem. Este é o mesmo valor que está na class RemoteConnections
	 * (mLocalID).
	 */
	public class CreateMessage extends Factory {

		public Object onCreate()
		{
			throw new UnsupportedOperationException();
		}
	}
}
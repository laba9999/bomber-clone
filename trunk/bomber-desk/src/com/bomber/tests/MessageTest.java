package com.bomber.tests;

import java.nio.ByteBuffer;

import org.junit.Test;

import com.bomber.remote.Message;
import com.bomber.remote.MessageSocketIO;

public class MessageTest {

	@Test
	public void testParse()
	{
		Message m = new Message();
		
		m.mOwnerUUID = 0;
		m.senderID = 1;
		m.UUID = 2;
		m.valInt = 123456;
		m.valShort = 321;
		m.valVector2_0.set(0.01f,0.02f);
		m.setStringValue("01234567890123456789012345678901");
		
		byte[] array = new byte[Message.MESSAGE_SIZE];
		ByteBuffer buffer = ByteBuffer.wrap(array);
		
		m.fillBuffer(buffer);
		
		Message m2 = new Message();
		m2.parse(buffer);
	}

}

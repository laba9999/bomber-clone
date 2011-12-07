package com.bomber.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.bomber.world.Clock;

public class ClockTest {

	@Test
	public void test()
	{
		Clock tmpClock = new Clock();
		tmpClock.reset(1, 12);
		tmpClock.start();

		while (!tmpClock.mReachedZero)
		{
			System.out.println(tmpClock.toString());
			try
			{
				Thread.sleep(1000);

			} catch (InterruptedException e)
			{
			}
		}

		assertTrue(tmpClock.mReachedZero);
	}

}

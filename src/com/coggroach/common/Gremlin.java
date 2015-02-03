package com.coggroach.common;

import java.util.Random;

import com.coggroach.packet.Packet;

public class Gremlin
{
	public static int EVILFACTOR = 12000;	
	public static int CORRUPTFACTOR = 8000;
	
	public static Packet receive(Packet p)
	{
		Random rand = new Random();
		if(rand.nextInt(EVILFACTOR) == 0)
		{
			print();
			return null;
		}
		if(rand.nextInt(CORRUPTFACTOR) == 0)
		{
			print();
			return corrupt(p, rand);
		}		
		return p;
	}
	
	private static Packet corrupt(Packet p, Random rand)
	{
		p.setByte(rand.nextInt(NetworkInfo.getMaxPacketSize()), (byte) rand.nextInt(256));		
		return p;
	}
	
	private static void print()
	{
		System.out.println("Gremlined");
	}
}

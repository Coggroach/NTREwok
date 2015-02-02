package com.coggroach.packet;

import java.io.IOException;
import java.io.InputStream;

import com.coggroach.common.NetworkInfo;

public class Packet
{
	private static final byte FLAG_C = 0b01111110;
	
	
	private byte[] stream;

	public Packet()
	{
		this.stream = new byte[NetworkInfo.getMaxPacketSize()];
	}
		
	public byte[] getBytes()
	{		
		return this.stream;
	}

	private void initPacket()
	{
		this.stream[0] = FLAG_C;
		this.stream[this.stream.length - 1] = FLAG_C;
	}

	public Packet getPacketToSend(byte b)
	{
		this.initPacket();
		this.preformChecksum();
		this.setPacketId(b);
		return this;
	}

	private void setPacketId(byte b)
	{
		this.stream[ NetworkInfo.getIndex(NetworkInfo.PROTOCOL) ] = b;
	}
	
	public String getString()
	{	
		return new String(stream);		
	}
	
	public void print()
	{
		System.out.println(this.getString());
	}

	private void preformChecksum()
	{

	}
	
	public void add(String s) throws PacketException
	{
		this.add(s.getBytes());
	}
	
	public void add(InputStream input) throws IOException
	{
		input.read(this.stream);		
	}

	private void add(byte[] b) throws PacketException
	{
		int length = NetworkInfo.getLength(NetworkInfo.PAYLOAD);
		int index = NetworkInfo.getIndex(NetworkInfo.PAYLOAD);
		
//		System.out.println("PayloadLength: " + b.length);
//		System.out.println("StreamLength: " + this.stream.length);
//		System.out.println("Index: " + index);
//		System.out.println("Length: " + length);
		
		if(b.length > length)
			throw new PacketException();
		
		for(int i = 0, j = 0; i < length && j < b.length; i++, j++)
		{			
			this.stream[i + index - 1] = b[j];
		}
	}

}

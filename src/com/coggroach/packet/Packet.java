package com.coggroach.packet;

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
//		for (int i = 0; i < NetworkInfo.PROTOCOL_LENGTH; i++)
//		{
//			this.stream[NetworkInfo.getIndex(NetworkInfo.PROTOCOL)] = (i == NetworkInfo.PROTOCOL_LENGTH - 1) ? b : 0;
//		}
	}
	
	public String getStringToSend(byte b)
	{
		return this.getPacketToSend(b).getString();
	}
	
	private String getString()
	{
		String s = "";
		for(int i = 0; i < this.stream.length; i++)
		{
			s += stream[i];
		}
		return s;
	}

	private void preformChecksum()
	{

	}
	
	public void add(String s) throws PacketException
	{
		this.add(s.getBytes());
	}

	public void add(byte[] b) throws PacketException
	{
		if(b.length > NetworkInfo.getLength(NetworkInfo.PAYLOAD))
			throw new PacketException();
		
		for(int i = NetworkInfo.getIndex(NetworkInfo.PAYLOAD - 1); i < NetworkInfo.getIndex(NetworkInfo.PAYLOAD); i++)
		{
			this.stream[i] = b[i];
		}
	}

}

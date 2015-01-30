package com.coggroach.proxy.packet;

import java.util.ArrayList;
import java.util.List;

import com.coggroach.proxy.SocketData;

public class Packet
{
	private byte[] header;
	private byte[] trailer;
	private byte[] protocol;
	private List<String> payload;
	private byte[] checksum;

	public static final Packet HANDSHAKE_PACKET = new Packet();
	public static final Packet ACK_PACKET = new Packet();
	public static final Packet NAK_PACKET = new Packet();
	public static final Packet TERMINATE_PACKET = new Packet();
	
	static
	{
		HANDSHAKE_PACKET.addToPayload("GoodMorning");
		ACK_PACKET.addToPayload("ACK");
		NAK_PACKET.addToPayload("NAK");
		TERMINATE_PACKET.addToPayload("ByeBye");		
		
		byte index = 0;
		
		HANDSHAKE_PACKET.getPacketToSend(index++);
		ACK_PACKET.getPacketToSend(index++);
		NAK_PACKET.getPacketToSend(index++);
		TERMINATE_PACKET.getPacketToSend(index++);
	}

	private static final byte FLAG_C = 0b01111110;

	public Packet()
	{
		this.header = new byte[SocketData.HEADER_LENGTH];
		this.trailer = new byte[SocketData.TRAILER_LENGTH];
		this.payload = new ArrayList<String>();
		this.checksum = new byte[SocketData.CHECKSUM_LENGTH];
		this.protocol = new byte[SocketData.PROTOCOL_LENGTH];
	}

	private void initPacket()
	{
		for (int i = 0; i < header.length; i++)
		{
			header[i] = FLAG_C;
		}
		for (int i = 0; i < trailer.length; i++)
		{
			trailer[i] = FLAG_C;
		}
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
		for (int i = 0; i < protocol.length; i++)
		{
			protocol[i] = (i == protocol.length - 1) ? b : 0;
		}
	}
	
	public String getStringToSend(byte b)
	{
		return this.getPacketToSend(b).getString();
	}
	
	private String getString()
	{
		String s = "";
		for(int i = 0; i < header.length; i++)
		{
			s += header[i];
		}
		for(int i = 0; i < protocol.length; i++)
		{
			s += protocol[i];
		}
		for(int i = 0; i < payload.size(); i++)
		{
			s += payload.get(i);
		}
		for(int i = 0; i < checksum.length; i++)
		{
			s += checksum.length;
		}
		for(int i = 0; i < trailer.length; i++)
		{
			s += trailer[i];
		}
		return s;
	}

	private void preformChecksum()
	{
		for (int i = 0; i < checksum.length; i++)
		{
			this.checksum[i] = 0b00000000;
		}
	}

	public void addToPayload(String s)
	{
		this.payload.add(s);
	}

}

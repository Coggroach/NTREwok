package com.coggroach.proxy.packet;

import com.coggroach.proxy.SocketData;

public class Packet
{
	private byte[] header;
	private byte[] trailer;
	private byte[] protocol;
	private byte[] payload;
	private byte[] checksum;

	public static final Packet HANDSHAKE_PACKET = new Packet();
	public static final Packet ACK_PACKET = new Packet();
	public static final Packet NAK_PACKET = new Packet();
	public static final Packet TERMINATE_PACKET = new Packet();
	
	static
	{
		HANDSHAKE_PACKET.addToPayload("GoodMorning".getBytes());
		ACK_PACKET.addToPayload("ACK".getBytes());
		NAK_PACKET.addToPayload("NAK".getBytes());
		TERMINATE_PACKET.addToPayload("ByeBye".getBytes());		
		
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
		this.payload = new byte[SocketData.MAX_PAYLOAD_LENGTH];
		this.checksum = new byte[SocketData.CHECKSUM_LENGTH];
		this.protocol = new byte[SocketData.PROTOCOL_LENGTH];
	}
	
	private int getTotalPacketSize()
	{
		return this.header.length + this.protocol.length + this.payload.length + this.checksum.length + this.trailer.length;
	}
	
	public byte[] getBytes()
	{
		byte[] total = new byte[this.getTotalPacketSize()];
		
		System.arraycopy(header, 0, total, 0, header.length);
		System.arraycopy(protocol, 0, total, 0, protocol.length);
		System.arraycopy(payload, 0, total, 0, payload.length);
		System.arraycopy(checksum, 0, total, 0, checksum.length);
		System.arraycopy(trailer, 0, total, 0, trailer.length);
		
		return total;
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
		for(int i = 0; i < payload.length; i++)
		{
			s += payload[i];
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

	public void addToPayload(byte[] b)
	{
		this.payload = b;
	}

}

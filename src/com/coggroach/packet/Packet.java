package com.coggroach.packet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.coggroach.common.NetworkInfo;

public class Packet
{
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
		this.stream[0] = NetworkInfo.FLAG_C;
		this.stream[this.stream.length - 1] = NetworkInfo.FLAG_C;
	}

	private void setChecksum(int i)
	{
		this.stream[NetworkInfo.getIndex(NetworkInfo.CHECKSUM)] = (byte) i;
	}

	public Packet getPacketToSend(byte b)
	{
		this.initPacket();
		this.setChecksum(this.preformChecksum());
		this.setAddress(b);
		return this;
	}

	public void setByte(int pos, byte b)
	{
		this.stream[pos] = b;
	}

	public Packet wrap()
	{
		this.initPacket();
		this.setChecksum(0);
		return this;
	}

	public void setAddress(byte b)
	{
		this.stream[NetworkInfo.getIndex(NetworkInfo.ADDRESS)] = b;
	}

	public void setProtocol(byte b)
	{
		this.stream[NetworkInfo.getIndex(NetworkInfo.PROTOCOL)] = b;
	}

	public String getString()
	{
		return new String(Arrays.copyOfRange(stream,
				NetworkInfo.getIndex(NetworkInfo.PAYLOAD),
				NetworkInfo.getIndex(NetworkInfo.PAYLOAD + 1)));
	}

	public void print()
	{
		System.out.println(this.getString());
	}

	private int preformChecksum()
	{
		int index = NetworkInfo.getIndex(NetworkInfo.PAYLOAD);
		int length = NetworkInfo.getLength(NetworkInfo.PAYLOAD);
		int iTotal = 0;

		for (int i = 0; i < length; i++)
		{
			iTotal += (int) (this.stream[i + index] << (i * 8));
		}

		iTotal %= 256;

		return iTotal;
	}

	public byte getAddress()
	{
		return this.stream[NetworkInfo.getIndex(NetworkInfo.ADDRESS)];
	}

	public boolean isSameProtocol(byte b)
	{
		return this.stream[NetworkInfo.getIndex(NetworkInfo.PROTOCOL)] == b;
	}

	public boolean hasSameAddress(byte b)
	{
		return this.stream[NetworkInfo.getIndex(NetworkInfo.ADDRESS)] == b;
	}

	public boolean isValid()
	{
		return this.preformChecksum() == this.stream[NetworkInfo.getIndex(NetworkInfo.CHECKSUM)] && 
				this.stream[ NetworkInfo.getIndex(NetworkInfo.HEADER)] == NetworkInfo.FLAG_C &&
				this.stream[ NetworkInfo.getIndex(NetworkInfo.TRAILER)] == NetworkInfo.FLAG_C;
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

		// System.out.println("PayloadLength: " + b.length);
		// System.out.println("StreamLength: " + this.stream.length);
		// System.out.println("Index: " + index);
		// System.out.println("Length: " + length);

		if (b.length > length)
			throw new PacketException();

		for (int i = 0, j = 0; i < length && j < b.length; i++, j++)
		{
			this.stream[i + index] = b[j];
		}
	}

}

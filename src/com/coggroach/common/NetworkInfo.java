package com.coggroach.common;

public class NetworkInfo
{
	public static final int SOCKET = 4991;
	public static final String IPADDRESS = "localhost";
	public static final int TIMEOUT = 8000;
	
	private static final int HEADER_LENGTH = 1;
	private static final int TRAILER_LENGTH = 1;
	private static final int ADDRESS_LENGTH = 1;
	private static final int CHECKSUM_LENGTH = 1;
	private static final int MAX_PAYLOAD_LENGTH = 16;
	private static final int PROTOCOL_LENGTH = 1;
	
	public static final int HEADER = 0;
	public static final int ADDRESS = 1;
	public static final int PROTOCOL = 2;
	public static final int PAYLOAD = 3;
	public static final int CHECKSUM = 4;
	public static final int TRAILER = 5;
	public static final int LENGTH = 6;
	
	public static byte ACK_PROTOCOL;
	public static byte NAK_PROTOCOL;
	public static byte SND_PROTOCOL;
	public static byte END_PROTOCOL;
	public static byte FLAG_C;
	public static byte MASK_C;
	
	static
	{
		ACK_PROTOCOL = 0b00111100;
		NAK_PROTOCOL = 0b01100110;
		SND_PROTOCOL = 0b00000001;
		END_PROTOCOL = 0b00001111;
		FLAG_C = 0b01111110;
		MASK_C = 0b01010101;
	}
	
	private static int MAX_PACKET_STORAGE = (int) Math.pow(2, ADDRESS_LENGTH * 8);
	
	public static int getMaxPacketStorage()
	{
		return MAX_PACKET_STORAGE;
	}
	
	public static int getMaxPacketSize()
	{
		return HEADER_LENGTH + PROTOCOL_LENGTH + MAX_PAYLOAD_LENGTH + CHECKSUM_LENGTH + TRAILER_LENGTH;
	}
	
	public static int getLength(int code)
	{
		switch(code)
		{		
		case TRAILER:
			return TRAILER_LENGTH;
		case CHECKSUM:
			return CHECKSUM_LENGTH;
		case PAYLOAD:
			return MAX_PAYLOAD_LENGTH;
		case PROTOCOL:
			return PROTOCOL_LENGTH;
		case HEADER:
			return HEADER_LENGTH;
		case ADDRESS:
			return ADDRESS_LENGTH;
		}
		return 0;
	}
	
	public static int getIndex(int code)
	{
		int index = 0;
		switch(code)
		{		
		case LENGTH:
			index += TRAILER_LENGTH;
		case TRAILER:
			index += CHECKSUM_LENGTH;
		case CHECKSUM:
			index += MAX_PAYLOAD_LENGTH;
		case PAYLOAD:
			index += PROTOCOL_LENGTH;
		case PROTOCOL:
			index += ADDRESS_LENGTH;
		case ADDRESS:
			index += HEADER_LENGTH;
		case HEADER:
			break;
		}
		return index;
	}
	
	public static int getIndex(int code, int back)
	{
		return getIndex(code + 1) - 1;
	}	
}

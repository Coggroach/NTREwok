package com.coggroach.common;

public class NetworkInfo
{
	public static final int SOCKET = 4991;
	public static final String IPADDRESS = "localhost";
	public static final int TIMEOUT = 2000;
	
	private static final int HEADER_LENGTH = 1;
	private static final int TRAILER_LENGTH = 1;
	private static final int CHECKSUM_LENGTH = 2;
	private static final int MAX_PAYLOAD_LENGTH = 8;
	private static final int PROTOCOL_LENGTH = 2;
	
	public static final int HEADER = 0;
	public static final int PROTOCOL = 1;
	public static final int PAYLOAD = 2;
	public static final int CHECKSUM = 3;
	public static final int TRAILER = 4;
	public static final int LENGTH = 5;
	
	
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
			index += HEADER_LENGTH;
		case HEADER:
			break;
		}
		return index;
	}
	
}

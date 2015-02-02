package com.coggroach.packet;

import java.util.ArrayList;
import java.util.List;

import com.coggroach.common.NetworkInfo;

public class PacketHandler
{
	private List<Packet> packets;
	
	public PacketHandler()
	{
		this.packets = new ArrayList<Packet>();
	}
	
	public List<Packet> getPackets()
	{
		return this.packets;
	}
	
	public void clear()
	{
		packets.clear();
	}
	
	public void process(String s) throws PacketException
	{
		int length = NetworkInfo.getLength(NetworkInfo.PAYLOAD);
		int count = s.length()/length;
		int index = 0;
		for(int i = 0; i < count; i++)
		{
			Packet p = new Packet();
			p.add(s.substring(index, index + length));
			p.getPacketToSend((byte) i);		
			this.packets.add(p);
			index += length;
		}
	}
	
	public int length()
	{
		return this.packets.size();
	}
}

package com.coggroach.packet;

import java.util.ArrayList;
import java.util.List;

public class PacketHandler
{
	private List<Packet> packets;
	
	public PacketHandler()
	{
		this.packets = new ArrayList<Packet>();
	}
	
	public Packet[] getPackets()
	{
		return (Packet[]) this.packets.toArray();
	}
	
	
}

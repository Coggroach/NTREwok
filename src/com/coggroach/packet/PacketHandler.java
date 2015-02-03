package com.coggroach.packet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.coggroach.common.NetworkInfo;

public class PacketHandler
{
	private List<Packet> packets;		
	private List<Byte> naks;
	private int index;
	
	public PacketHandler()
	{
		this.packets = new ArrayList<Packet>();		
		this.naks = new ArrayList<Byte>();
		this.index = 0;
	}
	
	public List<Packet> getPackets()
	{
		return this.packets;
	}
	
	public void clear()
	{
		this.packets.clear();		
		this.naks.clear();	
		this.index = 0;
	}
	
	public boolean isEmpty()
	{
		return this.packets.isEmpty();
	}
	
	public Packet getNext()
	{
		if(this.naks.isEmpty())
		{
			return this.packets.get(index++);				
		}
		
		Iterator<Byte> iterator = this.naks.iterator();
		byte address = iterator.next();
		iterator.remove();	
		
		return this.getPacketWithAddress(address);
	}
	
	private Packet getPacketWithAddress(byte b)
	{
		Iterator<Packet> iterator = this.packets.iterator();
		while(iterator.hasNext())
		{
			Packet p = iterator.next();
			if(p.hasSameAddress(b))
				return p;
		}
		return null;
	}
	
	private void removePacketWithAddress(byte b)
	{
		Iterator<Packet> iterator = this.packets.iterator();
		while(iterator.hasNext())
		{
			if(iterator.next().hasSameAddress(b))
				iterator.remove();
		}
	}
	
	public void onReceive(Packet p)
	{
		if(p.isSameProtocol(NetworkInfo.ACK_PROTOCOL))
		{
			this.removePacketWithAddress(p.getAddress());
		}
		else if(p.isSameProtocol(NetworkInfo.NAK_PROTOCOL))
		{
			this.naks.add(p.getAddress());
		}
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
			p.setProtocol(NetworkInfo.SND_PROTOCOL);
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

package com.coggroach.packet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.coggroach.common.NetworkInfo;

public class PacketHandler
{
	public static final int SERVER = 0;
	public static final int CLIENT = 1;

	private List<Packet> packets;
	private List<Byte> naks;
	private int index;
	private int mode;
	private boolean forceStop;
	private boolean endOfStream;

	public PacketHandler(int i)
	{
		this.packets = new ArrayList<Packet>();
		this.naks = new ArrayList<Byte>();
		this.index = 0;
		this.mode = i;
		this.forceStop = false;
		this.endOfStream = false;
	}
	
	public void stop()
	{
		this.forceStop = true;
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

	public void add(Packet p)
	{
		if(this.mode == SERVER)
		{
			if(p.isValid())
			{
				this.packets.add(p);
			}
			else
			{
				this.naks.add(p.getAddress());
			}
		}
	}

	public List<Byte> getMissingPacketAddress()
	{
		return null;
	}

	public static Packet getAckPacket(byte address)
	{
		Packet p = new Packet();
		p.setProtocol(NetworkInfo.ACK_PROTOCOL);
		p.setAddress(address);
		return p.wrap();
	}

	public static Packet getNakPacket(byte address)
	{
		Packet p = new Packet();
		p.setProtocol(NetworkInfo.NAK_PROTOCOL);
		p.setAddress(address);
		return p.wrap();
	}

	public void print()
	{
		System.out.println("PacketHandler");
		System.out.println("Running: " + !this.forceStop);
		System.out.println("Packets: " + this.packets.size());
		System.out.println("Naks   : " + this.naks.size());
		System.out.println("Mode   : " + this.mode);
	}

	public void print(int i)
	{
		System.out.println("Packets: " + this.packets.size());
	}

	public boolean isEmpty()
	{
		if(this.forceStop)
			return true;
		
		switch(mode)
		{
			case CLIENT:
				return this.packets.isEmpty() && this.naks.isEmpty();
			case SERVER:
				return this.endOfStream && this.packets.isEmpty();
		}
		return false;
	}
	
	public boolean isCompleted()
	{
		return this.endOfStream || this.forceStop;
	}

	public Packet getNext()
	{
		switch (mode)
		{
			case CLIENT:
				return getNextClient();
			case SERVER:
				return getNextServer();
		}
		return null;
	}
	
	private Packet getNextServer()
	{
		Packet p = null;
		Packet ack = null;
		
		if(!this.naks.isEmpty())
		{
			Iterator<Byte> iterator = this.naks.iterator();
			byte address = iterator.next();
			iterator.remove();

			p = this.getPacketWithAddress(address);
			ack = getNakPacket(p.getAddress());
		}
		else
		{
			if (index < this.packets.size())
			{
				p = this.packets.get(index++);
				ack = getAckPacket(p.getAddress());
				
				if(p != null)
				if(p.isSameProtocol(NetworkInfo.END_PROTOCOL))
					this.endOfStream = true;		
			}
		}		
		return ack;
	}

	private Packet getNextClient()
	{
		if (this.naks.isEmpty())
		{
			if (index < this.packets.size())
				return this.packets.get(index++);

			return null;
		}

		Iterator<Byte> iterator = this.naks.iterator();
		byte address = iterator.next();
		iterator.remove();

		return this.getPacketWithAddress(address);
	}

	private Packet getPacketWithAddress(byte b)
	{
		Iterator<Packet> iterator = this.packets.iterator();
		while (iterator.hasNext())
		{
			Packet p = iterator.next();
			if (p.hasSameAddress(b))
				return p;
		}
		return null;
	}

	private void removePacketWithAddress(byte b)
	{
		Iterator<Packet> iterator = this.packets.iterator();
		while (iterator.hasNext())
		{
			if (iterator.next().hasSameAddress(b))
			{
				iterator.remove();
				index--;
				return;
			}
		}
	}

	public void onReceive(Packet p)
	{
		if (p.isSameProtocol(NetworkInfo.ACK_PROTOCOL))
		{
			this.removePacketWithAddress(p.getAddress());
		}
		else if (p.isSameProtocol(NetworkInfo.NAK_PROTOCOL))
		{
			this.naks.add(p.getAddress());			
		}
	}

	public void process(String s) throws PacketException
	{
		int length = NetworkInfo.getLength(NetworkInfo.PAYLOAD);
		int count = (int) Math.ceil(s.length() / length);
		int index = 0;
		for (int i = 0; i < count; i++)
		{
			Packet p = new Packet();
			p.add(s.substring(index, index + length));
			
			if(i == count - 1)
				p.setProtocol(NetworkInfo.END_PROTOCOL);
			else
				p.setProtocol(NetworkInfo.SND_PROTOCOL);
			
			p.wrap((byte) i);
			this.packets.add(p);
			index += length;
		}
	}
	
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		Iterator<Packet> iterator = this.packets.iterator();
		while(iterator.hasNext())
		{
			builder.append(iterator.next().getString());
		}
		return builder.toString();
	}

	public int length()
	{
		return this.packets.size();
	}
}

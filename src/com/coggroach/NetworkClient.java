package com.coggroach;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.coggroach.common.FileIO;
import com.coggroach.common.NetworkInfo;
import com.coggroach.packet.Packet;
import com.coggroach.packet.PacketException;
import com.coggroach.packet.PacketHandler;
import com.coggroach.proxy.CommonSocket;

public class NetworkClient
{
	public static void main(String args[])
	{
		CommonSocket client = null;
		PacketHandler handler = new PacketHandler();
		File file = new File("res/Input.txt");
		//boolean isRunning = true;

		try
		{
			client = new CommonSocket(NetworkInfo.SOCKET);
			client.setTimeOut();
			client.setIdentity("Client0");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}

		try
		{
			System.out.println("Identity: " + client.getIdentity());
			handler.process(FileIO.readFromFile(file));
			List<Packet> packets = handler.getPackets();			
			System.out.println("Packets to Transmit: " + packets.size());

			for (int i = 0; i < packets.size(); i++)
			{
				client.transmit(packets.get(i));
				client.receive().print();
			}
			handler.clear();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (PacketException e)
		{
			e.printStackTrace();
		}

	}
}

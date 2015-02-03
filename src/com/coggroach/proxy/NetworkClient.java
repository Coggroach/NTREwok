package com.coggroach.proxy;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.coggroach.common.FileIO;
import com.coggroach.common.NetworkInfo;
import com.coggroach.packet.Packet;
import com.coggroach.packet.PacketException;
import com.coggroach.packet.PacketHandler;
import com.coggroach.socket.CommonSocket;

public class NetworkClient
{
	public static void main(String args[])
	{
		CommonSocket client = null;
		PacketHandler handler = new PacketHandler();
		File file = new File("res/Input.txt");
		// boolean isRunning = true;

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

			while(!handler.isEmpty())
			{
				client.transmit(handler.getNext());
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

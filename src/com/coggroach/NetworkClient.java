package com.coggroach;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.coggroach.common.FileIO;
import com.coggroach.proxy.Client;
import com.coggroach.proxy.SocketData;
import com.coggroach.proxy.packet.Packet;

public class NetworkClient
{
	public static void main(String args[])
	{
		Client client = null;
		File file = new File("res/Input.txt");
		boolean isRunning = true;
		
		try
		{
			client = new Client(SocketData.SOCKET);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}

		while (isRunning)
		{
			try
			{				
				client.transmit(Packet.HANDSHAKE_PACKET);
				client.transmit(FileIO.readFromFile(file));
				client.transmit(Packet.TERMINATE_PACKET);
				System.out.println(client.receive());
				isRunning = false;
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
	}
}

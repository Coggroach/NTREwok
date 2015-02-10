package com.coggroach.proxy;

import java.io.File;
import java.io.IOException;

import com.coggroach.common.FileIO;
import com.coggroach.common.NetworkInfo;
import com.coggroach.packet.Packet;
import com.coggroach.packet.PacketException;
import com.coggroach.packet.PacketHandler;
import com.coggroach.socket.BaseSocket;
import com.coggroach.socket.event.SocketListener;

public class NetworkClient
{
	static BaseSocket client = null;
	static PacketHandler handler = new PacketHandler(PacketHandler.CLIENT);

	public static void main(String args[])
	{
		try
		{
			client = new BaseSocket(NetworkInfo.SOCKET);
			client.setTimeOut();
			client.setIdentity("Client0");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}

		client.setSocketListener(new SocketListener()
		{
			@Override
			public boolean listen()
			{
				boolean run = true;
				while (run)
					try
					{
						if (client.available())
							handler.onReceive(client.receive());
					}
					catch (IOException e)
					{
						run = false;
						handler.stop();
					}
				return run;
			}
		});

		try
		{
			client.print();

			handler.process(FileIO.readFromFile(new File(FileIO.INPUT)));
			handler.print();

			client.run();

			while (!handler.isEmpty())
			{
				Packet p = handler.getNext();
				if (p != null)
				{
					client.transmit(p);
				}
			}

			handler.stop();
			handler.print();
			handler.clear();
			client.close();
		}
		catch (IOException e)
		{
			System.err.println("Client Disconnected");
		}
		catch (PacketException e)
		{
			e.printStackTrace();
		}
		System.exit(0);

	}
}

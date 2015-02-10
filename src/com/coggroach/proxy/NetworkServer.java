package com.coggroach.proxy;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import com.coggroach.common.FileIO;
import com.coggroach.common.Gremlin;
import com.coggroach.common.NetworkInfo;
import com.coggroach.packet.Packet;
import com.coggroach.packet.PacketHandler;
import com.coggroach.socket.BaseSocket;
import com.coggroach.socket.event.SocketListener;

public class NetworkServer
{
	static ServerSocket server;
	static BaseSocket common = null;
	static PacketHandler handler = new PacketHandler(PacketHandler.SERVER);

	public static void main(String args[])
	{
		try
		{
			server = new ServerSocket(NetworkInfo.SOCKET);
			common = new BaseSocket(server.accept(), "Server0");
			common.setTimeOut();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}

		common.setSocketListener(new SocketListener()
		{
			@Override
			public boolean listen()
			{
				boolean run = true;
				while (run)
					try
					{
						if (common.available())
							handler.add(common.receive());// Gremlin.receive(common.receive());
					}
					catch (IOException e)
					{
						System.err.println("Client Disconnected");
						handler.stop();
						run = false;
					}
				return run;
			}
		});

		try
		{
			common.print();
			handler.print();

			common.run();

			while (!handler.isCompleted())
			{
				Packet p = handler.getNext();
				if (p != null)
				{
					common.transmit(PacketHandler.getAckPacket(p.getAddress()));					
				}
			}

			FileIO.writeToFile(new File(FileIO.OUTPUT), handler.toString());
			handler.stop();
			handler.print();
			handler.clear();
			common.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		System.exit(0);
	}
}

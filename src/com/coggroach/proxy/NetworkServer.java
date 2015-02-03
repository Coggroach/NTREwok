package com.coggroach.proxy;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import com.coggroach.common.FileIO;
import com.coggroach.common.NetworkInfo;
import com.coggroach.packet.Packet;
import com.coggroach.packet.PacketHandler;
import com.coggroach.socket.CommonSocket;
import com.coggroach.socket.SocketListener;

public class NetworkServer
{
	static ServerSocket server;
	static CommonSocket common = null;
	static StringBuilder output = new StringBuilder();
	
	public static void main(String args[])
	{		
		try
		{
			server = new ServerSocket(NetworkInfo.SOCKET);
			common = new CommonSocket(server.accept(), "Server0");
			common.setTimeOut();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		common.print();

		common.setSocketListener(new SocketListener()
		{
			@Override
			public boolean listen()
			{
				boolean run = true;
				while (run)
				{
					try
					{
						Packet p = common.receive();
						if (p != null)
						{
							if(p.isValid())
							{
								common.transmit(PacketHandler.getAckPacket(p.getAddress()));
								output.append(p.getString());
							}						
							//p.print();
						}
					}
					catch (IOException e)
					{
						System.err.println("Client Disconnected");
						run = false;						
					}
				}
				return false;
			}
		});
		
		common.run();		
		
		File f = new File("res/Output.txt");	
		try
		{
			FileIO.writeToFile(f, output.toString());
		}
		catch (IOException e)
		{		
			e.printStackTrace();
		}
	}
}

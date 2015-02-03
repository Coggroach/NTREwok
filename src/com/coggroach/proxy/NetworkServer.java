package com.coggroach.proxy;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import com.coggroach.common.FileIO;
import com.coggroach.common.NetworkInfo;
import com.coggroach.packet.Packet;
import com.coggroach.socket.CommonSocket;
import com.coggroach.socket.SocketListener;

public class NetworkServer
{
	static ServerSocket server;
	static CommonSocket common = null;
	static String output = "";
	
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
							common.transmit(p);
							output += p.getString();
							p.print();
						}
					}
					catch (IOException e)
					{
						e.printStackTrace();
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
			FileIO.writeToFile(f, output);
		}
		catch (IOException e)
		{		
			e.printStackTrace();
		}
	}
}

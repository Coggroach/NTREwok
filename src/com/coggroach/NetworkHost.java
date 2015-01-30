package com.coggroach;

import java.io.IOException;

import com.coggroach.proxy.SocketData;
import com.coggroach.proxy.Server;

public class NetworkHost
{
	public static void main(String args[])
	{
		Server server = null;
		try
		{
			server = new Server(SocketData.SOCKET);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		
		server.run();
	}
}

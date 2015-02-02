package com.coggroach;

import java.io.IOException;

import com.coggroach.common.NetworkInfo;
import com.coggroach.proxy.Server;

public class NetworkHost
{
	public static void main(String args[])
	{
		Server server = null;
		try
		{
			server = new Server(NetworkInfo.SOCKET);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		
		try
		{
			server.run();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
	}
}

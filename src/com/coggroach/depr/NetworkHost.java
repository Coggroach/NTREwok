package com.coggroach.depr;

import java.io.File;
import java.io.IOException;

import com.coggroach.common.FileIO;
import com.coggroach.common.NetworkInfo;

public class NetworkHost
{
	public static void main(String args[])
	{
		Server server = null;		
		
		try
		{
			server = new Server(NetworkInfo.SOCKET);
			server.run();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
		
		File f = new File("res/Output.txt");	
		try
		{
			FileIO.writeToFile(f, server.s);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

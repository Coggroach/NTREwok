package com.coggroach;

import java.io.File;
import java.io.IOException;

import com.coggroach.common.FileIO;
import com.coggroach.common.NetworkInfo;
import com.coggroach.packet.PacketException;
import com.coggroach.proxy.CommonSocket;

public class NetworkClient
{
	public static void main(String args[])
	{
		CommonSocket client = null;
		File file = new File("res/Input.txt");
		boolean isRunning = true;

		try
		{
			client = new CommonSocket(NetworkInfo.SOCKET);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}

		while (isRunning)
		{
			try
			{
				client.transmit(FileIO.readFromFile(file));
				System.out.println(client.receive());
				isRunning = false;
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
}

package com.coggroach.proxy;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

import com.coggroach.common.FileIO;
import com.coggroach.packet.Packet;

public class Server
{
	private ServerSocket server;
	private int index;
	private boolean isRunning;
	public String s = "";

	public Server(int i) throws IOException
	{
		this.server = new ServerSocket(i);
		this.index = 0;
		this.isRunning = true;
	}

	public void writeStartup()
	{
		System.out.println("Server Starting...");
	}

	public void run() throws IOException, SocketException
	{		
		this.writeStartup();		
		
		while (isRunning)
		{
			final CommonSocket w = new CommonSocket(server.accept(),
					String.valueOf(index));

			w.setTimeOut();
			w.setSocketListener(new SocketListener()
			{
				@Override
				public boolean listen()
				{
					boolean run = true;
					while (run)
					{
						try
						{
							Packet p = w.receive();
							if (p != null)
							{
								w.transmit(p);
								s += p.getString();
								p.print();
							}
						}
						catch (IOException e)
						{
							e.printStackTrace();
							run = false;
							isRunning = false;
						}
					}
					return false;
				}
			});

			System.out.println("Client connected: " + index);
			Thread t = new Thread(w);
			t.start();
			index++;			
		}
	}
}

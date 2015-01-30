package com.coggroach.proxy;

import java.io.IOException;
import java.net.ServerSocket;

public class Server
{
	private ServerSocket server;
	private int index;
	private boolean isRunning;

	public Server(int i) throws IOException
	{
		this.server = new ServerSocket(i);
		this.writeStartup();
		this.index = 0;
		this.isRunning = false;
	}
	
	public void writeStartup()
	{
		System.out.println("Server Starting...");		
	}

	public void run()
	{
		while (isRunning)
		{
			ClientWorker w;
			try
			{				
				w = new ClientWorker(server.accept(), index);
				System.out.println("Client connected: " + index);				
				Thread t = new Thread(w);
				t.start();
				index++;
			} catch (IOException e)
			{
				System.out.println("Accept failed");
				System.exit(-1);
			}
		}
	}
}

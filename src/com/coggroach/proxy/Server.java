package com.coggroach.proxy;

import java.io.IOException;
import java.net.ServerSocket;

import com.coggroach.packet.Packet;

public class Server
{
	private ServerSocket server;
	private int index;
	private boolean isRunning;

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

	public void run() throws IOException
	{
		this.writeStartup();
		while (isRunning)
		{
			final CommonSocket w = new CommonSocket(server.accept(), String.valueOf(index));

			w.setSocketListener(new SocketListener()
			{
				@Override
				public boolean listen()
				{
					try
					{
						Packet p = w.receive();
						w.transmit(p);
					}
					catch (IOException e)
					{
						e.printStackTrace();
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

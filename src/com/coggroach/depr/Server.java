package com.coggroach.depr;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

import com.coggroach.packet.Packet;
import com.coggroach.socket.CommonSocket;
import com.coggroach.socket.SocketListener;

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
								//System.out.println(p.isValid());
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

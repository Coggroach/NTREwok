package com.coggroach.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientWorker implements Runnable
{
	private Socket client;
	private int id;
	private boolean connected;

	public ClientWorker(Socket s, int id)
	{
		this.client = s;
		this.id = id;
		this.connected = true;
	}

	@Override
	public void run()
	{
		String line;
		BufferedReader in = null;
		PrintWriter out = null;
		try
		{
			in = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}

		while (connected)
		{
			try
			{
				line = in.readLine();
				System.out.println("Client:" + id + ": " + line);
				out.println(line);
				connected = false;

			} catch (IOException e)
			{
				System.out.println("Read failed");
				System.exit(-1);
			}
		}

	}

}

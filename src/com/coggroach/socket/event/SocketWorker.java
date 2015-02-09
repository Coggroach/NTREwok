package com.coggroach.socket.event;

import java.io.IOException;
import java.io.InputStream;

public class SocketWorker implements Runnable 
{
	private InputStream stream;
	private boolean isRunning;
	
	public SocketWorker(InputStream stream)
	{
		this.stream = stream;
		this.isRunning = true;
	}

	@Override
	public void run() 
	{	
		try 
		{
			while(this.isRunning)
			{
				if(this.stream.available() != 0)
				{
					
				}
				Thread.sleep(1);
			}
		} 
		catch (IOException | InterruptedException e) 
		{		
			e.printStackTrace();
		}
	}

}

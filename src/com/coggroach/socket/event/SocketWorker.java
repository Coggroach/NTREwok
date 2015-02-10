package com.coggroach.socket.event;


public class SocketWorker implements Runnable
{
	private boolean isRunning;
	private SocketListener listener;

	public SocketWorker()
	{
		this.isRunning = true;
		this.listener = null;
	}

	public void setSocketListener(SocketListener l)
	{
		this.listener = l;
	}

	@Override
	public void run()
	{
		try
		{
			while (this.isRunning)
			{
				if (this.listener != null)
				{
					this.listener.listen();
				}
				else if (this.listener == null)
				{
					throw new Exception("SocketListener not set...");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

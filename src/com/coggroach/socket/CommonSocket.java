package com.coggroach.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.coggroach.common.NetworkInfo;
import com.coggroach.packet.Packet;
import com.coggroach.packet.PacketException;

public class CommonSocket implements Runnable
{
	private Socket socket;
	private InputStream input;
	private OutputStream output;
	private String identity;
	private SocketListener listener;

	public CommonSocket(int socket) throws UnknownHostException, IOException,
			SocketException
	{
		this(new Socket(NetworkInfo.IPADDRESS, socket), null);
	}

	public CommonSocket(Socket s, String id) throws IOException
	{
		this.socket = s;
		this.init();
		this.identity = id;
		this.listener = null;
	}

	public void setTimeOut() throws SocketException
	{
		this.socket.setSoTimeout(NetworkInfo.TIMEOUT);
	}

	public void setIdentity(String s)
	{
		this.identity = s;
	}

	public String getIdentity()
	{
		return this.identity;
	}

	public void close() throws IOException
	{
		this.input.close();
		this.output.close();
		this.socket.close();
	}

	public void setSocketListener(SocketListener listener)
	{
		this.listener = listener;
	}

	private void init() throws IOException
	{
		this.input = socket.getInputStream();
		this.output = socket.getOutputStream();
	}

	public void transmit(Packet p) throws IOException
	{
		output.write(p.getBytes());
	}

	public void transmit(String s) throws IOException, PacketException
	{
		Packet p = new Packet();
		p.add(s);
		output.write(p.getBytes());
	}

	public Packet receive() throws IOException
	{
		Packet p = new Packet();
		input.read(p.getBytes());
		return p;
	}

	@Override
	public void run()
	{
		if (this.listener != null)
		{
			this.listener.listen();
		}
	}
}

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
import com.coggroach.socket.event.SocketListener;
import com.coggroach.socket.event.SocketWorker;

public class BaseSocket
{
	private Socket socket;
	private InputStream input;
	private OutputStream output;
	private String identity;
	private SocketWorker worker;

	public BaseSocket(int socket) throws UnknownHostException, IOException,
			SocketException
	{
		this(new Socket(NetworkInfo.IPADDRESS, socket), null);
	}

	public BaseSocket(Socket s, String id) throws IOException
	{
		this.socket = s;
		this.input = socket.getInputStream();
		this.output = socket.getOutputStream();
		this.identity = id;
		this.worker = new SocketWorker();
	}
	
	
	public boolean isConnected()
	{
		return socket.isClosed();
	}
	
	public SocketWorker getWorker()
	{
		return this.worker;
	}
	
	public boolean available() throws IOException
	{
		return this.input.available() != 0;
	}
	
	public void print()
	{
		System.out.println("Identity: " + this.identity);	
	}

	public void setTimeOut() throws SocketException
	{
		this.setTimeOut(NetworkInfo.TIMEOUT);
	}
	
	public void setTimeOut(int i) throws SocketException
	{
		this.socket.setSoTimeout(i);
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
		this.worker.setSocketListener(listener);
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
	
	public void run()
	{
		Thread t = new Thread(this.worker);
		t.start();
	}
}

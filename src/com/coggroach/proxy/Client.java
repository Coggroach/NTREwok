package com.coggroach.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.coggroach.proxy.packet.Packet;

public class Client
{
	private Socket client;
	private BufferedReader input;
	private OutputStream output;
	private List<Byte> packetIds;
	private byte currentByte;

	public Client(int socket) throws UnknownHostException, IOException
	{
		this.client = new Socket(SocketData.IPADDRESS, socket);
		this.packetIds =  new ArrayList<Byte>();
		this.currentByte = 0;
		this.init();
		this.writeStartup();
	}
	
	private byte getNextByteId()
	{
		this.currentByte++;
		return this.currentByte;
	}

	public void writeStartup()
	{
		System.out.println("Client Starting...");
	}

	public void init() throws IOException
	{
		this.input = new BufferedReader(new InputStreamReader(
				client.getInputStream()));
		this.output = client.getOutputStream();
	}
	
	public void transmit(Packet p) throws IOException
	{
		output.write(p.getBytes());
	}

	public void transmit(String s) throws IOException
	{
		Packet p = new Packet();
		p.addToPayload(s.getBytes());		
		//output.println(p.getStringToSend(getNextByteId()));
		output.write(p.getBytes());
	}

	public String receive() throws IOException
	{
		return input.readLine();
	}
}

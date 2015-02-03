package com.coggroach.proxy;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

import com.coggroach.common.FileIO;
import com.coggroach.common.Gremlin;
import com.coggroach.common.NetworkInfo;
import com.coggroach.packet.Packet;
import com.coggroach.packet.PacketHandler;
import com.coggroach.socket.CommonSocket;
import com.coggroach.socket.SocketListener;

public class NetworkServer
{
	static ServerSocket server;
	static CommonSocket common = null;
	static StringBuilder output = new StringBuilder();
	static byte lastAddress = -1;
	
	private static boolean hasSkippedAddress(byte b1, byte b2)
	{
		return b1 + 1 < b2;
	}
	
	public static void main(String args[])
	{		
		try
		{
			server = new ServerSocket(NetworkInfo.SOCKET);
			common = new CommonSocket(server.accept(), "Server0");
			common.setTimeOut();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		common.print();

		common.setSocketListener(new SocketListener()
		{
			@Override
			public boolean listen()
			{
				boolean run = true;
				while (run)
				{
					try
					{
						Packet p = Gremlin.receive(common.receive());
						if (p != null)
						{
							if(p.isValid())
							{
								if(hasSkippedAddress(lastAddress, p.getAddress()))
								{
									common.transmit(PacketHandler.getNakPacket((byte) (lastAddress + 1)));
									System.out.println("NAK: " + (lastAddress + 1));
								}
								else
								{
									common.transmit(PacketHandler.getAckPacket(p.getAddress()));
									output.append(p.getString());
									lastAddress = p.getAddress();
									System.out.println("ACK: " + lastAddress);
								}								
							}						
							//p.print();
						}
						else
						{
							common.transmit(PacketHandler.getNakPacket((byte) (lastAddress + 1)));
							System.out.println("NAK: " + (lastAddress + 1));
						}
					}
					catch (IOException e)
					{
						System.err.println("Client Disconnected");
						run = false;						
					}
				}
				return false;
			}
		});
		
		common.run();		
		
		File f = new File("res/Output.txt");	
		try
		{
			FileIO.writeToFile(f, output.toString());
		}
		catch (IOException e)
		{		
			e.printStackTrace();
		}
	}
}

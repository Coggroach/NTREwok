package com.coggroach;

import java.io.File;
import java.io.IOException;

import com.coggroach.common.FileIO;

public class NetworkFiler
{
	public static void main(String args[])
	{
		File file = null;
		try
		{
			file = FileIO.createFile("res/Input.txt");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(file.length());
		try
		{
			FileIO.generateRandomContent(file, 1024);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(file.length());
	}
}

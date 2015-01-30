package com.coggroach.common;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class FileIO
{
	public static File createFile(String name) throws IOException
	{
		File f = new File(name);
		f.createNewFile();		
		
		return f;
	}
	
	public static void writeToFile(File file, String s) throws IOException
	{
		FileWriter writer = new FileWriter(file);
		writer.write(s);
		writer.flush();
		writer.close();		
	}
	
	public static void generateRandomContent(File file, int length) throws IOException
	{
		String s = "";
		Random rand = new Random();
		for(int i = 0; i < length; i++)
		{
			s += rand.nextInt(2);
			
		}
		FileIO.writeToFile(file, s);
	}
	
	public static String readFromFile(File file) throws IOException
	{
		FileReader reader = new FileReader(file);
		
		char[] charArray = new char[1024];		
		reader.read(charArray);
		reader.close();
		
		return String.valueOf(charArray);
	}
	
	
}

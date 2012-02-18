package com.costeaalex.fileduplicatefinder.searcher;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileElement implements Comparable<FileElement>
	{
	
	private File file;
	
	public FileElement(File file)
		{
		this.file=file;
		}
	
	public String getFileName()
		{
		return file.getAbsolutePath();
		}
	
	public boolean delete()
		{
		return file.delete();
		}
	
	public long getSize()
		{
		return file.length();
		}
	
	public void open()
		{
		try
			{
			Desktop.getDesktop().open(file);
			}
		catch (IOException e)
			{
			try
				{
				FileWriter errorLog=new FileWriter("Error.log", true);
				BufferedWriter out = new BufferedWriter(errorLog);
				out.write(e.getMessage());	
				out.flush();
				out.close();
				}
			catch (IOException e1)
				{
				e1.printStackTrace();
				}
			e.printStackTrace();
			}
		}
	
	public void remove()
		{
		file.delete();
		}
	
	@Override
	public boolean equals(Object arg0)
		{
		if(arg0 instanceof FileElement)
			return ((FileElement) arg0).getFileName().equals(this.getFileName());
		throw new IllegalArgumentException();
		}

	public int compareTo(FileElement arg0)
		{
		if(arg0 instanceof FileElement)
			return file.getName().compareTo(arg0.getFileName());
		throw new IllegalArgumentException();
		}
	
	}

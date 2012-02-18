package com.costeaalex.fileduplicatefinder.searcher;

import java.awt.Desktop;
import java.io.File;
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
		return file.getAbsolutePath()+"-"+file.length();
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

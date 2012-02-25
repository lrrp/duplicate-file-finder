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
	
	public String getAbsoluteFileName()
		{
		return file.getAbsolutePath();
		}
	
	public String getFileName()
		{
		return file.getName();
		}
	
	public boolean delete()
		{
		return file.delete();
		}
	
	public long getSize()
		{
		return file.length();
		}
	
	public void open() throws IOException
		{
		Desktop.getDesktop().open(file);
		}
	
	public void remove()
		{
		file.delete();
		}
	
	@Override
	public boolean equals(Object arg0)
		{
		if(arg0 instanceof FileElement)
			return this.getFileName().equals(((FileElement) arg0).getFileName());
		throw new IllegalArgumentException();
		}
	
	@Override
	public String toString()
		{
		return this.getAbsoluteFileName();
		}

	public int compareTo(FileElement arg0)
		{
		if(arg0 instanceof FileElement)
			return this.getFileName().compareTo(arg0.getFileName());
		throw new IllegalArgumentException();
		}
	
	}

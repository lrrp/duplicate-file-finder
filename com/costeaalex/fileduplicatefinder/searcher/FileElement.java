package com.costeaalex.fileduplicatefinder.searcher;

import java.io.File;

public class FileElement implements Comparable<FileElement>
	{
	
	private String fileName;
	private long size;
	private File file;
	
	public FileElement()
		{
		this.fileName="";
		this.size=0;
		}
	
	public FileElement(String filename, long size, File file)
		{
		this.fileName=filename;
		this.size=size;
		this.file=file;
		}
	
	public String getFileName()
		{
		return fileName;
		}
	
	public boolean delete()
		{
		return file.delete();
		}
	
	public void setFileName(String fileName)
		{
		this.fileName = fileName;
		}
	
	public long getSize()
		{
		return size;
		}
	
	public void setSize(long size)
		{
		this.size = size;
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
			return this.fileName.compareTo(arg0.fileName);
		return 0;
		}
	
	}

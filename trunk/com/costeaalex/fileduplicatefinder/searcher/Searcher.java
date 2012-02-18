package com.costeaalex.fileduplicatefinder.searcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Searcher
	{
	
	private ArrayList<FileElement> fileList=new ArrayList<FileElement>();
	
	public void search(File directory)
		{
		String[] files = directory.list();
		File tmp;
	
		for(int i=0; i<files.length; i++)
			{
			tmp=new File(directory.getAbsolutePath()+"\\"+files[i]);
			if(tmp.isDirectory())
				{
				search(tmp);
				}
			else
				{
				File f=new File(directory.getAbsolutePath()+"\\"+files[i]);
				fileList.add(new FileElement(f));
				}
			}
		}
	
	public ArrayList<FileElement> sort()
		{
		Collections.sort(fileList);
		return fileList;
		}
	
	public void list()
		{
		Iterator<FileElement> i=fileList.iterator();
		while(i.hasNext())
			{
			FileElement f=i.next();
			System.out.println(f.getFileName() + " - " + f.getSize());
			}
		}
	}

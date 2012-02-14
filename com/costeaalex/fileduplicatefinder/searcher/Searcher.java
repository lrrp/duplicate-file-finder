package com.costeaalex.fileduplicatefinder.searcher;

import java.io.File;

public class Searcher
	{
	
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
				if(files[i].lastIndexOf(".")!=-1)
					{
					}
				}
			}
		}
	
	}

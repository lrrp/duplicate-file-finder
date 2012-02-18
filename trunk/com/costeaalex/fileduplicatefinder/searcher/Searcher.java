package com.costeaalex.fileduplicatefinder.searcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Observable;

public class Searcher extends Observable implements Runnable
	{
	
	private ArrayList<FileElement> fileList=new ArrayList<FileElement>();
	File directory;
	
	public Searcher(File directory)
		{
		this.directory=directory;
		}
	
	public ArrayList<FileElement> getFileList()
		{
		return fileList;
		}
	
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
				FileElement fE=new FileElement(f);
				fileList.add(fE);
				setChanged();
				notifyObservers(fE);
				}
			}
		}
	
	public ArrayList<FileElement> sort()
		{
		Collections.sort(fileList);
		setChanged();
		notifyObservers(fileList);
		return fileList;
		}
	
	public ArrayList<FileElement> clean()
		{
		
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

	public void run()
		{
		search(directory);
		sort();
		clean();
		setChanged();
		notifyObservers("Done");
		}
	}

package com.costeaalex.fileduplicatefinder.searcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Observable;
import java.util.Vector;

public class Searcher extends Observable implements Runnable
	{
	
	private ArrayList<FileElement> fileList=new ArrayList<FileElement>();
	private File directory;
	private int filter;
	
	public Searcher(File directory, int filter)
		{
		this.directory=directory;
		this.filter=filter;
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
		if(filter==0)
			{
			Collections.sort(fileList);
			setChanged();
			notifyObservers(fileList);
			}
		if(filter==1)
			{
			Collections.sort(fileList, new SizeComparator());
			setChanged();
			notifyObservers(fileList);
			}
		return fileList;
		}
	
	public ArrayList<FileElement> clean()
		{
		int i;
		int dupcount=0;
		Vector<FileElement> toRemove=new Vector<FileElement>();
		FileElement dup=fileList.get(0);
		for(i=1; i<fileList.size(); i++)
			{
			if(fileList.get(i).equals(dup)) // TODO add filter condition
				{
				dup=fileList.get(i);
				dupcount++;
				}
			else
				{
				if(dupcount>0)
					{
					dup=fileList.get(i);
					dupcount=0;
					}
				else
					if(dupcount==0)
						{
						dup=fileList.get(i);
						toRemove.add(fileList.get(i-1));
						}
				}
			}
		
		if(dupcount==0)
			toRemove.add(fileList.get(i-1));
		
		for(i=0; i<toRemove.size(); i++)
			fileList.remove(toRemove.get(i));
		
		return fileList;
		}
	
	public void list()
		{
		Iterator<FileElement> i=fileList.iterator();
		while(i.hasNext())
			{
			FileElement f=i.next();
			System.out.println(f.getAbsoluteFileName() + " - " + f.getSize());
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

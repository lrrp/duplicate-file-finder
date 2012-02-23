package com.costeaalex.fileduplicatefinder.searcher;

import java.util.Comparator;

public class SizeComparator implements Comparator<FileElement>
	{

	public int compare(FileElement arg0, FileElement arg1)
		{
		if(arg0.getSize()<arg1.getSize())
			return -1;
		if(arg0.getSize()>arg1.getSize())
			return 1;
		return 0;
		}

	}

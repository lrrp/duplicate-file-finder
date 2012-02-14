package com.costeaalex.fileduplicatefinder.app;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import com.costeaalex.fileduplicatefinder.searcher.FileElement;
import com.costeaalex.fileduplicatefinder.searcher.Searcher;

public class FileDuplicateFinder
	{
	
	public static void main(String [] args)
		{
		Searcher s=new Searcher();
		s.search(new File("."));
		s.list();
		
		//System.out.println("aa");
		/*FileElement a= new FileElement("A",1,null);
		FileElement b= new FileElement("a",1,null);
		FileElement c= new FileElement("Aaa",1,null);
		FileElement d= new FileElement("A1",1,null);
		
		ArrayList<FileElement> x=new ArrayList<FileElement>();
		x.add(a);
		x.add(b);
		x.add(c);
		x.add(d);
		
		System.out.println(   ((FileElement) x.get(0)).getFileName() );
		System.out.println(   ((FileElement) x.get(1)).getFileName() );
		System.out.println(   ((FileElement) x.get(2)).getFileName() );
		System.out.println(   ((FileElement) x.get(3)).getFileName() );
	
		Collections.sort(x);
		
		System.out.println(   ((FileElement) x.get(0)).getFileName() );
		System.out.println(   ((FileElement) x.get(1)).getFileName() );
		System.out.println(   ((FileElement) x.get(2)).getFileName() );
		System.out.println(   ((FileElement) x.get(3)).getFileName() );
		 */
		}
	
	}

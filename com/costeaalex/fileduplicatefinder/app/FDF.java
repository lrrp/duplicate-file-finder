package com.costeaalex.fileduplicatefinder.app;

import java.io.File;

import com.costeaalex.fileduplicatefinder.gui.FDFGui;

public class FDF
	{
	
	public static void main(String [] args)
		{
		FDFGui fG=new FDFGui(new File(".").getAbsolutePath());
		fG.buildGui();
		}
	
	}

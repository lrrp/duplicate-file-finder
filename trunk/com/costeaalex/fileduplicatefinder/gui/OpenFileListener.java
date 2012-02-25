package com.costeaalex.fileduplicatefinder.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPopupMenu;
import javax.swing.Popup;
import javax.swing.SwingUtilities;

import com.costeaalex.fileduplicatefinder.searcher.FileElement;

public class OpenFileListener extends MouseAdapter
	{
	
	private ArrayList<FileElement> list;
	private Popup popUpWindowError;
	private JPopupMenu jPM;
	
	public OpenFileListener(ArrayList<FileElement> list, Popup popUpWindowError, JPopupMenu jPM)
		{
		this.list=list;
		this.popUpWindowError=popUpWindowError;
		this.jPM=jPM;
		}
	
	public void mouseClicked(MouseEvent mouseEvent) 
		{
		JTableCustom tableCustom = (JTableCustom) mouseEvent.getSource();
		if (mouseEvent.getClickCount() == 2) 
			{
			int index = tableCustom.rowAtPoint(mouseEvent.getPoint());
			int column = tableCustom.columnAtPoint(mouseEvent.getPoint()); 
			if (index >= 0 && column!=2) 
				{
				FileElement f = list.get(index);
				try
					{
					f.open();
					}
				catch (IOException e)
					{
					try
						{
						FileWriter errorLog=new FileWriter("Error.log", true);
						BufferedWriter out = new BufferedWriter(errorLog);
						out.write(e.getMessage());	
						out.flush();
						out.close();
						popUpWindowError.show();
						}
					catch (IOException e1)
						{
						e1.printStackTrace();
						}
					e.printStackTrace();
					}
				}
			}
		
		if (SwingUtilities.isRightMouseButton(mouseEvent))
            {
            jPM.show(tableCustom, mouseEvent.getX(), mouseEvent.getY());
            }
		}
	}

package com.costeaalex.fileduplicatefinder.gui;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.costeaalex.fileduplicatefinder.searcher.FileElement;

public class JTableCustom extends JTable
	{
	
	/**
	 * 
	 */
	private DefaultTableModel tableModel;
	
	private static final long	serialVersionUID	= 5070732538724833646L;
	
	public JTableCustom(TableModel dm)
		{
		super(dm);
		tableModel=(DefaultTableModel) dm;
		}

	@Override
	public boolean isCellEditable(int row, int column)
		{
		if(column==2)
			return true;
		return false;
		}
	
	//Updates the table
	public void updateTable(ArrayList<FileElement> fileList)
		{
		final ArrayList<FileElement> list=fileList;
		SwingUtilities.invokeLater(new Runnable()
			{
			public void run()
				{
				clearTable();
				for(int i=0; i<list.size(); i++)
					tableModel.insertRow(i, new Object[]{list.get(i).getAbsoluteFileName(), list.get(i).getSize(), new Boolean(false)});
				}
			});
		}
	
	//Clears the master table
	public boolean clearTable()
		{
		while(tableModel.getRowCount()>0)
			tableModel.removeRow(0);
		return true;
		}
	
	//Deletes the selected rows
	public boolean deleteSelected(int [] toDelete, ArrayList<FileElement> list)
		{
		Vector<FileElement> toRemove=new Vector<FileElement>();
		for(int i=0; i<toDelete.length; i++)
			toRemove.add(list.get(toDelete[i]));
		for(int i=0; i<toRemove.size(); i++)
			{
			//TODO
			//((FileElement) toRemove.get(i)).delete();
			tableModel.removeRow(toDelete[i]-i);
			list.remove(toRemove.get(i));
			}
		//updateTable(list);
		return true;
		}
	
	//Deletes the marked rows
	public boolean deleteMarked(ArrayList<FileElement> list)
		{
		Vector<FileElement> toRemove=new Vector<FileElement>();
		Vector<Integer> toDelete=new Vector<Integer>();
		for(int i=0; i<this.getRowCount(); i++)
			if((Boolean) this.getValueAt(i, 2) == true)
				{
				toRemove.add(list.get(i));
				toDelete.add(i);
				}
		
		for(int i=0; i<toRemove.size(); i++)
			list.remove(toRemove.get(i));
		
		for(int i=0; i<toDelete.size(); i++)
			tableModel.removeRow(toDelete.get(i)-i);
		//updateTable(list);
		return true;
		}

	//Inserts a row
	public void insertRow(int val1, Object [] val2)
		{
		tableModel.insertRow(val1, val2);
		}
	}

package com.costeaalex.fileduplicatefinder.gui;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class JTableCustom extends JTable
	{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5070732538724833646L;
	
	public JTableCustom(TableModel dm)
		{
		super(dm);
		}

	@Override
	public boolean isCellEditable(int row, int column)
		{
		// TODO Auto-generated method stub
		return false;
		}

	}

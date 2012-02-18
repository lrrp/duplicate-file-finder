package com.costeaalex.fileduplicatefinder.gui;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class CheckBoxCellRenderer extends JCheckBox implements TableCellRenderer
	{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1005579148806474434L;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
		if(value!=null && value instanceof Boolean)
			this.setSelected(((Boolean) value).booleanValue());
		return this;
		}

	}

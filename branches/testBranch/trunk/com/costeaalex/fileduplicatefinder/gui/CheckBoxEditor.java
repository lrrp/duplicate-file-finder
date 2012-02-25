package com.costeaalex.fileduplicatefinder.gui;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

public class CheckBoxEditor extends JCheckBox implements TableCellEditor, ItemListener
	{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2599372193111430306L;
	Vector<CellEditorListener> editorListeners;
	
	public CheckBoxEditor()
		{
		addItemListener(this);
		editorListeners = new Vector<CellEditorListener>();
		}
	
	public void addCellEditorListener(CellEditorListener arg0)
		{
		editorListeners.add(arg0);
		}

	public void cancelCellEditing()
		{
		}

	public Object getCellEditorValue()
		{
		return this.isSelected();
		}

	public boolean isCellEditable(EventObject arg0)
		{
		return true;
		}

	public void removeCellEditorListener(CellEditorListener arg0)
		{
		editorListeners.remove(arg0);
		updateSelected((JTable) arg0);
		}

	public boolean shouldSelectCell(EventObject arg0)
		{
		return false;
		}

	public boolean stopCellEditing()
		{
		return true;
		}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
		{
		this.setSelected((Boolean)value);
		if(!(Boolean)value)
			table.getSelectionModel().addSelectionInterval(row, row);
		return this;
		}
	
	public void updateSelected(JTable table)
		{
		boolean b=false;
		for(int i=0; i<table.getRowCount(); i++)
			if((Boolean) table.getValueAt(i, 2))
				b=true;
		
		if(b)
			for(int i=0; i<table.getRowCount(); i++)
				if((Boolean) table.getValueAt(i, 2))
					table.getSelectionModel().addSelectionInterval(i, i);
				else
					table.getSelectionModel().removeSelectionInterval(i, i);
		}

	public void itemStateChanged(ItemEvent e)
		{
		  for(int i=0; i<editorListeners.size(); ++i)
		  {
		   ((CellEditorListener) editorListeners.elementAt(i)).editingStopped(new ChangeEvent(this));
		  }
		}
	}

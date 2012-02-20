package com.costeaalex.fileduplicatefinder.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


import com.costeaalex.fileduplicatefinder.searcher.FileElement;
import com.costeaalex.fileduplicatefinder.searcher.Searcher;

public class FDFGui implements ActionListener,Observer,ComponentListener
	{
	static int selectedItem=-1;
	
	private JFrame jF;
	private JButton jBAction;
	private JButton jBDeleteSelected;
	private JButton jBDeleteMarked;
	private JButton jBE;
	private JScrollPane jSPResults;
	private Container container;
	private Searcher s;
	private Thread sT;
	private int index;
	private JPopupMenu jPM;
	private JMenuItem menuItemDeleteSelected;
	private JMenuItem menuItemDeleteMarked;
	private String searchDirectory;
	private ArrayList<FileElement> list;
	private JTableCustom jT;
	private DefaultTableModel tableModel;
	private String [] columns = {"File", "Size", "Mark" };
	private Popup popUpWindowError;
	
	public FDFGui(String searchDirectory)
		{
		index=0;
		this.searchDirectory=searchDirectory;
		}
	
	public void buildGui()
		{		
		jF=new JFrame("Find Duplicate Files");
		jF.setSize(800,600);
		jF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jF.addComponentListener(this);
		
		JPanel jPE=new JPanel();
		jPE.setLayout(new BorderLayout());
		jPE.setBackground(Color.white);
		Border blackline = BorderFactory.createLineBorder(Color.blue, 3);
		jPE.setBorder(blackline);
		
		JPanel jPE1=new JPanel();
		jPE1.setBackground(Color.white);
		jPE1.setLayout(new BoxLayout(jPE1, BoxLayout.PAGE_AXIS));
		jPE1.add(new JLabel("No default program to open this type of file!"));
		jPE1.add(new JLabel("Check Error log for more info!"));
		jBE=new JButton("Ok");
		jBE.addActionListener(this);
		jPE1.add(jBE);
		
		jPE.add(Box.createRigidArea(new Dimension(20, 20)) , BorderLayout.PAGE_START);
		jPE.add(Box.createRigidArea(new Dimension(20, 20)) , BorderLayout.WEST);
		jPE.add(jPE1, BorderLayout.CENTER);
		jPE.add(Box.createRigidArea(new Dimension(20, 20)) , BorderLayout.EAST);
		jPE.add(Box.createRigidArea(new Dimension(20, 20)) , BorderLayout.PAGE_END);
		
		PopupFactory factory = PopupFactory.getSharedInstance();
		popUpWindowError = factory.getPopup(jF, jPE, 270, 270);
		
		jBAction=new JButton("Find");
		jBAction.addActionListener(this);
		
		jBDeleteSelected=new JButton("Delete selected");
		jBDeleteSelected.addActionListener(this);
		jBDeleteSelected.setEnabled(false);
		
		jBDeleteMarked=new JButton("Delete marked");
		jBDeleteMarked.addActionListener(this);
		jBDeleteMarked.setEnabled(false);
		
		tableModel = new DefaultTableModel(null, columns);
			/*{
			public Class getColumnClass(int c) 
				{
		        return getValueAt(0, c).getClass();
		        }
			};*/
		jT=new JTableCustom(tableModel);
		jT.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jT.getColumnModel().getColumn(0).setPreferredWidth(562);
		jT.getColumnModel().getColumn(1).setPreferredWidth(150);
		jT.getColumnModel().getColumn(2).setPreferredWidth(50);
		JTableHeader header = jT.getTableHeader();
		header.setBackground(Color.white);
		jT.getColumn("Mark").setCellRenderer(new CheckBoxCellRenderer());
		jT.getColumn("Mark").setCellEditor(new CheckBoxEditor());
		jT.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		jSPResults=new JScrollPane(jT);
		jSPResults.setPreferredSize(new Dimension(780, 530));
		
		jPM=new JPopupMenu();
		menuItemDeleteSelected = new JMenuItem("Delete Selected");
		menuItemDeleteMarked = new JMenuItem("Delete Marked");
		menuItemDeleteSelected.addActionListener(this);
		menuItemDeleteMarked.addActionListener(this);
		jPM.add(menuItemDeleteSelected);
		jPM.add(menuItemDeleteMarked);
		
		container=jF.getContentPane();
		container.setLayout(new BorderLayout());
		
		JPanel jp1=new JPanel();
		jp1.setLayout(new FlowLayout());
		
		jp1.add(jBAction);
		jp1.add(jBDeleteSelected);
		jp1.add(jBDeleteMarked);
	
		container.add(jp1, BorderLayout.NORTH);
		container.add(jSPResults, BorderLayout.CENTER);
		
		jF.setVisible(true);
		}
	
	//When the JFrame resizes make the components inside resize
	public void componentResized(ComponentEvent evt)
		{
		Component c = (Component)evt.getSource();
	    final Dimension newSize = c.getSize();
	    SwingUtilities.invokeLater(new Runnable()
			{
			public void run()
				{
			    jSPResults.setPreferredSize(new Dimension(newSize.width-20, newSize.height-70));
			    jT.getColumnModel().getColumn(0).setPreferredWidth(71*newSize.width/100);
				jT.getColumnModel().getColumn(1).setPreferredWidth(19*newSize.width/100);
				jT.getColumnModel().getColumn(2).setPreferredWidth(6*newSize.width/100);
				}
			});
		}
	
	public void componentHidden(ComponentEvent e)
		{
		}

	public void componentMoved(ComponentEvent e)
		{
		}

	public void componentShown(ComponentEvent e)
		{
		}

	//Action listener may put it in new file
	public void actionPerformed(ActionEvent e)
		{
		if(e.getSource() == jBAction)
			{
			s=new Searcher(new File(searchDirectory));
			sT=new Thread(s);
			s.addObserver(this);
			jBAction.setEnabled(false);
			sT.start();
			}
		if(e.getSource() == jBDeleteSelected)
			{
			deleteSelected(jT.getSelectedRows());
			}
		if(e.getSource() == jBDeleteMarked)
			{
			deleteMarked();
			}
		if(e.getSource() == menuItemDeleteSelected)
			{
			deleteSelected(jT.getSelectedRows());
			}
		if(e.getSource() == menuItemDeleteMarked)
			{
			deleteMarked();
			}
		if(e.getSource() == jBE)
			{
			popUpWindowError.hide();
			}
		}
	
	//Updates the table
	public void updateTable()
		{
		SwingUtilities.invokeLater(new Runnable()
			{
			public void run()
				{
				clearTable(tableModel);
				for(int i=0; i<list.size(); i++)
					tableModel.insertRow(i, new Object[]{list.get(i).getAbsoluteFileName(), list.get(i).getSize(), new Boolean(false)});
				}
			});
		}
	
	//Clears the master table
	public boolean clearTable(DefaultTableModel tableM)
		{
		while(tableM.getRowCount()>0)
			tableM.removeRow(0);
		return true;
		}
	
	//Deletes the selected rows
	public boolean deleteSelected(int [] toDelete)
		{
		Vector<FileElement> toRemove=new Vector<FileElement>();
		for(int i=0; i<toDelete.length; i++)
			toRemove.add(list.get(toDelete[i]));
		for(int i=0; i<toRemove.size(); i++)
			{
			//((FileElement) toRemove.get(i)).delete();
			list.remove(toRemove.get(i));
			}
		updateTable();
		return true;
		}
	
	//Deletes the marked rows
	public boolean deleteMarked()
		{
		Vector<FileElement> toRemove=new Vector<FileElement>();
		for(int i=0; i<jT.getRowCount(); i++)
			if((Boolean) jT.getValueAt(i, 2) == true)
				toRemove.add(list.get(i));
		for(int i=0; i<toRemove.size(); i++)
			{
			//((FileElement) toRemove.get(i)).delete();
			list.remove(toRemove.get(i));
			}
		updateTable();
		return true;
		}

	/*
	 * Gets updated with information from the searcher;
	 * 
	 */
	public void update(Observable o, Object arg)
		{
		if(arg instanceof String)//when searcher is done
			{
			if(((String) arg).equals("Done"))
				{
				final ArrayList<FileElement> list;
				
				if(o instanceof Searcher)
					list= ((Searcher) o).getFileList();
				else
					list=null;
				
				this.list=list;
				
				SwingUtilities.invokeLater(new Runnable()
					{
					public void run()
						{
						while (tableModel.getRowCount()>0)
							tableModel.removeRow(0);
						for(int i=0; i<list.size(); i++)
							tableModel.insertRow(i, new Object[]{list.get(i).getAbsoluteFileName(), list.get(i).getSize(), new Boolean(false)});
						}
					});
				
				MouseListener mouseListener = new MouseAdapter() 
					{
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
					};
			    jT.addMouseListener(mouseListener);
				jBDeleteSelected.setEnabled(true);
				jBDeleteMarked.setEnabled(true);
				}
			}
		
		if(arg instanceof FileElement)//when searcher finds a new file
			{
			final String str= ((FileElement) arg).getAbsoluteFileName();
			
			SwingUtilities.invokeLater(new Runnable()
				{	
				public void run()
					{
					tableModel.insertRow(index, new Object[]{str, "", new Boolean(false)});
					index++;
					}
				});
			}
		
		if(arg instanceof ArrayList)//when searcher is sorted
			{
			final ArrayList<FileElement> list= ((ArrayList<FileElement>) arg);
			
			SwingUtilities.invokeLater(new Runnable()
				{
				public void run()
					{
					while (tableModel.getRowCount()>0)
						tableModel.removeRow(0);
					for(int i=0; i<list.size(); i++)
						tableModel.insertRow(i, new Object[]{list.get(i).getAbsoluteFileName(), list.get(i).getSize(), new Boolean(false)});
					}
				});
			}
		}
	}
package com.costeaalex.fileduplicatefinder.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


import com.costeaalex.fileduplicatefinder.searcher.FileElement;
import com.costeaalex.fileduplicatefinder.searcher.Searcher;

public class FDFGui implements ActionListener,Observer
	{
	static int selectedItem=-1;
	
	private JFrame jF;
	private JButton jBAction;
	private JScrollPane jSPResults;
	private Container container;
	private Searcher s;
	private Thread sT;
	private int index;
	private JPopupMenu jPM;
	private JMenuItem menuItemDelete;
	private String searchDirectory;
	private ArrayList<FileElement> list;
	private JTableCustom jT;
	private DefaultTableModel tableModel;
	private String [] columns = {"File", "Size" };
	
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
		
		jBAction=new JButton("Find");
		jBAction.addActionListener(this);
		
		tableModel = new DefaultTableModel(null, columns);
		jT=new JTableCustom(tableModel);
		jT.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jT.getColumnModel().getColumn(0).setPreferredWidth(612);
		jT.getColumnModel().getColumn(1).setPreferredWidth(150);
		JTableHeader header = jT.getTableHeader();
		header.setBackground(Color.white);
		jSPResults=new JScrollPane(jT);
		jSPResults.setPreferredSize(new Dimension(780, 530));
		
		/*model = new DefaultListModel();
		jLResults=new JList(model);
		jLResults.setVisibleRowCount(35);
		jLResults.setFixedCellWidth(570);
		jSPResults=new JScrollPane(jLResults);
		*/
		
		jPM=new JPopupMenu();
		menuItemDelete = new JMenuItem("Delete");
		menuItemDelete.addActionListener(this);
		jPM.add(menuItemDelete);
		
		container=jF.getContentPane();
		container.setLayout(new FlowLayout());
		
		container.add(jBAction);
		container.add(jSPResults);
		
		jF.setVisible(true);
		}

	public void actionPerformed(ActionEvent e)
		{
		if(e.getSource() instanceof JButton)
			{
			s=new Searcher(new File(searchDirectory));
			sT=new Thread(s);
			s.addObserver(this);
			jBAction.setEnabled(false);
			sT.start();
			}
		if(e.getSource() == menuItemDelete)
			{
			tableModel.removeRow(selectedItem);
			//list.get(selectedItem).remove();
			//System.out.println(list.get(selectedItem).getFileName());
			}
		}

	public void update(Observable o, Object arg)
		{
		if(arg instanceof String)
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
							tableModel.insertRow(i, new Object[]{list.get(i).getFileName(), list.get(i).getSize()});
						}
					});
				
				MouseListener mouseListener = new MouseAdapter() 
					{
					public void mouseClicked(MouseEvent mouseEvent) 
						{
						JTableCustom theList = (JTableCustom) mouseEvent.getSource();
						if (mouseEvent.getClickCount() == 2) 
							{
							int index = theList.rowAtPoint(mouseEvent.getPoint());
							if (index >= 0) 
								{
								FileElement f = list.get(index);
								f.open();
								}
							}
						
						if (SwingUtilities.isRightMouseButton(mouseEvent))
				            {
				            selectedItem=theList.rowAtPoint(mouseEvent.getPoint());
				            theList.getSelectionModel().setSelectionInterval(selectedItem, selectedItem);
				            jPM.show(theList, mouseEvent.getX(), mouseEvent.getY());
				            }
						}
					};
			    jT.addMouseListener(mouseListener);
			    
				}
			}
		
		if(arg instanceof FileElement)
			{
			final String str= ((FileElement) arg).getFileName();
			
			SwingUtilities.invokeLater(new Runnable()
				{	
				public void run()
					{
					tableModel.insertRow(index, new Object[]{str, ""});
					index++;
					}
				});
			}
		
		if(arg instanceof ArrayList)
			{
			final ArrayList<FileElement> list= ((ArrayList<FileElement>) arg);
			
			SwingUtilities.invokeLater(new Runnable()
				{
				public void run()
					{
					while (tableModel.getRowCount()>0)
						tableModel.removeRow(0);
					for(int i=0; i<list.size(); i++)
						tableModel.insertRow(i, new Object[]{list.get(i).getFileName(), list.get(i).getSize()});
					}
				});
			}
		}
	}
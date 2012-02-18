package com.costeaalex.fileduplicatefinder.gui;

import java.awt.Container;
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

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
//import javax.swing.SwingUtilities;

import com.costeaalex.fileduplicatefinder.searcher.FileElement;
import com.costeaalex.fileduplicatefinder.searcher.Searcher;

public class FDFGui implements ActionListener,Observer
	{
	static int selectedItem=-1;
	
	private JFrame jF;
	private JButton jBAction;
	private JScrollPane jSPResults;
	private JList jLResults;
	private Container container;
	private Searcher s;
	private Thread sT;
	private DefaultListModel model;
	private int index;
	private JPopupMenu jPM;
	private JMenuItem menuItemDelete;
	private String searchDirectory;
	private ArrayList<FileElement> list;
	
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
		
		model = new DefaultListModel();
		jLResults=new JList(model);
		jLResults.setVisibleRowCount(35);
		jLResults.setFixedCellWidth(570);
		jSPResults=new JScrollPane(jLResults);
		
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
			model.remove(selectedItem);
			//list.get(selectedItem).remove();
			System.out.println(list.get(selectedItem).getFileName());
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
						model.removeAllElements();
						for(int i=0; i<list.size(); i++)
							model.add(i, list.get(i).getFileName());
						}
					});
				
				MouseListener mouseListener = new MouseAdapter() 
					{
					public void mouseClicked(MouseEvent mouseEvent) 
						{
						JList theList = (JList) mouseEvent.getSource();
						if (mouseEvent.getClickCount() == 2) 
							{
							int index = theList.locationToIndex(mouseEvent.getPoint());
							if (index >= 0) 
								{
								FileElement f = list.get(index);
								f.open();
								}
							}
						
						if (SwingUtilities.isRightMouseButton(mouseEvent))
				            {
				            selectedItem=theList.locationToIndex(mouseEvent.getPoint());
				            theList.setSelectedIndex(theList.locationToIndex(mouseEvent.getPoint()));
				            jPM.show(theList, mouseEvent.getX(), mouseEvent.getY());
				            }
						}
					};
			    jLResults.addMouseListener(mouseListener);
			    
				}
			}
		
		if(arg instanceof FileElement)
			{
			final String str= ((FileElement) arg).getFileName();
			
			SwingUtilities.invokeLater(new Runnable()
				{	
				public void run()
					{
					model.add(index, str);
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
					model.removeAllElements();
					for(int i=0; i<list.size(); i++)
						model.add(i, list.get(i).getFileName());
					}
				});
			}
		}
	}
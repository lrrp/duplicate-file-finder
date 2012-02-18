package com.costeaalex.fileduplicatefinder.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


import com.costeaalex.fileduplicatefinder.searcher.FileElement;
import com.costeaalex.fileduplicatefinder.searcher.Searcher;

public class FDFGui implements ActionListener,Observer
	{
	static int selectedItem=-1;
	
	private JFrame jF;
	private JButton jBAction;
	private JButton jBE;
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
		
		tableModel = new DefaultTableModel(null, columns);
		jT=new JTableCustom(tableModel);
		jT.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jT.getColumnModel().getColumn(0).setPreferredWidth(612);
		jT.getColumnModel().getColumn(1).setPreferredWidth(150);
		JTableHeader header = jT.getTableHeader();
		header.setBackground(Color.white);
		jSPResults=new JScrollPane(jT);
		jSPResults.setPreferredSize(new Dimension(780, 530));
		
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
			list.get(selectedItem).remove();
			//System.out.println(list.get(selectedItem).getFileName());
			}
		if(e.getSource() == jBE)
			{
			popUpWindowError.hide();
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
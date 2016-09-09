/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

/**
 *
 * @author dell pc
 */
import java.util.Observer;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JProgressBar;

public class UI extends Download implements Observer {
	//private Download downloadobj;
	
	private ArrayList<Download> downloadList = new ArrayList<Download>();	
	//AbstractTableModel tableModel;
	JFrame frame = new JFrame("Internet Download Manager.");
	JTextField link = new JTextField(50);
	JPanel jap = new JPanel() ;
	JButton go = new JButton("GO!");
	JButton Download= new JButton("DOWNLOAD!");
	JButton Pause= new JButton("PAUSE!");	
	JButton Resume= new JButton("RESUME!");
	JButton Delete= new JButton("Delete!");
	JTable table;
        
	//Object[] row;
	

	//Object[][] rows;
	Object row[]=new Object[4];
	
	final String[] column1 = {"FileName", "Size", "Status","Progress"};
	JScrollPane Scroll_Table;
	
	JLabel link_text = new JLabel("Enter the link of File to Download...");	
	
	DefaultTableModel model = new DefaultTableModel();
	
	public  UI(){														//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    													//frame.setBounds(0,0,screenSize.width, screenSize.height);
	frame.setExtendedState(JFrame.MAXIMIZED_BOTH);			//frame.pack();
	frame.setLayout(null);												//frame.setSize();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	//jap.setLocation(150, 200);
	//jap.setSize(200, 20);
	link_text.setBounds(273,93,300,10);
	
//	jb.setValue(0);
        jb=new JProgressBar();
        jb.setValue(0);
        jb.setBorderPainted(true);
        jb.setBounds(50, 500, 200, 50);
        jb.setStringPainted(true);
        jb.setVisible(true);
        
	jap.add(link_text);
	jap.add(link);
	jap.setBounds(200, 120, 700, 30); 
	

	table = new JTable(model);
	model.setColumnIdentifiers(column1);
	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	//table.setRowHeight((int) renderer.getPreferredSize().getHeight());

	table.setBounds(270, 220, 900, 170);
	table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
	      public void valueChanged(ListSelectionEvent e) {
	    	  //tableSelectionChanged();
	      }
	    });
	    
	
	Scroll_Table = new JScrollPane(table);
	Scroll_Table.setBounds(270, 230, 900, 170);
	
	Download.setBounds(50, 150, 120, 30);
	Download.setBackground(Color.green);
	Download.setEnabled(false);
	
	Pause.setBounds(50, 200, 120, 30);
	Pause.setBackground(Color.BLACK);
	Pause.setForeground(Color.WHITE);
	Pause.setEnabled(false);
	Pause.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent eve){
		//pause();
			//	Pause.setBackground(Color.cyan);
		}
	});
	
	Resume.setBounds(50, 250, 120, 30);
	Resume.setBackground(Color.GRAY);
	Resume.setForeground(Color.WHITE);
	Resume.setEnabled(false);
	Resume.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent eve){
		//resume();
			//	Pause.setBackground(Color.cyan);
		}
	});
	
	Delete.setBounds(50, 300, 120, 30);
	Delete.setBackground(Color.red);
	Delete.setEnabled(false);
	Delete.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent eve){
		//delete();
			//	Pause.setBackground(Color.cyan);
		}
	});
	
	go.setBounds(925, 120, 75, 30); 
	go.setBackground(Color.cyan);
	go.setEnabled(true);
	go.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent eve){
			Add();

		}
	});
	
	frame.getContentPane().add(jap);
	frame.add(go);
	frame.add(Download);
	frame.add(Resume);
	frame.add(Pause);
	frame.add(Delete);
	frame.add(jap);
	frame.add(link_text);
	frame.add(Scroll_Table);
	frame.setVisible(true);
	//frame.add(downloadsPanel);
	frame.add(jb);
        
}
	protected void Add() {
		
		URL verifiedUrl = verifyUrl(link.getText());
		if (verifiedUrl != null) {
		
			addDownload(new Download(verifiedUrl));
			link.setText("");// reset add text field
		} else {
		link.setText("");// reset add text field
		JOptionPane.showMessageDialog(frame,
		"Invalid Download URL", "Error",
		JOptionPane.ERROR_MESSAGE);
		/*if (status== COMPLETE){
			JOptionPane.showMessageDialog(frame,
					"Download Complete!", "Error",
					JOptionPane.ERROR_MESSAGE);
			go.setEnabled(true);
		}*/
		}
		}
	
	public void addDownload ( Download download1) {
		//go.setEnabled(false);
		download1.addObserver(this);            
		row[0]=download1.getFileName(download1.getUrl());
		row[1]=download1.getSize();
		row[2]=download1.getStatus();
		row[3]=download1.jb;
		model.addRow(row);
		//downloadobj.addObserver(UI.this);
		//go.setEnabled(true);
		downloadList.add(download1);
		//tableModel.fireTableRowsInserted(table.getRowCount() - 1, table.getRowCount() - 1);
		}
	
	
	 URL verifyUrl(String url) {  

		 if (!url.toLowerCase().startsWith("http://"))
			 return null;
			 // Verify format of URL.
			 URL verifiedUrl = null;
			 try {
			 verifiedUrl = new URL(url);
			 } catch (Exception e) {
			 return null;
			 }
			 // Make sure URL specifies a file.
			 if (verifiedUrl.getFile().length() < 2)
			 return null;
			 return verifiedUrl;
		}
	 
	 public Download getDownload(int row) {
		    return (Download) downloadList.get(row);
		  }
	 @Override
		public void update(Observable arg0, Object arg1) {
                    
                    jb.setValue((int)getProgress());
			//model.addRow(row);
		}
	   
public static void main(String[] args){
	UI u = new UI();
}
	

}

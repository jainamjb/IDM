package javaapplication1;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JProgressBar;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Point;

public class UI extends Download {
	private Download selectedDownload;
	private ArrayList<Download> downloadList = new ArrayList<Download>();	
	JFrame frame = new JFrame("Internet Download Manager.");
	JTextField link = new JTextField(50);
	JPanel jap = new JPanel() ;
	JButton go = new JButton("GO!");
	JButton Download= new JButton("DOWNLOAD!");
	JButton Pause= new JButton("PAUSE!");	
	JButton Resume= new JButton("RESUME!");
	JButton Delete= new JButton("Delete!");
	JTable table;
	
    Download d = new Download();	
	JScrollPane Scroll_Table;
	JLabel link_text;	
	DownloadTableModel model;
	
	
	
	public  UI(){														
	link_text = new JLabel("Enter the link of File to Download...");
	model = new DownloadTableModel();	
	selectedDownload=null;												
																		
	frame.setExtendedState(JFrame.MAXIMIZED_BOTH);						
	frame.setLayout(null);												
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	link_text.setBounds(273,93,300,10);
    
        
	jap.add(link_text);
	jap.add(link);
	jap.setBounds(200, 120, 700, 30); 
	
	table = new JTable(model);
	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	//table.setBounds(270, 220, 900, 170);
	table.setLocation(270,220);
	
	table.addMouseListener(new MouseListener(){
    public void mouseClicked(MouseEvent evt)
    {   
       Point pnt = evt.getPoint();
       int row = table.rowAtPoint(pnt);
       selectedDownload=getDownload(row);
       //System.out.println(selectedDownload);
       if(status==DOWNLOADING)
    	   Pause.setEnabled(true);
       else if(status==PAUSED)
    	   Resume.setEnabled(true);
       else if(status==CANCELLED)
    	   Download.setEnabled(true);
       else if(status==COMPLETE)
       {
    	   Pause.setEnabled(false);
    	   Resume.setEnabled(false);
    	   Download.setEnabled(false);
       }
       Delete.setEnabled(true);
    }
    public void mouseEntered(MouseEvent arg0) {
}
public void mouseExited(MouseEvent arg0) {
}
public void mousePressed(MouseEvent arg0) {
}
public void mouseReleased(MouseEvent arg0) {
}
});
	 ProgressRenderer renderer = new ProgressRenderer(0, 100);
     renderer.setStringPainted(true);
     table.setDefaultRenderer(JProgressBar.class, renderer);
     table.setRowHeight(
             (int) renderer.getPreferredSize().getHeight());
      
     renderer.setStringPainted(true);
     renderer.setVisible(true);
     renderer.setBackground(Color.white);
     renderer.setForeground(Color.green.darker());
     renderer.setOpaque(true);
     renderer.setValue(0);
    
     
    Scroll_Table = new JScrollPane(table);
	Scroll_Table.setBounds(270, 230, 900, 170);
	
	Download.setBounds(50, 150, 120, 30);
	Download.setBackground(Color.green);
	Download.setEnabled(false);
	
	Pause.setBounds(50, 200, 120, 30);
	Pause.setBackground(Color.BLACK);
	Pause.setForeground(Color.WHITE);
	Pause.setEnabled(false);
	Pause.addMouseListener(new MouseListener(){
		public void mouseClicked(MouseEvent e) {
			  if(selectedDownload!=null)
              {
                  selectedDownload.pause();
                  Pause.setEnabled(false);
                  Resume.setEnabled(true);
              }			
		}
		public void mousePressed(MouseEvent e) {
		}
		public void mouseReleased(MouseEvent e) {
		}
		public void mouseEntered(MouseEvent e) {
		}
		public void mouseExited(MouseEvent e) {
		}
	});
	
	Resume.setBounds(50, 250, 120, 30);
	Resume.setBackground(Color.GRAY);
	Resume.setForeground(Color.WHITE);
	Resume.setEnabled(false);
	Resume.addMouseListener(new MouseListener(){
		public void mouseClicked(MouseEvent e) {
			  if(selectedDownload!=null)
            {
                selectedDownload.resume();
                Pause.setEnabled(true);
                Resume.setEnabled(false);
            }			
		}
		public void mousePressed(MouseEvent e) {
		}
		public void mouseReleased(MouseEvent e) {
		}
		public void mouseEntered(MouseEvent e) {
		}
		public void mouseExited(MouseEvent e) {
		}
	});
	
	Delete.setBounds(50, 300, 120, 30);
	Delete.setBackground(Color.red);
	Delete.setEnabled(false);
	Delete.addMouseListener(new MouseListener(){
		public void mouseClicked(MouseEvent e) {
			  if(selectedDownload!=null)
            {
                selectedDownload.cancel();
            }			
		}
		public void mousePressed(MouseEvent e) {
		}
		public void mouseReleased(MouseEvent e) {
		}
		public void mouseEntered(MouseEvent e) {
		}
		public void mouseExited(MouseEvent e) {
		}
	});
	
	go.setBounds(925, 120, 75, 30); 
	go.setBackground(Color.cyan);
	go.setEnabled(true);
	go.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent eve){
			Add();
                              // s=d.getSize();
                               //System.out.println(s);
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
	frame.add(renderer);
        
}
	protected void Add() {
		URL verifiedUrl = verifyUrl(link.getText());
		if (verifiedUrl != null) {
		
			model.addDownload(new Download(verifiedUrl));          
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
			 if (verifiedUrl.getFile().length() < 2)
			 return null;
			 return verifiedUrl;
		}
	 
	 public Download getDownload(int row) {
		    return (Download) downloadList.get(row);
		  }	   
public static void main(String[] args){
	new UI();
}
}
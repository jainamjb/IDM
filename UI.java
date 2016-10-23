import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
//import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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

public class UI {
	private boolean isSequential=false;
	private static Download selectedDownload;	
	JFrame frame = new JFrame("Internet Download Manager.");
	JTextField link = new JTextField(50);
	JCheckBox check = new JCheckBox();
	
	JPanel jap = new JPanel() ;
	JPanel jp = new JPanel();
	JButton go = new JButton("GO!");
	JButton Download= new JButton("DOWNLOAD!");
	JButton Pause= new JButton("PAUSE!");	
	JButton Resume= new JButton("RESUME!");
	JButton Delete= new JButton("Delete!");
	JButton Cancel= new JButton("Cancel!");
	JTable table;
	
    Download d = new Download();	
	JScrollPane Scroll_Table;
	JLabel link_text;	
	private DownloadTableModel model;
	public  UI(){														
	link_text = new JLabel("Enter the link of File to Download...");
	model = new DownloadTableModel();	
	selectedDownload=null;												
																		
	frame.setExtendedState(JFrame.MAXIMIZED_BOTH);						
	frame.setLayout(null);												
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	link_text.setBounds(273,93,300,10);
    check.setLocation(200, 45);
    check.setPreferredSize(null);
    check.setText("Enable Sequential Downlaod");
	check.setSelected(false);
	check.setVisible(true);
	check.addItemListener(new ItemListener(){
		public void itemStateChanged(ItemEvent ie) {
				if(check.isSelected()){
					isSequential=true;
					model.enableSequential();
				}
				else{
					isSequential=false;
				}
			}
	});
	
	jap.add(link_text);
	jp.add(check);
	jap.add(link);
	jap.setBounds(200, 120, 700, 30); 
	jp.setBounds(300, 400, 250, 30);
	
	table = new JTable(model);
	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	table.setLocation(270,220);
	
	table.addMouseListener(new MouseListener(){
    public void mouseClicked(MouseEvent evt)
    {   
       Point pnt = evt.getPoint();
       int row = table.rowAtPoint(pnt);
       selectedDownload=model.getDownload(row);
       Delete.setEnabled(true);
       Cancel.setEnabled(true);
       updateButtons();
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
	Download.addMouseListener(new MouseListener(){

		@Override
		public void mouseClicked(MouseEvent arg0) {
			if(selectedDownload!=null)
			{
				//selectedDownload.start(selectedDownload.getUrl());
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
		
	});
	
	Pause.setBounds(50, 200, 120, 30);
	Pause.setBackground(Color.BLACK);
	Pause.setForeground(Color.WHITE);
	Pause.setEnabled(false);
	Pause.addMouseListener(new MouseListener(){
		public void mouseClicked(MouseEvent e) {
			  if(selectedDownload!=null)
              {
				  Pause.setEnabled(false);
				  Resume.setEnabled(true);
				  //model.pause(selectedDownload);
				  selectedDownload.pause();
				  System.out.println("Paused !");
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
				  if(selectedDownload.status==selectedDownload.PAUSED)
					  {
					  	selectedDownload.resume();
				  	  	Pause.setEnabled(true);
				  	  	Resume.setEnabled(false);
					  }
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
               // selectedDownload.cancel();
                model.clearDownload(table.rowAtPoint(e.getPoint()));
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

	Cancel.setBounds(50, 350, 120, 30);
	Cancel.setBackground(Color.MAGENTA);
	Cancel.setEnabled(false);
	Cancel.addMouseListener(new MouseListener(){
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
			System.out.println("add called inside UI after pressing GO");
			Add();
                              // s=d.getSize();
                               //System.out.println(s);
		}
	});
	
	frame.getContentPane().add(jap);
	frame.getContentPane().add(jp);
	frame.add(go);
	frame.add(Cancel);
	frame.add(Download);
	frame.add(Resume);
	frame.add(Pause);
	frame.add(Delete);
	frame.add(jap);
	frame.add(jp);
	frame.add(link_text);
	frame.add(Scroll_Table);
	frame.setVisible(true);
	//frame.add(downloadsPanel);
	frame.add(renderer);
	//frame.add(enableSequentialDownload);
	//frame.add(check);
        
}
	protected void Add() {
		URL verifiedUrl = verifyUrl(link.getText());
		if (verifiedUrl != null) {
			System.out.println("url verified inside UI");
			model.addDownload(new Download(verifiedUrl,isSequential));          
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
	public void updateButtons(){
		if(selectedDownload.status==selectedDownload.DOWNLOADING)
	    	   Pause.setEnabled(true);
	       else if(selectedDownload.status==selectedDownload.PAUSED)
	    	   Resume.setEnabled(true);
	       else if(selectedDownload.status==selectedDownload.CANCELLED)
	    	   Download.setEnabled(true);
	       else if(selectedDownload.status==selectedDownload.COMPLETE)
	       {
	    	   Pause.setEnabled(false);
	    	   Resume.setEnabled(false);
	    	   Download.setEnabled(false);
	       } 
	       else if(selectedDownload.status==selectedDownload.WAITING)
	       {
	    	   Pause.setEnabled(false);
	    	   Resume.setEnabled(false);
	    	   Download.setEnabled(false);
	       }
	}
	public static void main(String[] args){
	//(Thread.currentThread()).setName("MainThread");
	new UI();
}
}
//package javaapplication1;
import java.util.*;

import javax.swing.JProgressBar;
//import javax.swing.*;
import javax.swing.table.*;
@SuppressWarnings("serial")
class DownloadTableModel extends AbstractTableModel
implements Observer
{
private boolean isSequential=false;
private static final String[] columnNames = {"File name", "Size(in MB)",
"Status", "Progress","Download Speed(in Kbps)"};
@SuppressWarnings("rawtypes")
private static final Class[] columnClasses = {String.class,
String.class, String.class, JProgressBar.class,String.class};
private ArrayList<Download> downloadList = new ArrayList<Download>();
public void addDownload(Download download) {
download.addObserver(this);
downloadList.add(download);
fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);

}
public Download getDownload(int row) {
return downloadList.get(row);
}
public void clearDownload(int row) {
downloadList.remove(row);
fireTableRowsDeleted(row, row);}
public int getColumnCount() {
return columnNames.length;
}
public String getColumnName(int col) {
return columnNames[col];
}

@SuppressWarnings({ "unchecked", "rawtypes" })
public Class getColumnClass(int col) {
return columnClasses[col];
}

public int getRowCount() {
return downloadList.size();
}

public Object getValueAt(int row, int col) {
	Download download = downloadList.get(row);
	if(row+1<=(downloadList.size()-1)&&isSequential&&(download.status==2||download.status==3))
	if(downloadList.get(row+1).status==5)
		if(downloadList.get(row+1)!=null){
			downloadList.get(row+1).start();
			//Start next download if sequential is enabled
			fireTableRowsUpdated(row+1,row+1);
		}
switch (col) {
case 0: // URL
return download.getFileName(download.getUrl());
case 1: // Size
float size = (float)download.getSize()/(1024*1024);
return (size == -1) ? "" : String.format("%.2f", size);
case 2: // status
return Download.STATUSES[download.getStatus()];
case 3: // progress
return (download.getStatus()==2)?100:new Float(download.getProgress());
case 4: // speed
return String.format("%.2f", download.getDownloadSpeed());
}
return "";
}

public void update(Observable o, Object arg) {
	int index = downloadList.indexOf(o);
	fireTableRowsUpdated(index, index);
//Download download = downloadList.get(0);
//download.getDownloadSpeed();
}
/*public void pause(Download pauseMe){
	pauseMe.pause();
}
public void resume(Download resumeMe){
	resumeMe.resume();
}*/
	public void enableSequential() {
		isSequential=true;
	} 
}
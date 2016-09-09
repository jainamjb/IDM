package javaapplication1;
import java.io.*;

import java.net.*;
import java.util.*;
import javax.swing.JProgressBar;

class Download extends Observable implements Runnable {

private static final int BUFFER_SIZE = 1024;
// statuses
public static final String STATUSES[] = {"Downloading",
"Paused", "Complete", "Cancelled", "Error"};
int progress;
JProgressBar jb;
public ArrayList<Download> downloadList = new ArrayList<Download>();
public  final int DOWNLOADING = 0;
public  final int PAUSED = 1;
public  final int COMPLETE = 2;
public  final int CANCELLED = 3;
public final int ERROR = 4;
public URL url; // download URL
protected int size; // size of download in bytes
private int downloaded; // number of bytes downloaded
protected int status; // current status of download
// Constructor
Download(){
    
}

Download( URL url){
   
	//size=-1;
	downloaded = 0;
	status = DOWNLOADING;
	this.url=url;
        start();
}
//Start downloading
protected void start() {

Thread t = new Thread(this);
status=DOWNLOADING;
t.start();

}

protected String getFileName(URL url) {
String fileName = url.getFile();
return fileName.substring(fileName.lastIndexOf('/') + 1);
}



public void run() {
FileOutputStream file = null;
BufferedInputStream stream = null;
try {
//Open connection to URL
HttpURLConnection connection =(HttpURLConnection) url.openConnection();
connection.setRequestMethod("GET");
connection.setDoInput(true);
connection.connect();

//200 response code is for invalid file.
int contentLength = connection.getContentLength();
if (contentLength < 1) {
  error();
}

if (connection.getResponseCode() / 100 != 2) {
error();
}
stream=new BufferedInputStream(connection.getInputStream());//get stream

//Open file and seek.
file = new FileOutputStream(getFileName(url));

byte[] buffer= new byte[BUFFER_SIZE];
int len=0;
downloaded=0;
len=stream.read(buffer);

while (len != -1) {
//adjusting buffer
file.write(buffer,0,len);
len=stream.read(buffer) ;
downloaded+=len;
progress=downloaded/size;
jb.setValue(progress);
stateChanged();
}
size=contentLength;
file.flush();
//Close file.
if (file != null) {
try {
file.close();
} 
catch (Exception e) {}
}
//Close connection 
if (stream != null) {
try {
stream.close();
} catch (Exception e) {}
}

/* Change status to complete if this point was
reached because downloading has finished. */
status = COMPLETE;
stateChanged();

} 
catch (Exception e) {
error();
}
}


public URL getUrl() {
return url;
}
// Get this download's size.
public int getSize() {
return size;
}
// Get this download's progress.
public float getProgress() {
return ((float) downloaded / size) * 100;
}
// Get this download's status.
public int getStatus() {
return status;
}
// Pause this download.
public void pause() {
status = PAUSED;
stateChanged();
}
// Resume this download.
public void resume() {
status = DOWNLOADING;
stateChanged();
start();
}
// Cancel this download.
public void cancel() {
status = CANCELLED;
stateChanged();
}
//Mark this download as having an error.
private void error() {
status = ERROR;
stateChanged();
}

public Download getDownload(int row) {
    return (Download) downloadList.get(row);
  }

//Notify observers that this download's status has changed.
private void stateChanged() {
setChanged();
notifyObservers();
}

}
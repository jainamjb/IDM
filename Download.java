//package javaapplication1;
import java.io.*;

import java.net.*;
import java.util.*;

class Download extends Observable implements Runnable {
private static final int BUFFER_SIZE = 8192;
public static final String STATUSES[] = {"Downloading","Paused", "Complete", "Cancelled", "Error"};
public  final int DOWNLOADING = 0;
public  final int PAUSED = 1;
public  final int COMPLETE = 2;
public  final int CANCELLED = 3;
public final int ERROR = 4;
public URL url; // download URL
public int size,contentLength; // size of download in bytes
protected int downloaded; // number of bytes downloaded
protected int status; // current status of download
private double download_speed;
private double start_time;
private Thread t;

Download(){
	
}
//Start downloading
 Download(URL url) {

	//size=-1;
	//downloaded = 0;
	status = DOWNLOADING;
	this.url=url;
t = new Thread(this);
status=DOWNLOADING;
t.start();
}

public String getFileName(URL url) {
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

//200 response code is for invalid file.
//connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
connection.connect();
if (connection.getResponseCode() / 100 != 2) {
error();
}
contentLength =Integer.parseInt( connection.getHeaderField("Content-Length"));
//System.out.println(contentLength);
size=contentLength;
if (contentLength < 1) {
  error();
}

stream=new BufferedInputStream(connection.getInputStream());//get stream

//Open file and seek.

file = new FileOutputStream(getFileName(url));
byte[] buffer= new byte[BUFFER_SIZE];
 start_time= System.nanoTime();
int len=0;
len=stream.read(buffer);

if(status==DOWNLOADING)
downloaded=0;
try{
boolean goback=false;
do{
while (len != -1&&status==DOWNLOADING) {
//adjusting buffer
file.write(buffer,0,len);
len=stream.read(buffer) ;
downloaded+=len;
/*if(getProgress()==30){
	Thread.currentThread().sleep(10000);
}*/
stateChanged();
}
if(status==PAUSED){
	goback=true;
	//System.out.println("Paused !!!!!");
}
	synchronized(t){
	//System.out.println(t.getName());
	t.wait();}

System.out.println(status);
}while(goback==true);

}
finally{
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
}
/* Change status to complete if this point was
reached because downloading has finished. */
status = COMPLETE;
UI.changeTheButtons();
stateChanged();

} 
catch (Exception e) {
error();
e.printStackTrace(System.out);
}
}
 
public Double getDownloadSpeed(){
    double current_time=System.nanoTime();
    //System.out.println((1000000000.0*downloaded)/(1024*(current_time-start_time)));
    return (1000000000.0*downloaded)/(1024*(current_time-start_time));
}

public URL getUrl() {
return url;
}

public int getSize() {
return contentLength;
}

public float getProgress() {
return ((float) downloaded / size) * 100;
}


public int getStatus() {
return status;
}
// Pause
public void pause() {
status = PAUSED;
stateChanged();
}
// Resume this download.
public void resume() {
status = DOWNLOADING;
stateChanged();
synchronized(t){
t.notify();}
}
public void start(URL url)
{
	Thread t=new Thread(this);
	t.start();
}

public void cancel() {
status = CANCELLED;
stateChanged();
}


private void error() {
status = ERROR;
stateChanged();
}


private void stateChanged() {
//System.out.println(contentLength+" "+downloaded);
setChanged();
notifyObservers();
}

}
//package javaapplication1;
import java.io.*;

import java.net.*;
import java.util.*;

class Download extends Observable implements Runnable {
	int j,i;												//To be removed later
	private static final int BUFFER_SIZE = 8192;
	public static final String STATUSES[] = {"Downloading","Paused", "Complete", "Cancelled", "Error","Waiting"};
	public  final int DOWNLOADING = 0;
	public  final int PAUSED = 1;
	public  final int COMPLETE = 2;
	public  final int CANCELLED = 3;
	public final int ERROR = 4;
	public final int WAITING = 5;
	public URL url; // download URL
	public int size,contentLength; // size of download in bytes
	protected int downloaded; // number of bytes downloaded
	protected int status; // current status of download
	private double start_time;
	private Thread t;
	private static int count=0;
	public Thread getT(){return t;}
	Download(){
		t=new Thread();
		//wait
	}
//Start downloading
	Download(URL url,boolean isSequential) {
		count++;
		stateChanged();
		this.url=url;
		if(!isSequential||count==1)
		{
			status = DOWNLOADING;
			stateChanged();
			t = new Thread(this);
			t.start();
		}
		else {
			status = WAITING;
			stateChanged();
		}
		System.out.println("run called inside download class");
	}
	
	public String getFileName(URL url) {
		String fileName = url.getFile();
		return fileName.substring(fileName.lastIndexOf('/') + 1);
	}
	
@SuppressWarnings("static-access")
	public void run() {
		RandomAccessFile actualFile=null;
		BufferedInputStream stream = null;
	//System
	/*if(count>=2)
	{
		status=WAITING;
		return;
	}*/
	
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
			byte[] buffer= new byte[BUFFER_SIZE];
			start_time= System.nanoTime();
			int len=stream.read(buffer);
			actualFile = new RandomAccessFile(getFileName(url), "rw");
			actualFile.seek(downloaded); 
			System.out.println("Inside Download ! Enetring while loop in run !");
			while (len != -1&&status==DOWNLOADING) {
				//adjusting buffer
				actualFile.write(buffer,0,len);
				len=stream.read(buffer) ;
				downloaded+=len;
				stateChanged();
				
				try{
					t.sleep(400);							//To be reomoved later !
				}
				catch(InterruptedException e) { }
				if(downloaded>=0.14*contentLength)
					break;
			}
			System.out.println(status);
			if(status==DOWNLOADING){
				status = COMPLETE;
				count--;
				stateChanged();}
		}
		catch (Exception e) {
			error();
			e.printStackTrace(System.out);
		}
		
		finally{
			
//Close file.
			if (actualFile != null) {
				try {
					actualFile.close();
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
System.out.println(" Inside Download (by selectedDownload) waiting because pause is pressed");
}
// Resume this download.
public void resume() {
status = DOWNLOADING;
stateChanged();
start();
}
public void start()
{
	status=DOWNLOADING;
	t=new Thread(this);
	t.start();
}

public void cancel() {
	count--;
	status = CANCELLED;
	stateChanged();
}


private void error() {
status = ERROR;
stateChanged();
}


private void stateChanged() {
	if(i/20==j)
			{System.out.println(contentLength+" "+downloaded);j++;}
	i++;
	/*if(status==COMPLETE)
	{
		startNext();
	}*/
	setChanged();
	notifyObservers();
}
/*private void startNext() {
	
}*/

}
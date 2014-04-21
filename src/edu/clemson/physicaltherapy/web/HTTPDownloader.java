package edu.clemson.physicaltherapy.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.FilenameUtils;

public class HTTPDownloader {
	
	public static boolean downloadFile(String url_string, String file_string)
	{
		try {
			

			
	        //set the download URL, a url that points to a file on the internet
	        //this is the file to be downloaded
	        URL url = new URL(url_string);
	        
	        //create the new connection
	        
	        HttpURLConnection urlConnection;
	        System.err.println("Protocol = "+url.getProtocol());
	        

	        
	        System.err.println("Downloading "+url_string);
	        if (url.getProtocol().equalsIgnoreCase("https"))
	        {
	        	urlConnection = (HttpsURLConnection) url.openConnection();
	        }
	        
	        else // if (url.getProtocol().equalsIgnoreCase("http"))
	        {
	        	 urlConnection = (HttpURLConnection) url.openConnection();
	        }
	        //set up some things on the connection
	        urlConnection.setRequestMethod("GET");
	        urlConnection.setDoOutput(true);
	        

	        //and connect!
	        urlConnection.connect();

	        //create a new file, specifying the path, and the filename
	        //which we want to save the file as.
	        File file = new File(file_string);

	        //this will be used to write the downloaded data into the file we created
	        FileOutputStream fileOutput = new FileOutputStream(file);

	        //this will be used in reading the data from the internet
	        InputStream inputStream = urlConnection.getInputStream();

	        //this is the total size of the file
	        int totalSize = urlConnection.getContentLength();
	        //variable to store total downloaded bytes
	        int downloadedSize = 0;

	        //create a buffer...
	        byte[] buffer = new byte[1024];
	        int bufferLength = 0; //used to store a temporary size of the buffer

	        //now, read through the input buffer and write the contents to the file
	        while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
	                //add the data in the buffer to the file in the file output stream (the file on the sd card
	                fileOutput.write(buffer, 0, bufferLength);
	                //add up the size so we know how much is downloaded
	                downloadedSize += bufferLength;
	                //this is where you would do something to report the prgress, like this maybe
//	                updateProgress(downloadedSize, totalSize);

	        }
	        //close the output stream when done
	        fileOutput.close();
	        return true;

	//catch some possible errors...
	} catch (MalformedURLException e) {
	        e.printStackTrace();
	        return false;
	} catch (IOException e) {
	        e.printStackTrace();
	        return false;
	}
	}
	
    public static String getFilenameFromUrl(String url_string) {

        String baseName = FilenameUtils.getBaseName(url_string);
        String extension = FilenameUtils.getExtension(url_string);
        /// http://stackoverflow.com/questions/6138127/how-to-do-url-decoding-in-java
        return android.net.Uri.decode(baseName+"."+extension);
        
    }
    
	public static String getDirectUrl(String videoUrl)
	{

		// Fix up dropbox urls to take you directly to the file.
	    if (videoUrl.contains("www.dropbox.com"))
	    {
	    	//System.err.println("Fixing dropbox url");
	    	videoUrl = videoUrl.replaceAll("www.dropbox.com", "dl.dropboxusercontent.com");
	    }
	    return videoUrl; 
	}
	
	
	public static String getLocalFileNameFromUrl(String videoUrl)
	{

		String filename = HTTPDownloader.getFilenameFromUrl(videoUrl);

	    
	    // Android doesn't support Apple's ".m4v" mp4 files. Rename them as a workaround.
	    if (filename.contains(".m4v"))
	    {
	    	//System.err.print("Changing file name from "+filename);
	    			
	    	filename = filename.replaceAll(".m4v", ".mp4");
	    	//System.err.println("to "+filename);
	    }
	    
		return filename;
	}
}

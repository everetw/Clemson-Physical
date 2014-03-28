package com.example.clemsonphysical;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class VideosManager {
	// SDCard Path
	
	final String MEDIA_PATH = new String("/sdcard/");
	private ArrayList<HashMap<String, String>> videosList = new ArrayList<HashMap<String, String>>();
	
	// Constructor
	public VideosManager(){
		
	}
	
	/**
	 * Function to read all mp3 files from sdcard
	 * and store the details in ArrayList
	 * */
	public ArrayList<HashMap<String, String>> getPlayList(File home){
		
		// TODO: Get filenames from internal database.
		
		

		if (home.listFiles(new FileExtensionFilter()).length > 0) {
			for (File file : home.listFiles(new FileExtensionFilter())) {
				HashMap<String, String> video = new HashMap<String, String>();
				video.put("videoTitle", file.getName().substring(0, (file.getName().length() - 4)));
				video.put("videoPath", file.getPath());
				
				// Adding each video to VideoList
				videosList.add(video);
			}
		}
		// return videos list array
		return videosList;
	}
	
	/**
	 * Class to filter files which are having .mp3 extension
	 * */
	class FileExtensionFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".mp3") || name.endsWith(".MP3") || name.endsWith(".mp4") || name.endsWith(".m4v") || name.endsWith(".3gp"));
		}
	}
}

package com.example.clemsonphysical;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;



/// Fix surface has been released problem https://androidproblem.wordpress.com/category/mediaplayer/

public abstract class VideoPlayerActivity extends CameraActivity implements OnCompletionListener {


	protected VideoView videoView;
	protected VideosManager videoManager;
	protected Utilities utils;
	protected int currentVideoIndex = 0; 
	protected boolean isShuffle = false;
	protected boolean isRepeat = false;
	protected ArrayList<HashMap<String, String>> videosList = new ArrayList<HashMap<String, String>>();
	

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_practitioner_video_view);
		
		// All player buttons

		videoView = (VideoView) findViewById(R.id.videoView1);		
		videoManager = new VideosManager();
				
		utils = new Utilities();
		
		// Listeners
		//videoProgressBar.setOnSeekBarChangeListener(this); // Important
		videoView.setOnCompletionListener(this); // Important
		videoView.setMediaController(new MediaController(this));
		
		// Getting all videos list
		videosList = videoManager.getPlayList(this.getExternalFilesDir("videos"));
		
		// By default play first video
		playVideo(0);
				

	}
	

	
	/**
	 * Function to play a video
	 * @param videoIndex - index of video
	 * */
	public void  playVideo(int videoIndex){
		// Play video
		try {
			if (videoIndex >= videosList.size())
				return;
			videoView.setVideoPath(videosList.get(videoIndex).get("videoPath"));
        	videoView.start();
		
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} 
		  catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			
		}
	}


	/**
	 * On Video Playing completed
	 * if repeat is ON play same video again
	 * if shuffle is ON play random video
	 * */
	@Override
	public void onCompletion(MediaPlayer arg0) {
		
		// check for repeat is ON or OFF
		if(isRepeat){
			// repeat is on play same video again
			playVideo(currentVideoIndex);
		} else if(isShuffle){
			// shuffle is on - play a random video
			Random rand = new Random();
			currentVideoIndex = rand.nextInt((videosList.size() - 1) - 0 + 1) + 0;
			playVideo(currentVideoIndex);
		} else{
			// no repeat or shuffle ON - play next video
			if(currentVideoIndex < (videosList.size() - 1)){
				playVideo(currentVideoIndex + 1);
				currentVideoIndex = currentVideoIndex + 1;
			}else{
				// play first video
				playVideo(0);
				currentVideoIndex = 0;
			}
		}
	}
	
	@Override
	 public void onDestroy(){
	 super.onDestroy();
	    //mp.release();
	 }
	
	@Override
	
	/**
	 *
	 * @fn public boolean onCreateOptionsMenu(Menu menu)
	 * @brief Inflate the menu; this adds items to the action bar if it is present.
	 * @param menu Meny to be created.
	 * @return true
	 */
	
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.video_menu, menu);
		return true;
	}
	
	@Override
	/**
	 * @fn public boolean onOptionsItemSelected(MenuItem item)
	 * @brief Handles menu item selection. 
	 * Only menu item here is the "action_about" for the info activity.
	 * @param item MenuItem that was selected
	 * @return true  
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
	
		 Intent intent = null;
		/// Menu from http://developer.android.com/guide/topics/ui/menus.html#options-menu
	    switch (item.getItemId()) {
	   
		case android.R.id.home:

			NavUtils.navigateUpFromSameTask(this);
			return true; 
			
	    case R.id.action_about:
//	    	intent = new Intent(this, InfoView.class);
//	    	startActivity(intent);
	    	break;

	    case R.id.action_add_annotation:
	    	Annotate();
	    	break;
	    	
	    case R.id.action_annotation_list:
	    	//deleteAllRows();
	    	break;	
	    	
	    case R.id.action_settings:
	    	intent = new Intent(this, SettingsActivity.class);
	    	startActivity(intent);
	    	break;
	    case R.id.action_record:
	    	dispatchTakeVideoIntent();
	    	break;
	    	
	    }
	    return true;
	}

	public abstract void Annotate();


	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 100){
        	 currentVideoIndex = data.getExtras().getInt("videoIndex");
        	 // play selected video
            playVideo(currentVideoIndex);
       }
		
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) 
		{
			/// Fixed null pointer exceptions from - http://www.androidhive.info/2013/09/android-working-with-camera-api/
	        try {
	        	displayToast("Preview Video "+mediaUri.getPath());
	        	previewVideo(videoView,mediaUri.getPath());
	            //rotateAndSetImage(imageViewHolder,mediaUri.getPath());
	            //imageTextViewHolder.setText(mediaUri.getPath());
	        } catch (NullPointerException e) {
	            e.printStackTrace();
	        }
	        
	    }
		else if ((requestCode == REQUEST_IMAGE_CAPTURE || requestCode == REQUEST_VIDEO_CAPTURE) && resultCode == RESULT_CANCELED)
		{
			File file = new File(mediaUri.getPath());
			file.delete();
		}

		
	}
	


	
	
}
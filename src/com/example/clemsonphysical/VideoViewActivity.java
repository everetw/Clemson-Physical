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

public abstract class VideoViewActivity extends DatabaseActivity  {


	protected VideoView videoView;
	protected TextView annotationTextView;
	

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_view);
		
		
		// All player buttons

		videoView = (VideoView) findViewById(R.id.videoView1);	
		//Add the media controller.
		videoView.setMediaController(new MediaController(this));

		annotationTextView = (TextView)findViewById(R.id.textView1);
				

	}
	

	public abstract void Annotate();


	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);

		
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
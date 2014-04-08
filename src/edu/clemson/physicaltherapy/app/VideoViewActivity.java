package edu.clemson.physicaltherapy.app;


import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.clemsonphysical.R;



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
	
	


	

	


	
	
}
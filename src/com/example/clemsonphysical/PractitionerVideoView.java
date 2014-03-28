package com.example.clemsonphysical;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PractitionerVideoView extends VideoPlayerActivity {

	private TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		textView = (TextView)findViewById(R.id.textView1);
		//setContentView(R.layout.activity_practitioner_video_view);
	}
	
	@Override
	public void addAnnotation()
	{
		if (videoView.isPlaying())
		{
			videoView.pause();
			int currentTime = videoView.getCurrentPosition();
			textView.setText(Integer.toString(currentTime));
			textView.setTextColor(Color.rgb(255, 255, 255));
			textView.setVisibility(View.VISIBLE);
		}
			
	}



}

package com.example.clemsonphysical;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class UserVideoView extends DatabaseActivity {

	private ExerciseLog exerciseLog;
	protected VideoView videoView;
	protected TextView annotationTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_video_view);
		
		
		// Setup the Action Bar
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
				
		
		// All player buttons
		videoView = (VideoView) findViewById(R.id.videoView1);	
		//Add the media controller.
		videoView.setMediaController(new MediaController(this));

		annotationTextView = (TextView)findViewById(R.id.textView1);
		Intent intent = getIntent();
		exerciseLog = (ExerciseLog)intent.getSerializableExtra("ExerciseLogClass");
		
		Exercise exercise;
		try {
			exercise = (Exercise) Exercise.getById(dbSQLite, exerciseLog.getExerciseId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			exercise = new Exercise();
		}
        //Set the title of the Action Bar to the Exercise Name
        getActionBar().setTitle(exercise.getName()+" @ " + exerciseLog.getCreateTime());
        
		videoView.setVideoPath(exerciseLog.getVideoLocation());
		displayToast(exerciseLog.getVideoLocation());
		videoView.start();
		
	}
	
	
	public void Annotate()
	{
		if (videoView.isPlaying())
		{
			videoView.pause();
		}
		
		int currentTime = videoView.getCurrentPosition();
		annotationTextView.setText(Integer.toString(currentTime));
		annotationTextView.setTextColor(Color.rgb(255, 255, 255));
		annotationTextView.setVisibility(View.VISIBLE);
		
			
	}
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
	    	intent = new Intent(this, InfoView.class);
	    	startActivity(intent);
	    	break;

	    case R.id.action_add_annotation:
	    	Annotate();
	    	break;
	    	
	    case R.id.action_annotation_list:
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


	@Override
	protected String getVideoSubdirectory() {
		
		return "user_videos";
	}

}

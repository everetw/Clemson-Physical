package edu.clemson.physicaltherapy.app;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.example.clemsonphysical.R;

import edu.clemson.physicaltherapy.database.DatabaseObject;
import edu.clemson.physicaltherapy.datamodel.Exercise;
import edu.clemson.physicaltherapy.datamodel.ExerciseLog;
import edu.clemson.physicaltherapy.datamodel.ExerciseLogAnnotation;

public class UserVideoView extends DatabaseActivity  {

	private ExerciseLog exerciseLog;
	protected VideoView videoView;
	protected TextView annotationTextView;
	
	private ExerciseLogAnnotation current_annotation;
	private List<DatabaseObject> annotationList;
	private Handler videoHandler = new Handler();
	
	private static int POLL_INTERVAL = 100;
	
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
		MediaController mediaController = new MediaController(this);
		
		videoView.setMediaController(mediaController);
		
		/// Information about VideoView http://www.techotopia.com/index.php/Implementing_Video_Playback_on_Android_using_the_VideoView_and_MediaController_Classes


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
		//displayToast(exerciseLog.getVideoLocation());
		videoHandler.postDelayed(checkAnnotations, POLL_INTERVAL);
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
		
		getMenuInflater().inflate(R.menu.user_video_menu, menu);
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
//	    case R.id.action_record:
//	    	dispatchTakeVideoIntent();
//	    	break;
	    	
	    }
	    return true;
	}


	@Override
	protected String getVideoSubdirectory() {
		
		return "user_videos";
	}
	

	///Repeat a task with a time delay using handlers - http://stackoverflow.com/questions/6242268/repeat-a-task-with-a-time-delay/
	/// More on handlers. http://www.tutorialspoint.com/android/android_mediaplayer.htm
	private Runnable checkAnnotations = new Runnable() {
	      public void run() {
	    	 
	    	 // if the videoView isn't playing, return.
	    	 // This coding is shameful. 
	    	  System.err.println("Checking annotations");
	    	 if (!videoView.isPlaying())
	    	 {
	    		 videoHandler.postDelayed(this, POLL_INTERVAL);
	    		 return;
	    	 }
	    	 
	    	 // Find the current time
	         int currentTime = videoView.getCurrentPosition();
	         
	         // Calculate the time of the next check. 
	         
	         int nextTime = currentTime+POLL_INTERVAL;
	         if ((currentTime / POLL_INTERVAL) % 10 == 0)
	         {
	        	 // Every second, update the annotation.
	         annotationTextView.setText("Seconds "+currentTime/1000);
	         annotationTextView.setTextColor(Color.rgb(255, 255, 255));
	 		annotationTextView.setVisibility(View.VISIBLE);
	         }
	         
//		         if (current_annotation.getVideoTime() < nextTime )
//		         {
//		        	 annotationTextView.setText("Annotation" );
//		        	 current_annotation = (ExerciseLogAnnotation)annotationList.get(++annotation_index);
//		         }
	         // Call again in POLL_INTERVAL ms.
	         videoHandler.postDelayed(this, POLL_INTERVAL);
	      }
	   };


	@Override
	protected void onPause()
	{
		System.err.println("On pause");
		super.onPause();
		videoHandler.removeCallbacks(checkAnnotations);
	}
	
	@Override
	protected void onResume()
	{
		System.err.println("On resume");
		super.onResume();
		videoHandler.postDelayed(checkAnnotations, POLL_INTERVAL);
	}
	
	@Override
	protected void onStop()
	{
		System.err.println("On stop");
		videoHandler.removeCallbacks(checkAnnotations);
		super.onStop();
		
	}
	
	

		  


}

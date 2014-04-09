package edu.clemson.physicaltherapy.app;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import edu.clemson.physicaltherapy.R;
import edu.clemson.physicaltherapy.datamodel.DatabaseObject;
import edu.clemson.physicaltherapy.datamodel.Exercise;
import edu.clemson.physicaltherapy.datamodel.ExerciseLog;
import edu.clemson.physicaltherapy.datamodel.ExerciseLogAnnotation;

public class UserVideoView extends VideoViewActivity  {

	private ExerciseLog exerciseLog;
	private ExerciseLogAnnotation current_annotation;
	private List<DatabaseObject> annotationList;
	
	private static int POLL_INTERVAL = 100;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
        
		setVideoPath(exerciseLog.getVideoLocation());
		
		start();
		
	}
	
	@Override
	protected void setContentView()
	{
		setContentView(R.layout.activity_user_video_view);
	}
	
	@Override
	public void addAnnotation(int time, String annotation)
	{
		// Add the annotation to the database
		//displayAnnotation(time+": "+annotation);
		ExerciseLogAnnotation ela = new ExerciseLogAnnotation(0,exerciseLog.getId(),time,annotation);
		try
		{
			ela.add(dbSQLite);
		} catch (Exception e)
		{
			e.printStackTrace();	
		}
		
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
	    	displayAnnotationDialog();
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


	@Override
	public String readAnnotation(int time, int interval) {
		// TODO Auto-generated method stub
		ExerciseLogAnnotation ela =  ExerciseLogAnnotation.getNextAnnotationByTime(dbSQLite, exerciseLog.getId(), time, interval);
		if (ela == null)
		{
			return null;
		}
		return ela.getAnnotation();
	}



	
	

		  


}

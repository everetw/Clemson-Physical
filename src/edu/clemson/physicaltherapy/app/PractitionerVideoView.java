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
import edu.clemson.physicaltherapy.datamodel.ExerciseAnnotation;
import edu.clemson.physicaltherapy.datamodel.ExerciseLogAnnotation;

public class PractitionerVideoView extends VideoViewActivity {

	
	private Exercise exercise;
	private ExerciseAnnotation current_annotation;
	private List<DatabaseObject> annotationList;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		exercise = (Exercise)intent.getSerializableExtra("ExerciseClass");
		
        //Set the title of the Action Bar to the Exercise Name
        getActionBar().setTitle(exercise.getName());
        
		setVideoPath(exercise.getFileLocation());
		
		start();
		
	
	}
	
	@Override
	protected void setContentView()
	{
		setContentView(R.layout.activity_video_view);
	}
	
	@Override
	public void addAnnotation(int time, String annotation)
	{
		// Add the annotation to the database
		//displayAnnotation(time+": "+annotation);
		ExerciseAnnotation ea = new ExerciseAnnotation(0,exercise.getId(),time,annotation);
		try
		{
			ea.add(dbSQLite);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected String getVideoSubdirectory() {
		
		return "exercises";
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
//	    	
	    }
	    return true;
	}

	@Override
	public String readAnnotation(int time, int interval) {
		// TODO Auto-generated method stub
		ExerciseAnnotation ela =  ExerciseAnnotation.getNextAnnotationByTime(dbSQLite, exercise.getId(), time, interval);
		if (ela == null)
		{
			return null;
		}
		return ela.getAnnotation();
	}



}

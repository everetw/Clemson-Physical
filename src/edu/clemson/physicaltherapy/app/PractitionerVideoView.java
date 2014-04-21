package edu.clemson.physicaltherapy.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import edu.clemson.physicaltherapy.R;
import edu.clemson.physicaltherapy.datamodel.Exercise;
import edu.clemson.physicaltherapy.datamodel.ExerciseAnnotation;

public class PractitionerVideoView extends VideoViewActivity {

	
	private Exercise exercise;
	private ExerciseAnnotation current_annotation;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		exercise = (Exercise)intent.getSerializableExtra("ExerciseClass");
		
        //Set the title of the Action Bar to the Exercise Name
        getActionBar().setTitle(exercise.getName());
//        System.err.println("Playing "+exercise.getFileLocation());
		setVideoPath(exercise.getFileLocation());
      
//        System.err.println("Playing "+exercise.getVideoUrl());
//		setVideoPath(exercise.getVideoUrl());
		
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
	    	onActionAddAnnotation();
	    	break;
	
	    	
	    case R.id.activity_show_log:
	    	showLog(null);
	    	break;
	    	
	    case R.id.action_settings:
	    	intent = new Intent(this, SettingsActivity.class);
	    	startActivity(intent);
	    	break;

	    case R.id.action_delete_all_annotations:
	    	displayDeleteAllDialog();
	    	break;

	    }
	    return true;
	}
	
	public void showLog(View view){
		Intent intent = new Intent(this, LogView.class);
		intent.putExtra("ExerciseClass", exercise);
		startActivity(intent);
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



	@Override
	public void updateAnnotation(int time, String annotation, int interval) {
		// TODO Auto-generated method stub
		ExerciseAnnotation exerciseAnnotation = ExerciseAnnotation.getPreviousAnnotationByTime(dbSQLite, exercise.getId(), time, interval);
		if (exerciseAnnotation == null)
		{
			addAnnotation(time,annotation);
		}
		else
		{
			exerciseAnnotation.setAnnotation(annotation);
			exerciseAnnotation.setVideoTime(time);
			exerciseAnnotation.update(dbSQLite);
		}
		//System.err.println("updateAnnotation "+annotation);
		
	}

	@Override
	public void deleteAnnotation(int time, String annotation, int interval) {
		// TODO Auto-generated method stub
		//System.err.println("deleteAnnotation "+annotation);
		ExerciseAnnotation exerciseAnnotation = ExerciseAnnotation.getPreviousAnnotationByTime(dbSQLite, exercise.getId(), time, interval);
		if (exerciseAnnotation != null)
		{
			exerciseAnnotation.delete(dbSQLite);
		}
		
		removeAnnotation();
		
	}

	@Override
	protected void deleteAllAnnotations() {
		// TODO Auto-generated method stub
		ExerciseAnnotation.deleteAllByExerciseId(dbSQLite,exercise.getId());
		
	}



}

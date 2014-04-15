package edu.clemson.physicaltherapy.app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import edu.clemson.physicaltherapy.R;
import edu.clemson.physicaltherapy.datamodel.Exercise;
import edu.clemson.physicaltherapy.datamodel.ExerciseLog;
import edu.clemson.physicaltherapy.datamodel.ExerciseLogAnnotation;

public class UserVideoView extends VideoViewActivity  {

	private ExerciseLog exerciseLog;
	private ExerciseLogAnnotation current_annotation;
	
	
	
	
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
	protected void addAnnotation(int time, String annotation)
	{
		// Add the annotation to the database
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
	@Override
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
	    	onActionAddAnnotation();
	    	break;
	    	
	    case R.id.action_compare:
			intent = new Intent(this, CompareView.class);
			intent.putExtra("ExerciseLogClass", exerciseLog);
			
			startActivity(intent);
			break;

	    	
	    case R.id.action_settings:
	    	intent = new Intent(this, SettingsActivity.class);
	    	startActivity(intent);
	    	break;
	    	
	    case R.id.action_share:
	    	exportVideo();
	    	break;

	    case R.id.action_delete_all_annotations:
	    	displayDeleteAllDialog();
	    	break;
	    	
	    }
	    return true;
	}


	private void exportVideo() {
		// TODO Auto-generated method stub
		// Get the exercise name
		Exercise exercise;
		try {
			exercise = Exercise.getById(dbSQLite, exerciseLog.getExerciseId());
		
			boolean hasAudioNotes = !exerciseLog.getAudioLocation().equals("");
		
			String message = 
					"Exercise: "+exercise.getName()+"\n"+
					"Exercise instructions: "+exercise.getInstructions()+"\n"+
					"Time Exercise Performed: "+exerciseLog.getCreateTime()+"\n"+
					"Notes: "+exerciseLog.getCreateTime()+"\n"+
					"Audio Notes: "+ hasAudioNotes +"\n";
			
			List<String> filePaths = new ArrayList<String>();
			filePaths.add(exerciseLog.getVideoLocation());
			
			if (hasAudioNotes)
			{
				filePaths.add(exerciseLog.getAudioLocation());
			}
			
			List<ExerciseLogAnnotation> annotationList  = ExerciseLogAnnotation.getAllByExerciseLogId(dbSQLite,exerciseLog.getId());
			
			if (annotationList.size() > 0)
			{
				// Video is between the last '/' and the '.' for the file extension.
				String video_name = exerciseLog.getVideoLocation().substring(exerciseLog.getVideoLocation().lastIndexOf('/')+1, exerciseLog.getVideoLocation().lastIndexOf('.'));
				File annotationsFile = new File(this.getExternalFilesDir("export").getCanonicalPath(), "Annotations-"+video_name+".csv");
				createCSVForAnnotations(annotationsFile, annotationList);			
				filePaths.add(annotationsFile.getAbsolutePath());
			}
			
			LayoutUtils.email(this, null, null, "Clemson Physical Therapy Video", message, filePaths);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	protected String getVideoSubdirectory() {
		
		return "user_videos";
	}
	
	private void createCSVForAnnotations(File outputFile, List<ExerciseLogAnnotation> annotationList) {
		//TODO Read everything that is displayed on screen or hidden
		// Get the Table Layout 
	    
	    // Writing to CSV from http://www.mkyong.com/java/how-to-export-data-to-csv-file-java/
	    FileWriter writer;
		try {
			writer = new FileWriter(outputFile);
		 
		    
		    
		    for (int i = 0; i < annotationList.size(); i++)
		    {
		    	ExerciseLogAnnotation annotation = annotationList.get(i);
		    	writer.append("\"");
		    	
		    	int mSeconds = annotation.getVideoTime();
		    	int hours = mSeconds / 3600000;
		    	mSeconds = mSeconds % 3600000; 
		    	int minutes = mSeconds / 60000;
		    	mSeconds = mSeconds % 60000;
		    	int seconds = mSeconds / 1000;
		    	mSeconds = mSeconds % 1000;
		    	
		    	writer.append(hours+":"+minutes+":"+seconds+"."+mSeconds);
		    	writer.append("\",\"");
		    	writer.append(annotation.getAnnotation());
		    	writer.append("\"\n");
		    }
		    
		    writer.flush();
		    writer.close();
		}
	    catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	    



	@Override
	protected String readAnnotation(int time, int interval) {
		// TODO Auto-generated method stub
		ExerciseLogAnnotation ela =  ExerciseLogAnnotation.getNextAnnotationByTime(dbSQLite, exerciseLog.getId(), time, interval);
		if (ela == null)
		{
			return null;
		}
		return ela.getAnnotation();
	}

	@Override
	public void updateAnnotation(int time, String annotation, int interval) {
		// TODO Auto-generated method stub
		ExerciseLogAnnotation exerciseLogAnnotation = ExerciseLogAnnotation.getPreviousAnnotationByTime(dbSQLite, exerciseLog.getId(), time, interval);
		if (exerciseLogAnnotation == null)
		{
			addAnnotation(time,annotation);
		}
		else
		{
			exerciseLogAnnotation.setAnnotation(annotation);
			exerciseLogAnnotation.setVideoTime(time);
			exerciseLogAnnotation.update(dbSQLite);
		}
		System.err.println("updateAnnotation "+annotation);
		
	}

	@Override
	public void deleteAnnotation(int time, String annotation, int interval) {
		System.err.println("deleteAnnotation "+annotation);
		ExerciseLogAnnotation exerciseLogAnnotation = ExerciseLogAnnotation.getPreviousAnnotationByTime(dbSQLite, exerciseLog.getId(), time, interval);
		if (exerciseLogAnnotation != null)
		{
			exerciseLogAnnotation.delete(dbSQLite);
		}
		
		removeAnnotation();
		
	}

	@Override
	protected void deleteAllAnnotations() {
		ExerciseLogAnnotation.deleteAllByExerciseLogId(dbSQLite,exerciseLog.getId());
	}



}

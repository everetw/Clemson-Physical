package edu.clemson.physicaltherapy.app;



import java.net.MalformedURLException;
import java.net.URL;

import com.example.clemsonphysical.R;
import com.example.clemsonphysical.R.id;
import com.example.clemsonphysical.R.layout;
import com.example.clemsonphysical.R.menu;
import com.example.clemsonphysical.R.xml;

import edu.clemson.physicaltherapy.datamodel.Exercise;
import edu.clemson.physicaltherapy.datamodel.ExerciseLog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore.Video.Thumbnails;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;



public class ExerciseView extends DatabaseActivity {
	
	private TextView exercise_id;
	private TextView exercise_name;
	private TextView exercise_video_url;
	private TextView exercise_instructions;
	private TextView exercise_file_location;
	private WebView exercise_instruction_webview;
	private ImageButton exercise_play_button;
	private ImageView exercise_play_view;
	public Exercise exercise;
	public ExerciseLog exerciseLog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	    int orientation = getResources().getConfiguration().orientation; 
	    if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
	    	setContentView(R.layout.activity_exercise_view_landscape);
	    }
	    else
	    {
	    	setContentView(R.layout.activity_exercise_view);
	    }
	    
	    // Setup the Action Bar
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		// Get the exercise from the intent.
		Intent intent = getIntent();
		
		Exercise exercise_intent = (Exercise)intent.getSerializableExtra("ExerciseClass");
		
		if (exercise_intent != null)
			exercise = exercise_intent;

		
		// Get the widgets
        exercise_id = (TextView) findViewById(R.id.exerciseIdTextView);
        exercise_name = (TextView) findViewById(R.id.exerciseNameTextView);
        exercise_video_url = (TextView) findViewById(R.id.exerciseVideoUrlTextView);
        exercise_instructions = (TextView) findViewById(R.id.exerciseInstructionUrlTextView);
        exercise_instruction_webview = (WebView) findViewById(R.id.exerciseInstructionWebView);
        exercise_file_location = (TextView) findViewById(R.id.exerciseFileLocationTextView);
        exercise_play_button = (ImageButton) findViewById(R.id.exercisePlayImageButton);
        exercise_play_view = (ImageView) findViewById(R.id.exercisePlayImageView);
        

        
        exercise_id.setText(Integer.toString(exercise.getId()));
        exercise_name.setText(exercise.getName());

        exercise_video_url.setText(exercise.getVideoUrl());
        
        // This is messy.
        // If the instructions are a URL, load the file in the webview.
        try
        {
        	URL testUrl = new URL(exercise.getInstructions());
        	exercise_instruction_webview.loadUrl(exercise.getInstructions());
        }
        catch (MalformedURLException mue)
        {
        	// If not, assume text.
        	// Next line should work but doesn't. Appears to be a bug in Android API.
        	// exercise_instruction_webview.loadData(exercise.getInstructions(), "text/plain", "UTF-8");
        	exercise_instruction_webview.loadDataWithBaseURL(null,exercise.getInstructions(), "text/plain", "UTF-8", null);
        }
        
        
        
        exercise_file_location.setText(exercise.getFileLocation());

		//TODO Set the background widget to the thumbnail for the video.
        
        
//	        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) exercise_play_button.getLayoutParams();
//	        layoutParams.height = layoutParams.width;
//	        exercise_play_button.setLayoutParams(layoutParams);
//	        
        // exercise_play_button.getLayoutParams().height = exercise_play_button.getLayoutParams().width;
        
        /// http://android-er.blogspot.com/2011/05/create-thumbnail-for-video-using.html
        // MINI_KIND: 512 x 384 thumbnail 
        exercise_play_view.setImageBitmap(ThumbnailUtils.createVideoThumbnail(exercise.getFileLocation(), Thumbnails.MINI_KIND));
        
        /// http://stackoverflow.com/questions/2415619/how-to-convert-a-bitmap-to-drawable-in-android
        //exercise_play_button.setBackgroundDrawable(new BitmapDrawable(getResources(),bmThumbnail));
        
        //Set the title of the Action Bar to the Exercise Name
        getActionBar().setTitle(exercise.getName());
        
 
	}
	



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exercise_view, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        
 	   
 		case android.R.id.home:

 			NavUtils.navigateUpFromSameTask(this);
 			return true; 
 			
 
        case R.xml.settings:
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            break;
 
        case R.layout.activity_info_view:
            Intent intent = new Intent(this, InfoView.class);
            startActivity(intent);
            break;
        case R.id.activity_show_log:
        	showLog(null);
        	break;
        case R.id.action_add:
        	displayRecordVideoDialog(null);
        	break;
        	
        }
 
        return true;
    }
	
	public void showPractitionerVideo(View view){
		// add stuff to the intent
		Intent intent = new Intent(this, PractitionerVideoView.class);
		intent.putExtra("ExerciseClass", exercise);
		startActivity(intent);
	}
	
	public void showLog(View view){
		Intent intent = new Intent(this, LogView.class);
		intent.putExtra("ExerciseClass", exercise);
		startActivity(intent);
	}


	@Override
	protected String getVideoSubdirectory() {
		// TODO Auto-generated method stub
		return "user_videos";
	}
	
    public void displayRecordVideoDialog(View v)
    {
    	
    	String title = "Record Exercise";
    	String message = "Are you ready to record the "+exercise.getName()+" exercise?";
    			
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// set title

		alertDialogBuilder.setTitle(title);
 
		// set dialog message
		alertDialogBuilder
				.setMessage(message)
				.setCancelable(false)
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
						
						
					}
				  })
				;
		alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dispatchTakeVideoIntent();
				
			}
					
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);

		
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) 
		{
			/// Fixed null pointer exceptions from - http://www.androidhive.info/2013/09/android-working-with-camera-api/
	        try {
	        	
	        	// Capture the log values.
	        	exerciseLog = new ExerciseLog(0,exercise.getId(),mediaUri.getPath(),"");
	        	try {
					exerciseLog.add(dbSQLite);
					// Now get the newly generated id from the database.
					exerciseLog = ExerciseLog.getByVideoLocation(dbSQLite, mediaUri.getPath());
					displayExerciseLogNotesDialog();
				} catch (Exception e) {
					displayMessageDialog("SQLError","Could not save log for "+ exercise.getName() +"\nVideo: "+mediaUri.getPath()+"\n");
					e.printStackTrace();
					
				}
	        	

	        } catch (NullPointerException e) {
	            e.printStackTrace();
	        }
	        
	    }

	}
	
    
    
	/**
	 * Here we store the file url as it will be null after returning from camera
	 * app
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	 
	    // save file url in bundle as it will be null on scren orientation
	    // changes
	    
	    outState.putSerializable("exercise", exercise);
	    outState.putSerializable("exercise_log", exerciseLog);
	    
	    
	}
	 
	/*
	 * Here we restore the fileUri again
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	 
	    // get the file url
	    //System.err.println("restoring instance state");
	    
	    exercise = (Exercise) savedInstanceState.getSerializable("exercise");
	    exerciseLog = (ExerciseLog) savedInstanceState.getSerializable("exercise_log");
	    
	}
	
	
	public void displayExerciseLogNotesDialog() {
		
    	String title = "Add Exercise Log Notes?";
    	

		
		// get prompts.xml view
		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = li.inflate(R.layout.exercise_notes_dialog, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		alertDialogBuilder.setTitle(title);
		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		final EditText exerciseLogNotesInput = (EditText) promptsView
				.findViewById(R.id.exerciseLogNotesEditText);
		
		

		// set dialog message
		alertDialogBuilder
			.setCancelable(false)
			.setPositiveButton("OK",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
					// get user input and set it to result
					exerciseLog.setVideoNotes(exerciseLogNotesInput.getText().toString());
					
					try {
						// Update the database.
						exerciseLog.update(dbSQLite);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			
				
			    }
			  })
			.setNegativeButton("Cancel",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {					

				dialog.cancel();
			    }
			  });

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}
	

	@Override
	protected void onNewIntent(Intent intent)
	{
	    super.onNewIntent(intent);
	    System.err.println("onNewIntent");
		Exercise exercise_intent = (Exercise)intent.getSerializableExtra("ExerciseClass");
		
		if (exercise_intent != null)
			exercise = exercise_intent;

	
	
	}  

	

}

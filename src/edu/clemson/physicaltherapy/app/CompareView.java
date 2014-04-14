package edu.clemson.physicaltherapy.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import edu.clemson.physicaltherapy.R;
import edu.clemson.physicaltherapy.datamodel.Exercise;
import edu.clemson.physicaltherapy.datamodel.ExerciseLog;

public class CompareView extends DatabaseActivity {
	
	private VideoView exerciseVideoView;
	private VideoView exerciseLogVideoView;
	private TextView exerciseAnnotationTextView;
	private TextView exerciseLogAnnotationTextView;
	private ExerciseLog exerciseLog;
	private Exercise exercise;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		int orientation = getResources().getConfiguration().orientation; 
		if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
			setContentView(R.layout.activity_compare_view_landscape);
		}
		else
		{
			setContentView(R.layout.activity_compare_view);
		}
		
		exerciseVideoView = (VideoView) findViewById(R.id.videoView1);	
		//Add the media controller.
		MediaController exerciseMediaController = new MediaController(this);
		
		// Display controls at the bottom of the VideoView.
		/// http://stackoverflow.com/questions/3686729/mediacontroller-positioning-over-videoview
		exerciseMediaController.setAnchorView(exerciseVideoView);
		exerciseVideoView.setMediaController(exerciseMediaController);
		
		exerciseAnnotationTextView = (TextView)findViewById(R.id.textView1);
		Intent intent = getIntent();
		exerciseLog = (ExerciseLog)intent.getSerializableExtra("ExerciseLogClass");
		
		try {
			exercise = (Exercise) Exercise.getById(dbSQLite,exerciseLog.getExerciseId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Set the title of the Action Bar to the Exercise Name
		getActionBar().setTitle("Compare: "+exercise.getName());
		
		exerciseVideoView.setVideoPath(exercise.getFileLocation());
		//displayToast(exerciseLog.getVideoLocation());
		
		exerciseLogVideoView = (VideoView) findViewById(R.id.videoView2);	
		
		//Add the media controller.
		MediaController exerciseLogMediaController = new MediaController(this);

		// Display controls at the bottom of the VideoView.
		/// http://stackoverflow.com/questions/3686729/mediacontroller-positioning-over-videoview
		exerciseLogMediaController.setAnchorView(exerciseLogVideoView);
		exerciseLogVideoView.setMediaController(exerciseLogMediaController);
		
		exerciseLogAnnotationTextView = (TextView)findViewById(R.id.textView2);
		
		exerciseLogVideoView.setVideoPath(exerciseLog.getVideoLocation());
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compare_view, menu);
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
        }
 
        return true;
    }

}

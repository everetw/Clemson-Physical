package edu.clemson.physicaltherapy.app;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import edu.clemson.physicaltherapy.R;



/// Fix surface has been released problem https://androidproblem.wordpress.com/category/mediaplayer/

public abstract class VideoViewActivity extends DatabaseActivity implements MediaController.MediaPlayerControl {


	
	private VideoView videoView;
	private TextView annotationTextView;
	private Handler videoHandler = new Handler();
	private int annotationCount = 0;
	
	
	private static int POLL_INTERVAL = 500;
	private static int DISPLAY_TIME = 3000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView();
		
		
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
		
		
	}
	
	protected abstract void setContentView();
	

	public abstract void addAnnotation(int time, String annotation);
	public abstract String readAnnotation(int time, int interval);
	
	public void displayAnnotation(String annotation)
	{
        annotationTextView.setText(annotation);
        annotationTextView.setVisibility(View.VISIBLE);
        //displayToast(annotation);
		
	}
	
	public void removeAnnotation(String annotation)
	{
        annotationTextView.setVisibility(View.GONE);
		
	}
	
	
		
	///Repeat a task with a time delay using handlers - http://stackoverflow.com/questions/6242268/repeat-a-task-with-a-time-delay/
	/// More on handlers. http://www.tutorialspoint.com/android/android_mediaplayer.htm
	private Runnable checkAnnotations = new Runnable() {
	      public void run() {
	    	 
	    	 // if the videoView isn't playing, return.
	    	 // This coding is shameful. 
	    	 // System.err.println("Checking annotations");
	    	 if (!videoView.isPlaying())
	    	 {
	    		 videoHandler.postDelayed(this, POLL_INTERVAL);
	    		 return;
	    	 }
	    	 
	    	 // Find the current time
	         int currentTime = videoView.getCurrentPosition();
	         
	         // Calculate the time of the next check. 
	         int nextTime = currentTime+POLL_INTERVAL;
	        
	         // grab the next annotation from the database.
	         String annotation = readAnnotation(currentTime,POLL_INTERVAL);
	         if (annotation == null)
	         {
	        	 if (annotationCount < DISPLAY_TIME)
	        	 {
	        		 annotationCount+=POLL_INTERVAL;
	        	 }
	        	 else
	        	 {
	        		 displayAnnotation("");
	        		 annotationCount=0;
	        	 }
	         }
	         else
	         {
	        	 displayAnnotation(annotation);
	         }
		     
	         // Call again in POLL_INTERVAL ms.
	         videoHandler.postDelayed(this, POLL_INTERVAL);
	      }
	   };

	   
		public void displayAnnotationDialog() {
			
	    	String title = "Add Annotation?";
	    	
	    	// Pause the video
	    	videoView.pause();
			
			// get prompts.xml view
			LayoutInflater li = LayoutInflater.from(this);
			View promptsView = li.inflate(R.layout.annotation_dialog, null);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);

			alertDialogBuilder.setTitle(title);
			// set prompts.xml to alertdialog builder
			alertDialogBuilder.setView(promptsView);

			final EditText annotationInput = (EditText) promptsView
					.findViewById(R.id.annotationEditText);
			
			

			// set dialog message
			alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK",
				  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,int id) {
						// get user input and set it to result
						addAnnotation(getCurrentPosition(),annotationInput.getText().toString());
						// Continue the video.
						//videoView.start();
					
				    }
				  })
				.setNegativeButton("Cancel",
				  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,int id) {
				    // Continue the video
				    //videoView.start();
					dialog.cancel();
				    }
				  });

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();

		}
	   

	/// Do not poll when activity is in background.
	@Override
	protected void onPause()
	{
		//System.err.println("On pause");
		super.onPause();
		videoHandler.removeCallbacks(checkAnnotations);
	}
	
	/// Resume polling when activity is resumed.	
	@Override
	protected void onResume()
	{
		//System.err.println("On resume");
		super.onResume();
		videoHandler.postDelayed(checkAnnotations, 0);
	}
	
	public void setVideoPath(String path)
	{
		videoView.setVideoPath(path);
	}
	
	/// Methods for interface MediaController.MediaPlayer. These are simply wrappers for the VideoView.
	

	@Override
	public boolean canPause() {
		
		return videoView.canPause();
	}


	@Override
	public boolean canSeekBackward() {

		return videoView.canSeekBackward();
	}


	@Override
	public boolean canSeekForward() {

		return videoView.canSeekForward();
	}


	@Override
	public int getAudioSessionId() {
		
		return videoView.getAudioSessionId();
	}


	@Override
	public int getBufferPercentage() {

		return videoView.getBufferPercentage();
	}


	@Override
	public int getCurrentPosition() {

		return videoView.getCurrentPosition();
	}


	@Override
	public int getDuration() {
		return videoView.getDuration();
		
	}


	@Override
	public boolean isPlaying() {
		return videoView.isPlaying();
	}


	@Override
	public void pause() {
		videoView.pause();
		
	}


	@Override
	public void seekTo(int msec) {
		videoView.seekTo(msec);
		
	}


	@Override
	public void start() {

		videoView.start();
		
	}
	


	


	
	
}
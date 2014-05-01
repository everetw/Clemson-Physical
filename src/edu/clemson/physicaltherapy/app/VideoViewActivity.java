package edu.clemson.physicaltherapy.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import edu.clemson.physicaltherapy.R;

/// Fix surface has been released problem https://androidproblem.wordpress.com/category/mediaplayer/

public abstract class VideoViewActivity extends DatabaseActivity implements
		MediaController.MediaPlayerControl, MediaPlayer.OnCompletionListener
{

	public static String	VIDEO_TIME		= "video_time";
	private VideoView		videoView;
	private TextView		annotationTextView;
	private Handler			videoHandler	= new Handler();
	private int				annotationCount	= 0;
	private ImageButton		annotationDeleteButton;

	private static int		POLL_INTERVAL	= 250;				// Check every
																// 250ms
	private static int		DISPLAY_TIME	= 3000;			// Display
																// annotations
																// for 3
																// seconds.

	protected static int	DELETE_ALL		= 0;
	protected static int	DELETE_CURRENT	= 1;

	/**
	 * @fn protected void onCreate(Bundle savedInstanceState)
	 * @brief Method called when activity is created.
	 * 
	 * @param savedInstanceState
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView();

		// Setup the Action Bar
		try
		{
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			{
				getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		catch (java.lang.NullPointerException npe)
		{

		}

		// / Information about VideoView
		// http://www.techotopia.com/index.php/Implementing_Video_Playback_on_Android_using_the_VideoView_and_MediaController_Classes
		videoView = (VideoView) findViewById(R.id.videoView1);

		// Add the media controller.
		MediaController mediaController = new MediaController(this);

		// Display controls at the bottom of the VideoView.
		// /
		// http://stackoverflow.com/questions/3686729/mediacontroller-positioning-over-videoview
		mediaController.setAnchorView(videoView);
		videoView.setMediaController(mediaController);

		videoView.setOnCompletionListener(this);

		// TextView that holds the annotations.
		annotationTextView = (TextView) findViewById(R.id.textView1);

		// Add an on Focus Change Listener so that
		// If the focus is gained, it pauses the video to allow editing.
		// If the focus is lost, the annotation is updated.
		// annotationTextView.setOnFocusChangeListener(new
		// View.OnFocusChangeListener() {
		//
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// if (hasFocus)
		// {
		// pause();
		// }
		// else
		// {
		// // InputMethodManager imm =
		// (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		// // imm.hideSoftInputFromWindow(annotationTextView.getWindowToken(),
		// 0);
		// //
		// String newAnnotation = annotationTextView.getText().toString();
		// int currentTime = getCurrentPosition();
		// //
		// addAnnotation(getCurrentPosition(),annotationTextView.getText().toString());
		// updateAnnotation(currentTime,newAnnotation);
		// }
		// }
		// });
		// Grab the delete button.
		annotationDeleteButton = (ImageButton) findViewById(R.id.imageButton1);

	}

	protected void resumeVideo(int videoTime)
	{
		// Restore value of members from saved state
		seekTo(videoTime);
		start();
	}

	protected abstract void setContentView();

	protected abstract void addAnnotation(int time, String annotation);

	protected abstract void deleteAnnotation(int time, String annotation,
			int interval);

	protected abstract void updateAnnotation(int time, String annotation,
			int interval);

	protected abstract String readAnnotation(int time, int interval);

	protected abstract void deleteAllAnnotations();

	public void onDeleteAnnotationButtonClicked(View v)
	{

		pause();
		displayDeleteDialog(DELETE_CURRENT);

	}

	protected void displayAnnotation(String annotation)
	{
		annotationTextView.setText(annotation);
		annotationTextView.setVisibility(View.VISIBLE);
		annotationDeleteButton.setVisibility(View.VISIBLE);
		annotationDeleteButton.setClickable(true);

	}

	protected void removeAnnotation()
	{
		annotationTextView.setText("");
		annotationTextView.setVisibility(View.GONE);
		annotationDeleteButton.setVisibility(View.GONE);

	}

	@Override
	public void onCompletion(MediaPlayer mp)
	{
		// Remove the annotation when the video is completed.
		removeAnnotation();
	}

	// /Repeat a task with a time delay using handlers -
	// http://stackoverflow.com/questions/6242268/repeat-a-task-with-a-time-delay/
	// / More on handlers.
	// http://www.tutorialspoint.com/android/android_mediaplayer.htm
	protected Runnable	checkAnnotations	= new Runnable()
											{
												public void run()
												{

													// if the videoView isn't
													// playing, return.
													// This coding is shameful.
													// System.err.println("Checking annotations");
													// https://blog.trifork.com/2011/08/12/using-android-preferences-in-a-background-service/
													if (!isPlaying())
													{
														videoHandler.postDelayed(this, POLL_INTERVAL);
														return;
													}

													// Find the current time
													int currentTime = getCurrentPosition();

													// Calculate the time of the
													// next check.
													int nextTime = currentTime
															+ POLL_INTERVAL;

													// grab the next annotation
													// from the database.
													String annotation = readAnnotation(currentTime, POLL_INTERVAL);
													if (annotation == null)
													{
														if (annotationCount < DISPLAY_TIME)
														{
															annotationCount += POLL_INTERVAL;
														}
														else
														{
															removeAnnotation();
															annotationCount = 0;
														}
													}
													else
													{
														displayAnnotation(annotation);
														annotationCount = 0;
													}

													// Call again in
													// POLL_INTERVAL ms.
													videoHandler.postDelayed(this, POLL_INTERVAL);
												}
											};

	protected void onActionAddAnnotation()
	{
		pause();
		displayAnnotationDialog();

	}

	protected void onActionDeleteAll()
	{
		pause();
		displayDeleteDialog(DELETE_ALL);
	}

	protected void displayDeleteDialog(final int delete_type)
	{

		String title = "Confirm Delete!";

		String message;

		if (delete_type == DELETE_ALL)
		{
			message = "Are you sure you want to delete all annotations for this video?";
		}
		else
		{
			message = "Are you sure you want to delete this annotation?";
		}

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setTitle(title);

		alertDialogBuilder.setMessage(message);
		// set dialog message
		alertDialogBuilder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				// get user input and set it to result
				if (delete_type == DELETE_ALL)
				{
					deleteAllAnnotations();
				}
				else
				{
					String annotation = annotationTextView.getText().toString();
					int currentTime = getCurrentPosition();
					// System.err.println("Delete button clicked at "+currentTime);
					deleteAnnotation(currentTime, annotation, DISPLAY_TIME
							+ POLL_INTERVAL);

				}
				// Remove the current annotation from view, if there is
				// one.
				removeAnnotation();

			}
		}).setNegativeButton("No", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				// Continue the video
				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	protected void displayAnnotationDialog()
	{

		String title = "Update Annotation?";

		final String currentAnnotation = annotationTextView.getText().toString();
		if (currentAnnotation.equals(""))
		{
			title = "Add Annotation?";
		}
		// get prompts.xml view
		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = li.inflate(R.layout.annotation_dialog, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setTitle(title);
		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		final EditText annotationInput = (EditText) promptsView.findViewById(R.id.annotationEditText);

		annotationInput.setText(currentAnnotation);

		// set dialog message
		alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				// get user input and set it to result
				// System.err.println("current annotation is:"+currentAnnotation);
				if (currentAnnotation.equals(""))
				{
					addAnnotation(getCurrentPosition(), annotationInput.getText().toString());
				}
				else
				{
					updateAnnotation(getCurrentPosition(), annotationInput.getText().toString(), DISPLAY_TIME
							+ POLL_INTERVAL);
				}

				displayAnnotation(annotationInput.getText().toString());

			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				// Continue the video
				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	// / Do not poll when activity is in background.
	@Override
	protected void onPause()
	{
		// System.err.println("On pause");
		super.onPause();
		videoHandler.removeCallbacks(checkAnnotations);

	}

	// / Resume polling when activity is resumed.
	@Override
	protected void onResume()
	{
		// System.err.println("On resume");
		super.onResume();

		// If we are showing annotations, start the poller.
		if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsActivity.KEY_ANNOTATIONS, true))
		{
			videoHandler.postDelayed(checkAnnotations, 0);
		}

	}

	protected void setVideoPath(String path)
	{
		videoView.setVideoPath(path);
	}

	// / Methods for interface MediaController.MediaPlayer. These are simply
	// wrappers for the VideoView.

	@Override
	public boolean canPause()
	{

		return videoView.canPause();
	}

	@Override
	public boolean canSeekBackward()
	{

		return videoView.canSeekBackward();
	}

	@Override
	public boolean canSeekForward()
	{

		return videoView.canSeekForward();
	}

	@Override
	public int getAudioSessionId()
	{

		return videoView.getAudioSessionId();
	}

	@Override
	public int getBufferPercentage()
	{

		return videoView.getBufferPercentage();
	}

	@Override
	public int getCurrentPosition()
	{

		return videoView.getCurrentPosition();
	}

	@Override
	public int getDuration()
	{
		return videoView.getDuration();

	}

	@Override
	public boolean isPlaying()
	{
		return videoView.isPlaying();
	}

	@Override
	public void pause()
	{
		videoView.pause();

	}

	@Override
	public void seekTo(int msec)
	{
		videoView.seekTo(msec);

	}

	@Override
	public void start()
	{

		videoView.start();
	}

	protected void setMediaController(MediaController controller)
	{
		videoView.setMediaController(controller);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putInt(VIDEO_TIME, getCurrentPosition());
		super.onSaveInstanceState(outState);
	}

}
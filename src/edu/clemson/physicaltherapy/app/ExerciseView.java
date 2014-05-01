package edu.clemson.physicaltherapy.app;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import edu.clemson.physicaltherapy.R;
import edu.clemson.physicaltherapy.datamodel.Exercise;
import edu.clemson.physicaltherapy.datamodel.ExerciseAnnotation;
import edu.clemson.physicaltherapy.datamodel.ExerciseLog;

public class ExerciseView extends VideoViewActivity
{

	private TextView	exercise_id;
	private TextView	exercise_name;
	private TextView	exercise_video_url;
	private TextView	exercise_instructions;
	private TextView	exercise_file_location;
	private WebView		exercise_instruction_webview;
	private ImageButton	exercise_play_button;
	private ImageView	exercise_play_view;

	private Exercise	exercise;
	private ExerciseLog	exerciseLog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		// Get the exercise from the intent.
		Intent intent = getIntent();

		Exercise exercise_intent = (Exercise) intent.getSerializableExtra("ExerciseClass");

		if (exercise_intent != null)
			exercise = exercise_intent;

		// Get the widgets
		exercise_id = (TextView) findViewById(R.id.exerciseIdTextView);
		exercise_name = (TextView) findViewById(R.id.exerciseNameTextView);
		exercise_video_url = (TextView) findViewById(R.id.exerciseVideoUrlTextView);
		exercise_instructions = (EditText) findViewById(R.id.exerciseInstructionUrlTextView);
		exercise_instruction_webview = (WebView) findViewById(R.id.exerciseInstructionWebView);
		// exercise_play_button = (ImageButton)
		// findViewById(R.id.exercisePlayImageButton);
		exercise_file_location = (TextView) findViewById(R.id.exerciseFileLocationTextView);
		// exercise_play_view = (ImageView)
		// findViewById(R.id.exercisePlayImageView);

		// Set the fields
		exercise_id.setText(Integer.toString(exercise.getId()));
		exercise_name.setText(exercise.getName());
		exercise_video_url.setText(exercise.getVideoUrl());

		// This is messy.
		// If the instructions are a URL, load the file in the webview.

		exercise_instructions.setText(exercise.getInstructions());
		try
		{
			URL testUrl = new URL(exercise.getInstructions());

			// Load pdf via Google Viewer
			// http://kylewbanks.com/blog/Loading-PDF-in-Android-WebView
			int pdf = exercise.getInstructions().toLowerCase().indexOf(".pdf");

			if (pdf != -1)
			{
				exercise_instruction_webview.getSettings().setJavaScriptEnabled(true);
				exercise_instruction_webview.loadUrl("http://docs.google.com/gview?embedded=true&url="
						+ testUrl.toString());
			}
			else
			{
				exercise_instruction_webview.getSettings().setJavaScriptEnabled(false);
				exercise_instruction_webview.loadUrl(exercise.getInstructions());
			}
		}
		catch (MalformedURLException mue)
		{
			// If not, assume text.
			// Next line should work but doesn't. Appears to be a bug in Android
			// API.
			// exercise_instruction_webview.loadData(exercise.getInstructions(),
			// "text/plain", "UTF-8");
			// So we include a null base url.
			exercise_instruction_webview.getSettings().setJavaScriptEnabled(false);
			exercise_instruction_webview.loadDataWithBaseURL(null, exercise.getInstructions(), "text/plain", "UTF-8", null);
		}

		// Keep webview from automatically redirecting.
		// /
		// http://stackoverflow.com/questions/4066438/android-webview-how-to-handle-redirects-in-app-instead-of-opening-a-browser
		exercise_instruction_webview.setWebViewClient(new WebViewClient()
		{
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				return false;
			}
		});

		exercise_file_location.setText(exercise.getFileLocation());

		// /
		// http://android-er.blogspot.com/2011/05/create-thumbnail-for-video-using.html
		// MINI_KIND: 512 x 384 thumbnail
		// exercise_play_view.setImageBitmap(ThumbnailUtils.createVideoThumbnail(exercise.getFileLocation(),
		// Thumbnails.MINI_KIND));

		// Set the title of the Action Bar to the Exercise Name
		getActionBar().setTitle(exercise.getName());

		String filePath = "./";

		try
		{
			filePath = this.getExternalFilesDir(getVideoSubdirectory()).getCanonicalPath()
					+ "/";
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int videoTime = intent.getIntExtra(VIDEO_TIME, 0);
		if (savedInstanceState != null)
		{
			videoTime = savedInstanceState.getInt(VIDEO_TIME, 0);
		}

		System.err.println("Playing exercise id " + exercise.getId());
		boolean playLocally = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsActivity.KEY_DOWNLOAD, false);
		if (exercise.getFileLocation().equals("online video") || !playLocally)
		{
			System.err.println("Playing remote" + exercise.getVideoUrl());
			setVideoPath(exercise.getVideoUrl());
			resumeVideo(videoTime);
		}
		else
		{

			System.err.println("Playing local " + exercise.getFileLocation());
			setVideoPath(exercise.getFileLocation());
			resumeVideo(videoTime);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		// /
		// http://stackoverflow.com/questions/7133141/android-changing-option-menu-items-programmatically
		getMenuInflater().inflate(R.menu.exercise_view, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{

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

			case R.id.action_fullscreen:
				showPractitionerVideo(null);
				break;

		}

		return true;
	}

	public void showPractitionerVideo(View view)
	{
		// add stuff to the intent
		Intent intent = new Intent(this, PractitionerVideoView.class);
		intent.putExtra("ExerciseClass", exercise);
		intent.putExtra(VIDEO_TIME, getCurrentPosition());
		startActivity(intent);
	}

	public void showLog(View view)
	{
		Intent intent = new Intent(this, LogView.class);
		intent.putExtra(VIDEO_TIME, exercise);
		startActivity(intent);
	}

	@Override
	protected String getVideoSubdirectory()
	{
		// TODO Auto-generated method stub
		return "user_videos";
	}

	public void displayRecordVideoDialog(View v)
	{

		String title = "Record Exercise";
		String message = "Are you ready to record the " + exercise.getName()
				+ " exercise?";

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// set title

		alertDialogBuilder.setTitle(title);

		// set dialog message
		alertDialogBuilder.setMessage(message).setCancelable(false).setNegativeButton("No", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				// if this button is clicked, just close
				// the dialog box and do nothing
				dialog.cancel();

			}
		});
		alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.cancel();
				dispatchTakeVideoIntent();

			}

		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK)
		{
			// / Fixed null pointer exceptions from -
			// http://www.androidhive.info/2013/09/android-working-with-camera-api/
			try
			{

				// Capture the log values.
				exerciseLog = new ExerciseLog(0, exercise.getId(), mediaUri.getPath());
				try
				{
					exerciseLog.add(dbSQLite);
					// Now get the newly generated id from the database.
					exerciseLog = ExerciseLog.getByVideoLocation(dbSQLite, mediaUri.getPath());
					displayExerciseLogNotesDialog();
				}
				catch (Exception e)
				{
					displayMessageDialog("SQLError", "Could not save log for "
							+ exercise.getName() + "\nVideo: "
							+ mediaUri.getPath() + "\n");
					e.printStackTrace();

				}

			}
			catch (NullPointerException e)
			{
				e.printStackTrace();
			}

		}

	}

	/**
	 * Here we store the file url as it will be null after returning from camera
	 * app
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{

		// save file url in bundle as it will be null on scren orientation
		// changes

		outState.putSerializable("exercise", exercise);
		outState.putSerializable("exercise_log", exerciseLog);

		outState.putInt("video_time", getCurrentPosition());

		super.onSaveInstanceState(outState);

	}

	/*
	 * Here we restore the fileUri again
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		exercise = (Exercise) savedInstanceState.getSerializable("exercise");
		exerciseLog = (ExerciseLog) savedInstanceState.getSerializable("exercise_log");

	}

	public void displayExerciseLogNotesDialog()
	{

		String title = "Add Exercise Log Notes?";

		// get prompts.xml view
		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = li.inflate(R.layout.exercise_notes_dialog, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setTitle(title);
		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		final EditText exerciseLogNotesInput = (EditText) promptsView.findViewById(R.id.exerciseLogNotesEditText);

		// set dialog message
		alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				// get user input and set it to result
				exerciseLog.setVideoNotes(exerciseLogNotesInput.getText().toString());

				try
				{
					// Update the database.
					exerciseLog.update(dbSQLite);

				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{

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
		// System.err.println("onNewIntent");
		Exercise exercise_intent = (Exercise) intent.getSerializableExtra("ExerciseClass");

		if (exercise_intent != null)
			exercise = exercise_intent;

	}

	@Override
	protected void setContentView()
	{
		int orientation = getResources().getConfiguration().orientation;
		if (Configuration.ORIENTATION_LANDSCAPE == orientation)
		{
			setContentView(R.layout.activity_exercise_view_landscape);
		}
		else
		{
			setContentView(R.layout.activity_exercise_view);
		}

	}

	@Override
	protected void addAnnotation(int time, String annotation)
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void deleteAnnotation(int time, String annotation, int interval)
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateAnnotation(int time, String annotation, int interval)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String readAnnotation(int time, int interval)
	{
		// TODO Auto-generated method stub
		ExerciseAnnotation ela = ExerciseAnnotation.getNextAnnotationByTime(dbSQLite, exercise.getId(), time, interval);
		if (ela == null)
		{
			return null;
		}
		return ela.getAnnotation();
	}

	@Override
	protected void deleteAllAnnotations()
	{
		// TODO Auto-generated method stub

	}

}

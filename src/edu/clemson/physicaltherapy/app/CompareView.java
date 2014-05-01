package edu.clemson.physicaltherapy.app;

import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import edu.clemson.physicaltherapy.R;
import edu.clemson.physicaltherapy.datamodel.Exercise;
import edu.clemson.physicaltherapy.datamodel.ExerciseLog;
import edu.clemson.physicaltherapy.web.HTTPDownloader;

public class CompareView extends DatabaseActivity
{

	private VideoView	exerciseVideoView;
	private VideoView	exerciseLogVideoView;
	private TextView	exerciseAnnotationTextView;
	private TextView	exerciseLogAnnotationTextView;
	private ExerciseLog	exerciseLog;
	private Exercise	exercise;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		int orientation = getResources().getConfiguration().orientation;
		if (Configuration.ORIENTATION_LANDSCAPE == orientation)
		{
			setContentView(R.layout.activity_compare_view_landscape);
		}
		else
		{
			setContentView(R.layout.activity_compare_view);
		}

		exerciseVideoView = (VideoView) findViewById(R.id.videoView1);
		// Add the media controller.
		MediaController exerciseMediaController = new MediaController(this);

		// Display controls at the bottom of the VideoView.
		// /
		// http://stackoverflow.com/questions/3686729/mediacontroller-positioning-over-videoview
		exerciseMediaController.setAnchorView(exerciseVideoView);
		exerciseVideoView.setMediaController(exerciseMediaController);

		exerciseAnnotationTextView = (TextView) findViewById(R.id.textView1);
		Intent intent = getIntent();
		exerciseLog = (ExerciseLog) intent.getSerializableExtra("ExerciseLogClass");

		try
		{
			exercise = (Exercise) Exercise.getById(dbSQLite, exerciseLog.getExerciseId());
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Set the title of the Action Bar to the Exercise Name
		getActionBar().setTitle("Compare: " + exercise.getName());

		// displayToast(exerciseLog.getVideoLocation());

		int exerciseLogVideoTime = 0;
		int exerciseVideoTime = 0;

		exerciseLogVideoView = (VideoView) findViewById(R.id.videoView2);

		// Add the media controller.
		MediaController exerciseLogMediaController = new MediaController(this);

		// Display controls at the bottom of the VideoView.
		// /
		// http://stackoverflow.com/questions/3686729/mediacontroller-positioning-over-videoview
		exerciseLogMediaController.setAnchorView(exerciseLogVideoView);
		exerciseLogVideoView.setMediaController(exerciseLogMediaController);
		exerciseLogVideoView.setVideoPath(exerciseLog.getVideoLocation());

		exerciseLogAnnotationTextView = (TextView) findViewById(R.id.textView2);

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

		boolean playLocally = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsActivity.KEY_DOWNLOAD, false);

		// If we are playing locally or this is a local video, play locally.
		if (playLocally == true || exercise.getVideoUrl().equals(""))
		{
			// if we don't have the video, download it.
			if (exercise.getFileLocation().equals("online video"))
			{
				DownloadSingle task = new DownloadSingle();
				task.execute(exercise.getVideoUrl(), filePath);

			}
			else
			{
				System.err.println("Playing local "
						+ exercise.getFileLocation());
				exerciseVideoView.setVideoPath(exercise.getFileLocation());

			}
		}
		else
		{
			System.err.println("Playing remote" + exercise.getVideoUrl());
			exerciseVideoView.setVideoPath(exercise.getVideoUrl());

		}

		// If we are recreating the activity, restart the video.
		if (savedInstanceState != null)
		{
			exerciseLogVideoTime = savedInstanceState.getInt("log_video_time");
			exerciseVideoTime = savedInstanceState.getInt("exercise_video_time");
			exerciseLogVideoView.seekTo(exerciseLogVideoTime);
			exerciseVideoView.seekTo(exerciseVideoTime);
			exerciseLogVideoView.start();
			exerciseVideoView.start();

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compare_view, menu);
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
		}

		return true;
	}

	class DownloadSingle extends AsyncTask<String, String, String>
	{

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			if (pDialog == null)
				pDialog = new ProgressDialog(CompareView.this);
			pDialog.setMessage("Downloading video files from remote servers. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Getting product details in background thread
		 * */
		protected String doInBackground(String... args)
		{

			String videoUrl = args[0];

			String filepath = args[1];

			String url = HTTPDownloader.getDirectUrl(videoUrl);
			String filename = HTTPDownloader.getLocalFileNameFromUrl(videoUrl);
			System.err.println("Attempting to download " + filepath + filename
					+ " from " + url);
			boolean success = HTTPDownloader.downloadFile(url, filepath
					+ filename);
			// If we successfully downloaded the file, return the filename.
			if (success)
			{
				return filepath + filename;
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			// dismiss the dialog once done
			pDialog.dismiss();

			// update the exercise with the result and play it.
			if (result != null)
			{
				updateFileLocation(result);
			}

		}
	}

	private void updateFileLocation(String result)
	{
		exercise.setFileLocation(result);
		exercise.update(dbSQLite);
		exerciseVideoView.setVideoPath(exercise.getFileLocation());

	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putInt("exercise_video_time", exerciseVideoView.getCurrentPosition());
		outState.putInt("log_video_time", exerciseLogVideoView.getCurrentPosition());
		super.onSaveInstanceState(outState);
	}

}

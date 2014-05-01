package edu.clemson.physicaltherapy.app;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import edu.clemson.physicaltherapy.R;
import edu.clemson.physicaltherapy.datamodel.Exercise;
import edu.clemson.physicaltherapy.datamodel.ExerciseAnnotation;
import edu.clemson.physicaltherapy.web.HTTPDownloader;

public class PractitionerVideoView extends VideoViewActivity
{

	private Exercise			exercise;
	private ExerciseAnnotation	current_annotation;
	private boolean				playLocally	= true;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		exercise = (Exercise) intent.getSerializableExtra("ExerciseClass");
		int videoTime = intent.getIntExtra(VIDEO_TIME, 0);
		if (savedInstanceState != null)
		{
			videoTime = savedInstanceState.getInt(VIDEO_TIME, 0);
		}

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

		playLocally = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsActivity.KEY_DOWNLOAD, false);

		System.err.println("Playing exercise id " + exercise.getId());
		// If we are playing locally or this is a local video, play locally.
		if (playLocally == true || exercise.getExternalId() < 0)
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
				setVideoPath(exercise.getFileLocation());
				resumeVideo(videoTime);
			}
		}
		else
		{
			System.err.println("Playing remote" + exercise.getVideoUrl());
			setVideoPath(exercise.getVideoUrl());
			resumeVideo(videoTime);
		}

	}

	private void updateFileLocationAndStart(String result)
	{
		exercise.setFileLocation(result);
		exercise.update(dbSQLite);
		setVideoPath(exercise.getFileLocation());
		// if we have just downloaded the video, restart it.
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
		// displayAnnotation(time+": "+annotation);
		ExerciseAnnotation ea = new ExerciseAnnotation(0, exercise.getId(), time, annotation);
		try
		{
			ea.add(dbSQLite);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected String getVideoSubdirectory()
	{

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
	public boolean onCreateOptionsMenu(Menu menu)
	{

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
	public boolean onOptionsItemSelected(MenuItem item)
	{

		Intent intent = null;
		// / Menu from
		// http://developer.android.com/guide/topics/ui/menus.html#options-menu
		switch (item.getItemId())
		{

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
				displayDeleteDialog(DELETE_ALL);
				break;

		}
		return true;
	}

	public void showLog(View view)
	{
		Intent intent = new Intent(this, LogView.class);
		intent.putExtra("ExerciseClass", exercise);
		startActivity(intent);
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
	public void updateAnnotation(int time, String annotation, int interval)
	{
		// TODO Auto-generated method stub
		ExerciseAnnotation exerciseAnnotation = ExerciseAnnotation.getPreviousAnnotationByTime(dbSQLite, exercise.getId(), time, interval);
		if (exerciseAnnotation == null)
		{
			addAnnotation(time, annotation);
		}
		else
		{
			exerciseAnnotation.setAnnotation(annotation);
			// Do not update video time on annotation. Prevents
			// "annotation creep".
			// exerciseAnnotation.setVideoTime(time);
			exerciseAnnotation.update(dbSQLite);
		}
		// System.err.println("updateAnnotation "+annotation);

	}

	@Override
	public void deleteAnnotation(int time, String annotation, int interval)
	{
		// TODO Auto-generated method stub
		// System.err.println("deleteAnnotation "+annotation);
		ExerciseAnnotation exerciseAnnotation = ExerciseAnnotation.getPreviousAnnotationByTime(dbSQLite, exercise.getId(), time, interval);
		if (exerciseAnnotation != null)
		{
			exerciseAnnotation.delete(dbSQLite);
		}

		removeAnnotation();

	}

	@Override
	protected void deleteAllAnnotations()
	{
		// TODO Auto-generated method stub
		ExerciseAnnotation.deleteAllByExerciseId(dbSQLite, exercise.getId());

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
				pDialog = new ProgressDialog(PractitionerVideoView.this);
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
				updateFileLocationAndStart(result);
			}

		}
	}

}

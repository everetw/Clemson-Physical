package edu.clemson.physicaltherapy.app;

import java.net.MalformedURLException;
import java.net.URL;

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
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import edu.clemson.physicaltherapy.R;
import edu.clemson.physicaltherapy.datamodel.Exercise;
import edu.clemson.physicaltherapy.datamodel.ExerciseLog;

public class ExerciseEditView extends DatabaseActivity
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
	private boolean		editMode	= false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		int orientation = getResources().getConfiguration().orientation;
		if (Configuration.ORIENTATION_LANDSCAPE == orientation)
		{
			setContentView(R.layout.activity_exercise_edit_view_landscape);
		}
		else
		{
			setContentView(R.layout.activity_exercise_edit_view);
		}

		// Setup the Action Bar
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}

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
		exercise_play_button = (ImageButton) findViewById(R.id.exercisePlayImageButton);
		exercise_file_location = (TextView) findViewById(R.id.exerciseFileLocationTextView);
		exercise_play_view = (ImageView) findViewById(R.id.exercisePlayImageView);

		// Set the fields
		exercise_id.setText(Integer.toString(exercise.getId()));
		exercise_name.setText(exercise.getName());
		exercise_video_url.setText(exercise.getVideoUrl());

		exercise_instructions.setText(exercise.getInstructions());

		exercise_file_location.setText(exercise.getFileLocation());

		// /
		// http://android-er.blogspot.com/2011/05/create-thumbnail-for-video-using.html
		// MINI_KIND: 512 x 384 thumbnail
		exercise_play_view.setImageBitmap(ThumbnailUtils.createVideoThumbnail(exercise.getFileLocation(), Thumbnails.MINI_KIND));

		// Set the title of the Action Bar to the Exercise Name
		getActionBar().setTitle(exercise.getName());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.exercise_edit_view, menu);

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

			case R.id.action_save:
				saveExercise();
				break;

			case R.id.action_add:
				displayCustomExerciseDialog(null);
				break;

		}

		return true;
	}

	public void showPractitionerVideo(View view)
	{
		// add stuff to the intent
		Intent intent = new Intent(this, PractitionerVideoView.class);
		intent.putExtra("ExerciseClass", exercise);
		startActivity(intent);
	}

	private void saveExercise()
	{
		// Update all editable fields.
		String name = exercise_name.getText().toString();
		String instructions = exercise_instructions.getText().toString();
		displayToast("Saving exercise...");
		exercise.setName(name);
		exercise.setInstructions(instructions);
		exercise.update(dbSQLite);
		// reset the action bar
		getActionBar().setTitle(exercise.getName());

	}

	public void displayCustomExerciseDialog(View v)
	{

		String title = "Re-Record Exercise Video";
		String message = "Would you like to re-record your "
				+ exercise.getName()
				+ " video?\n\nThis will ERASE your current video and all annotations.";

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

				// Set the values and update the exercise.
				exercise.setFileLocation(mediaUri.getPath());

				exercise.update(dbSQLite);

				// /
				// http://android-er.blogspot.com/2011/05/create-thumbnail-for-video-using.html
				// MINI_KIND: 512 x 384 thumbnail
				exercise_play_view.setImageBitmap(ThumbnailUtils.createVideoThumbnail(exercise.getFileLocation(), Thumbnails.MINI_KIND));
				exercise_file_location.setText(exercise.getFileLocation());

			}
			catch (NullPointerException e)
			{
				e.printStackTrace();
			}

		}

	}

}

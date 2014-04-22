package edu.clemson.physicaltherapy.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import edu.clemson.physicaltherapy.R;
import edu.clemson.physicaltherapy.datamodel.DatabaseObject;
import edu.clemson.physicaltherapy.datamodel.Exercise;
import edu.clemson.physicaltherapy.datamodel.ExerciseAnnotation;
import edu.clemson.physicaltherapy.datamodel.ExerciseLog;
import edu.clemson.physicaltherapy.web.HTTPDownloader;
import edu.clemson.physicaltherapy.web.JSONParser;

public class MainActivity extends DisplayTableActivity {
	
	private enum field_order  
		{
			KEY_EXERCISE_ID,
			KEY_EXERCISE_NAME,
			KEY_EXERCISE_VIDEO_URL,
			KEY_EXERCISE_INSTRUCTION_URL,
			KEY_EXERCISE_FILE_LOCATION,
			DELETE_BUTTON,
			NEW_ROW;
		};

	private static int FONT_SIZE = 30;
	
	private boolean editMode = false;
	private Menu menu;
	
	private static String url_all_exercises = "http://people.cs.clemson.edu/~everetw/clemsonphysical/db_get_exercises.php";
	private static String url_all_annotations = "http://people.cs.clemson.edu/~everetw/clemsonphysical/db_get_annotations_for_exercise.php";
	
	private boolean downloadVideos;
	JSONParser jsonParser = new JSONParser();

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
  
		setContentView(R.layout.activity_main);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(false);
		}
		
		// get the default font size
		//http://stackoverflow.com/questions/6263250/convert-pixels-to-sp
			
		TextView testTextView = (TextView) findViewById(R.id.dummyTextView);
		float textSize = testTextView.getTextSize();
		FONT_SIZE = LayoutUtils.pixelsToSp(this, textSize);

	
	}
	
	

	
	private void createData()
	{
		Exercise.deleteAll(dbSQLite);
		ExerciseAnnotation.deleteAll(dbSQLite);
		//ExerciseLog.deleteAll(dbSQLite);
		Log.d("DEBUGGING","before creating data");
		new GetAllExercises().execute();
		new GetAllAnnotations().execute();


	    
	    //new GetAllAnnotations().execute();
	    Log.d("DEBUGGING","after creating data");
	    
//	    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
//        if (settings.getBoolean(SettingsActivity.KEY_DOWNLOAD, true))
//        {
//	    	new DownloadFiles().execute();
//	    }
	    //Exercise bicep;
		try {
			//bicep = Exercise.getByName(dbSQLite, "Bicep Curls");
		    List<DatabaseObject> sampleData = new ArrayList<DatabaseObject>();
			sampleData.add(new ExerciseLog(0,3,this.getExternalFilesDir("user_videos").getCanonicalPath()+"/VID_20140406_185047_526647753.mp4","Did bicep curls",""));
			sampleData.add(new ExerciseLog(0,3,this.getExternalFilesDir("user_videos").getCanonicalPath()+"/VID_20140408_090425_526647753.mp4","Did 15 bicep curls.\n1 set palms up. 1 set palms down. 1 set hammer curls.",""));
			for (int i = 0; i < sampleData.size(); i++)
	
				{
	
					sampleData.get(i).add(dbSQLite);

				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	    drawTable();
	}
	

	class GetAllExercises extends AsyncTask<String, String, String> {
		 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog == null)
            	pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading record from remote DB. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... args) {
 

                // Check for success tag
            int success;
            try {
                // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();                        
                        
                        // getting product details by making HTTP request
                // Note that product details url will use GET request
                JSONObject json = jsonParser.makeHttpRequest(url_all_exercises, "GET", params);
                Log.d("DEBUGGING","got json data");
 
                        // check your log for json response
                Log.d("Single Product Details", json.toString());
 
                        // json success tag
                success = json.getInt("success");
                if (success == 1) {
                    // successfully received product details
                    JSONArray productObj = json.getJSONArray("exercises"); // JSON Array
                    		Exercise entry = new Exercise();
                            for(int i=0; i<productObj.length(); i++){
                            	JSONObject dbObject = productObj.getJSONObject(i);
                            	entry.setObjectFromJSON(dbObject);
                            	entry.add(dbSQLite);
                            }
                        }
                else{
                            // product with pid not found
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String result) {
        	super.onPostExecute(result);
            // dismiss the dialog once done
            pDialog.dismiss();
        }
    }
	

	
	class GetAllAnnotations extends AsyncTask<String, String, String> {
		 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog == null)
            	pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading record from remote DB. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... args) {
 

                // Check for success tag
            int success;
            try {
                // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();                        
                        
                        // getting product details by making HTTP request
                // Note that product details url will use GET request
                JSONObject json = jsonParser.makeHttpRequest(url_all_annotations, "GET", params);
                Log.d("DEBUGGING","got json data");
 
                        // check your log for json response
                Log.d("Single Product Details", json.toString());
 
                        // json success tag
                success = json.getInt("success");
                if (success == 1) {
                    // successfully received product details
                    JSONArray productObj = json.getJSONArray("annotations"); // JSON Array
                    		ExerciseAnnotation entry = new ExerciseAnnotation();
                            for(int i=0; i<productObj.length(); i++){
                            	JSONObject dbObject = productObj.getJSONObject(i);
                            	entry.setObjectFromJSON(dbObject);
                            	entry.add(dbSQLite);
                            }
                        }
                else{
                            // product with pid not found
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String result) {
        	super.onPostExecute(result);
            // dismiss the dialog once done
        	drawTable();
            pDialog.dismiss();
        }
    }
	
	
	class DownloadFiles extends AsyncTask<String, String, String> {
		 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (pDialog == null)
            	pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Downloading video files from remote servers. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Getting product details in background thread
         * */
        protected String doInBackground(String... args) {
 
        	String filepath = args[0];

    		List<Exercise> exerciseList = Exercise.getAll(dbSQLite);
    		for (int i = 0; i < exerciseList.size(); i++)
    		{
    			Exercise e  = exerciseList.get(i);
    			
    			// If we have a remote video, get it. Local videos will have no URL.
    			if (!e.getVideoUrl().equals(""))
    			{

					String url = HTTPDownloader.getDirectUrl(e.getVideoUrl());
					String file = HTTPDownloader.getLocalFileNameFromUrl(e.getVideoUrl());
					System.err.println("Attempting to download "+filepath+file+" from "+url);
					boolean success = HTTPDownloader.downloadFile(url,filepath+file);
					// If we successfully downloaded the file, update the database.
					if (success)
						{
						System.err.println("Downloaded file. Updating database.");
						e.setVideoUrl(url);
						e.setFileLocation(file);
						e.update(dbSQLite);
						}
					
				
    			}
    			
    		}
    		return null;

 
            
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String result) {
        	super.onPostExecute(result);
            // dismiss the dialog once done
            pDialog.dismiss();
        }
    }
 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		/// http://stackoverflow.com/questions/7133141/android-changing-option-menu-items-programmatically
		this.menu = menu;
		getMenuInflater().inflate(R.menu.main, menu);
    	MenuItem itemToggleEdit = menu.findItem(R.id.toggle_edit_mode);
    	if (editMode)
    	{
    		itemToggleEdit.setTitle("Leave Edit Mode");
    	} else
    	{
    		itemToggleEdit.setTitle("Edit Custom Exercises");
    	}
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
 
        case R.xml.settings:
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            break;
 
        case R.layout.activity_info_view:
            Intent intent = new Intent(this, InfoView.class);
            startActivity(intent);
            break;
            
        case R.id.toggle_edit_mode:
        	editMode = !editMode;
        	MenuItem itemToggleEdit = menu.findItem(R.id.toggle_edit_mode);
        	if (editMode)
        	{
        		itemToggleEdit.setTitle("Leave Edit Mode");
        	} else
        	{
        		itemToggleEdit.setTitle("Edit Custom Exercises");
        	}
        	drawTable();
        	break;
            
        case R.id.create_data:
            createData();
            break;
        
        case R.id.action_custom_exercise:
        	displayCustomExerciseDialog(null);
        	break;
        
        case R.id.download_videos:
        	try {
				new DownloadFiles().execute(this.getExternalFilesDir("exercises").getCanonicalPath()+"/");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	break;
        
        }
        
 
        return true;
    }
	
	public void showExercise(View view){
		Intent intent = new Intent(this, ExerciseView.class);
		startActivity(intent);
	}

	
	public void showPractitionerVideo(View view){
		Intent intent = new Intent(this, PractitionerVideoView.class);
		startActivity(intent);
	}

	@Override
	protected String getTableName() {
		
		return Exercise.TABLE_NAME;
	}


	@Override
	protected void drawTable() {
		
		
		// Get the Table Layout 
	    TableLayout tableLayout = (TableLayout) findViewById(R.id.tlGridTable);
	    //TODO: Next 3 lines in XML
	    tableLayout.setBaselineAligned(false);
	    tableLayout.setShowDividers(TableLayout.SHOW_DIVIDER_BEGINNING | TableLayout.SHOW_DIVIDER_END | TableLayout.SHOW_DIVIDER_MIDDLE);
	    tableLayout.setDividerPadding(2);
	    tableLayout.removeAllViews();
	    tableLayout.setWeightSum(1f);
	    tableLayout.setStretchAllColumns(true);
	   
	     // Create the labels
    	TableRow tableRow = LayoutUtils.createTableRow(this);
    	tableRow.setId(0);
    	
    	
        TextView textView = LayoutUtils.createTextView(this, Exercise.DbKeys.KEY_ID.getKeyLabel(), FONT_SIZE,LayoutUtils.LIGHT_GRAY, LayoutUtils.DARK_GRAY);
    	textView.setVisibility(View.GONE);
    	tableRow.addView(textView);
        

    	
        textView = LayoutUtils.createTextView(this, Exercise.DbKeys.KEY_EXERCISE_NAME.getKeyLabel(), FONT_SIZE, LayoutUtils.LIGHT_GRAY, LayoutUtils.DARK_GRAY);
        LayoutParams trlp = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1f);
        trlp.setMargins(2, 2, 2, 2);
        textView.setLayoutParams(trlp);
        
        tableRow.addView(textView);
        //TODO Add on click listener

        textView = LayoutUtils.createTextView(this, Exercise.DbKeys.KEY_EXERCISE_VIDEO_URL.getKeyLabel(), FONT_SIZE, LayoutUtils.LIGHT_GRAY, LayoutUtils.DARK_GRAY);
        tableRow.addView(textView);
        textView.setVisibility(View.GONE);
        
        textView = LayoutUtils.createTextView(this, Exercise.DbKeys.KEY_EXERCISE_INSTRUCTIONS.getKeyLabel(), FONT_SIZE, LayoutUtils.LIGHT_GRAY, LayoutUtils.DARK_GRAY);
        tableRow.addView(textView);
        textView.setVisibility(View.GONE);
        
        textView = LayoutUtils.createTextView(this, Exercise.DbKeys.KEY_EXERCISE_FILE_LOCATION.getKeyLabel(), FONT_SIZE, LayoutUtils.LIGHT_GRAY, LayoutUtils.DARK_GRAY);
        tableRow.addView(textView);
        textView.setVisibility(View.GONE);
        
        // New Row Indicator = Must be last 
        textView = LayoutUtils.createTextView(this, "New Row", FONT_SIZE, LayoutUtils.DARK_GRAY,LayoutUtils.LIGHT_GRAY);
        textView.setVisibility(View.GONE);
        tableRow.addView(textView);
        
        
        textView = LayoutUtils.createTextView(this, "Delete",  FONT_SIZE, LayoutUtils.BACKGROUND_COLOR, LayoutUtils.TEXT_COLOR);
        if (editMode)
        {
        	textView.setVisibility(View.VISIBLE);	
        }
        else
        {
        	textView.setVisibility(View.GONE);
        }
        
        tableRow.addView(textView);
        
        
        // Override the on click listener to do nothing.
        tableRow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
        

        
        tableLayout.addView(tableRow);
		


        
        //LinkedList<DatabaseObject>();
        //rowList.add(tableRow);

        List<Exercise> exerciselist = null;
        

        // TODO Get exercises from plan, if set in preferences.
        exerciselist = Exercise.getAll(dbSQLite);
   
        
        for (int i = 0; i < exerciselist.size(); i++){
        	
        	
            // First row: Entered data
        	tableRow = LayoutUtils.createTableRow(this);
        	
        	
        	Exercise exercise = (Exercise) exerciselist.get(i);
        	
        	// If not edit mode, display all exercises. If edit mode, only display user exercises.
        	if (!editMode || exercise.getVideoUrl().equals(""))
        	{
        
	        	//System.err.println("Exercise Id="+exercise.getId()+" Name="+exercise.getName());
	        	
	        	textView = LayoutUtils.createTextView(this, Integer.toString(exercise.getId()), FONT_SIZE, LayoutUtils.DARK_GRAY,LayoutUtils.LIGHT_GRAY);
	        	textView.setVisibility(View.GONE);
	        	tableRow.addView(textView);
	            
	        	
	            textView = LayoutUtils.createTextView(this, exercise.getName(), FONT_SIZE, LayoutUtils.DARK_GRAY,LayoutUtils.LIGHT_GRAY);
	            trlp = new TableRow.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,1f);
	            trlp.setMargins(2, 2, 2, 2);
	            textView.setLayoutParams(trlp);
	            
	            textView.setOnClickListener(new View.OnClickListener() {
	    			
	    			@Override
	    			public void onClick(View v) {
	    				
	    				
	    				ViewGroup vp = (ViewGroup) v.getParent();
						vp.performClick();
	    				//displayToast(((TextView)v).getText().toString() + " clicked!");
	    				onExerciseFieldClick(v);
	    				// Start the intent.
	    				// Send to the exercise screen.
	
	
	    			}
	    		});
	            
	            tableRow.addView(textView);
	            
	            textView = LayoutUtils.createTextView(this, exercise.getVideoUrl(), FONT_SIZE, LayoutUtils.LIGHT_GRAY, LayoutUtils.DARK_GRAY);
	            tableRow.addView(textView);
	            textView.setVisibility(View.GONE);
	            
	            textView = LayoutUtils.createTextView(this, exercise.getInstructions(), FONT_SIZE, LayoutUtils.LIGHT_GRAY, LayoutUtils.DARK_GRAY);
	            tableRow.addView(textView);
	            textView.setVisibility(View.GONE);
	            
	            textView = LayoutUtils.createTextView(this, exercise.getFileLocation(), FONT_SIZE, LayoutUtils.LIGHT_GRAY, LayoutUtils.DARK_GRAY);
	            tableRow.addView(textView);
	            textView.setVisibility(View.GONE);
	    
	            
		        ImageButton button = new ImageButton(this);
		        
		        
	
					button.setImageResource(android.R.drawable.ic_menu_delete); 	
			        button.setOnClickListener(new View.OnClickListener(){
			            public void onClick(View v){
			                 // Do some operation for minus after getting v.getId() to get the current row
			            	// http://stackoverflow.com/questions/14112044/android-how-to-get-the-id-of-a-parent-view
			            	 //save button.
			            	TableRow tr = (TableRow)v.getParent();
			            	
			            	//Receipt receipt = getReceiptFromTableRow(tr);
			            	Exercise exercise = getExerciseFromTableRow(tr);
			            	String keys = LayoutUtils.getKeysFromTableRow(tr);
		
			            	deleteExerciseDialog(exercise);
			            	
			            	//send click through to parent.
			            	tr.performClick();
			            	
		
			            	TableLayout tl = (TableLayout)tr.getParent();
			            	tl.removeView(tr);
			            	
			            	
			            }
			        
			            
			        });
				
				
		        
		        if (editMode)
		        {
		        	button.setVisibility(View.VISIBLE);	
		        }
		        else
		        {
		        	button.setVisibility(View.GONE);
		        }
		        
		        tableRow.addView(button);
		        
		        // New Row Indicator = Must be last 
		        textView = LayoutUtils.createTextView(this, "false", FONT_SIZE, LayoutUtils.DARK_GRAY,LayoutUtils.LIGHT_GRAY);
		        textView.setVisibility(View.GONE);
		        tableRow.addView(textView);
	
		        tableLayout.addView(tableRow);
		        
        	}
        
        }
        
		
	}
	
	private Exercise getExerciseFromTableRow(TableRow tr)
	{
		// ID - child 0
		int  id =  Integer.parseInt(((TextView)tr.getChildAt(field_order.KEY_EXERCISE_ID.ordinal())).getText().toString());
		
		// Exercise Name
		String name = ((TextView)tr.getChildAt(field_order.KEY_EXERCISE_NAME.ordinal())).getText().toString();
		
		// Video Url
		String video_url = ((TextView)tr.getChildAt(field_order.KEY_EXERCISE_VIDEO_URL.ordinal())).getText().toString();
		
		// Instruction URL
		String instruction_url = ((TextView)tr.getChildAt(field_order.KEY_EXERCISE_INSTRUCTION_URL.ordinal())).getText().toString();
		
		// File location
		String file_location = ((TextView)tr.getChildAt(field_order.KEY_EXERCISE_FILE_LOCATION.ordinal())).getText().toString();

		
		return new Exercise(id,name,video_url,instruction_url,file_location);
	
	}

	private void deleteExerciseDialog(final Exercise exercise)
	{
		
		
    	String title = "Confirm Delete!";
    	
    	String message = "Are you sure you want to delete exercise "+exercise.getName()+" AND all log entries?";


		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		alertDialogBuilder.setTitle(title);
		

		alertDialogBuilder.setMessage(message);
		// set dialog message
		alertDialogBuilder
			.setCancelable(false)
			.setPositiveButton("Yes",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
					// get user input and set it to result
			    	deleteExercise(exercise);
				
			    }
			  })
			.setNegativeButton("No",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
			    // Continue the video
				dialog.cancel();
			    }
			  });

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}
	
	private void deleteExercise(Exercise exercise)
	{
		
		try 
		{
			//System.err.println("Deleting logs for exercise "+exercise.getName()+" with id "+ exercise.getId());
			ExerciseLog.deleteAllByExerciseId(dbSQLite, exercise.getId());
			//System.err.println("Deleting exercise "+exercise.getName()+" with id "+ exercise.getId());
			exercise.delete(dbSQLite);	
		}
		catch (Exception e)
		{
			displayMessageDialog(e.getMessage(),e.toString());
			e.printStackTrace();
		}
	}

	private void onExerciseFieldClick(View v) 
	{
		// Get the table row
    	TableRow tr = (TableRow)v.getParent();
    	
    	Exercise exercise = getExerciseFromTableRow(tr);
		
		Intent intent = null;
		if (editMode && exercise.getVideoUrl().equals(""))
		{
			intent = new Intent(this,ExerciseEditView.class);
		}
		else
		{
			intent = new Intent(this,ExerciseView.class);
		}
		
    	
    	/// http://stackoverflow.com/questions/2736389/how-to-pass-object-from-one-activity-to-another-in-android
    	intent.putExtra("ExerciseClass", exercise);
    	// Only edit user exercises.
    	intent.putExtra("edit_mode", editMode && exercise.getVideoUrl().equals(""));
    	 
    	
		startActivity(intent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);

		
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) 
		{
			/// Fixed null pointer exceptions from - http://www.androidhive.info/2013/09/android-working-with-camera-api/
	        try {
	        	
	        	// Capture the log values.
	        	
	        	
	        	displayCustomExerciseResultsDialog();

	        } catch (NullPointerException e) {
	            e.printStackTrace();
	        }
	        
	    }

	}

/// http://www.mkyong.com/android/android-prompt-user-input-dialog-example/
	private void displayCustomExerciseResultsDialog() {
		displayCustomExerciseResultsDialog("","");
	}
	
	
	private void displayCustomExerciseResultsDialog(String exercise_name, String exercise_instructions) {
		
    	String title = "Custom Exercise Details";
    	

		
		// get prompts.xml view
		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = li.inflate(R.layout.custom_exercise_dialog, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		alertDialogBuilder.setTitle(title);
		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		final EditText exerciseNameInput = (EditText) promptsView
				.findViewById(R.id.exerciseNameEditText);
		
		final EditText exerciseInstructionsInput = (EditText) promptsView
				.findViewById(R.id.exerciseInstructionsEditText);
		
		exerciseNameInput.setText(exercise_name);
		exerciseInstructionsInput.setText(exercise_instructions);
		

		// set dialog message
		alertDialogBuilder
			.setCancelable(false)
			.setPositiveButton("OK",
			  new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,int id) {
					// get user input and set it to result
					// edit text
					String exercise_name = exerciseNameInput.getText().toString();
					String exercise_instructions = exerciseInstructionsInput.getText().toString();
					Exercise exercise = new Exercise(0,exercise_name,"",exercise_instructions,mediaUri.getPath());
					try {
						exercise.add(dbSQLite);
						drawTable();
					} catch (Exception e) {
						
						//e.printStackTrace();
						displayOverwriteExerciseDialog(exercise_name,exercise_instructions,mediaUri.getPath());
						
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
	
    protected void displayOverwriteExerciseDialog(final String exercise_name, final String exercise_instructions, final String exercise_path) {
    	
     	String title = "Overwrite Exercise?";
    	

		
    		// get prompts.xml view
    		LayoutInflater li = LayoutInflater.from(this);
    		
    		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
    				this);

    		alertDialogBuilder.setTitle(title);
    		

    		// set dialog message
    		alertDialogBuilder
    			.setMessage("Exercise "+exercise_name+" already exists in the database. Do you want to overwrite it?")
    			.setCancelable(false)
    			.setPositiveButton("OK",
    			  new DialogInterface.OnClickListener() {
    			    public void onClick(DialogInterface dialog,int id) {
    					// get user input and set it to result
    					// edit text
    					try {
    						// If add doesn't work, try update.
    						
    						Exercise exercise = Exercise.getByName(dbSQLite, exercise_name);
    						exercise.setVideoUrl("");
    						exercise.setInstructions(exercise_instructions);
    						exercise.setFileLocation(mediaUri.getPath());
    						exercise.update(dbSQLite);
    						// Force redraw to get updated values.
    						drawTable();
    					} catch (Exception e1) {
    						
    						e1.printStackTrace();
    						displayMessageDialog("SQLError","Could not save "+exercise_name);
    					}
    					
    			
    				
    			    }
    			  })
    			.setNegativeButton("No",
    			  new DialogInterface.OnClickListener() {
    			    public void onClick(DialogInterface dialog,int id) {
    			    	dialog.cancel();
    			    	displayCustomExerciseResultsDialog(exercise_name, exercise_instructions);
    			    	
    				
    			    }
    			  });

    		// create alert dialog
    		AlertDialog alertDialog = alertDialogBuilder.create();

    		// show it
    		alertDialog.show();

		
		
	}




	public void displayCustomExerciseDialog(View v)
    {
    	
    	String title = "Record Custom Exercise";
    	String message = "Would you like to record a custom exercise?";
    			
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
	protected String getVideoSubdirectory()
	{
		return "custom_exercises";
	}
    




}
